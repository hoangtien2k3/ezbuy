package com.ezbuy.customer.service.impl;

import com.ezbuy.customer.client.NotiServiceClient;
import com.ezbuy.customer.constants.Const;
import com.ezbuy.customer.model.NotiContentDTO;
import com.ezbuy.customer.model.ReceiverDataDTO;
import com.ezbuy.customer.model.dto.CustomerDTO;
import com.ezbuy.customer.model.dto.ValidateCustomerDto;
import com.ezbuy.customer.model.dto.request.AuthenticationRequest;
import com.ezbuy.customer.model.dto.request.CreateCustomerRequest;
import com.ezbuy.customer.model.dto.request.CreateNotificationDTO;
import com.ezbuy.customer.model.dto.response.AuthenticationResponse;
import com.ezbuy.customer.model.postgresql.Address;
import com.ezbuy.customer.model.postgresql.Customer;
import com.ezbuy.customer.model.postgresql.ShopUser;
import com.ezbuy.customer.repository.AddressRepository;
import com.ezbuy.customer.repository.CustomerGroupRepository;
import com.ezbuy.customer.repository.CustomerRepository;
import com.ezbuy.customer.repository.ShopUserRepository;
import com.ezbuy.customer.service.CustomerService;
import com.ezbuy.customer.service.TokenService;
import com.reactify.util.AppUtils;
import com.reactify.util.DataUtil;
import com.reactify.util.PasswordGenerator;
import com.reactify.util.Translator;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ShopUserRepository shopUserRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveUserDetailsService reactiveUserDetailsService;
    private final TokenService tokenProvider;
    private final NotiServiceClient notiServiceClient;

    @Override
    public Mono<DataResponse<CustomerDTO>> createCustomer(CreateCustomerRequest customerRequest) {
        log.info("create customer: {}", customerRequest);
        return validRequestCreateCustomer(customerRequest)
                .flatMap(validate -> {
                    if (!validate.isValid()) {
                        return Mono.error(new BusinessException(
                                String.valueOf(HttpStatus.BAD_REQUEST.value()), validate.errorMessage()));
                    }

                    // check customer exist in database
                    customerRepository
                            .findByEmail(customerRequest.email())
                            .flatMap(customer -> {
                                if (customer != null && customer.getEmail().equals(customerRequest.email())) {
                                    return Mono.error(new BusinessException(
                                            String.valueOf(HttpStatus.BAD_REQUEST.value()), "customer.exist"));
                                }

                                return Mono.empty();
                            })
                            .switchIfEmpty(Mono.just(Const.TRUE));

                    String displayName = DataUtil.isNullOrEmpty(customerRequest.displayName())
                            ? customerRequest.firstName() + " " + customerRequest.lastName()
                            : customerRequest.displayName();
                    Boolean isSendMail = DataUtil.isNullOrEmpty(customerRequest.subscribedToNewsletter())
                            ? Const.FALSE
                            : customerRequest.subscribedToNewsletter();
                    String encodedPassword = passwordEncoder.encode(customerRequest.password());
                    log.info("Encoded password: {}", encodedPassword);
                    Customer customer = Customer.builder()
                            .customerGroupId(Const.CUST_GROUP.NEW)
                            .username(customerRequest.email())
                            .firstName(customerRequest.firstName())
                            .lastName(customerRequest.lastName())
                            .displayName(displayName)
                            .phoneNumber(customerRequest.phoneNumber())
                            .email(customerRequest.email())
                            .password(encodedPassword)
                            .updatedAt(LocalDateTime.now())
                            .subscribedToNewsletter(isSendMail)
                            .build();
                    log.info("Encoded password: {}", customer.getPassword());
                    return customerRepository
                            .save(customer)
                            .flatMap(savedCustomer -> {
                                Address address = Address.builder()
                                        .customerId(savedCustomer.getId())
                                        .build();
                                ShopUser shopUser = ShopUser.builder()
                                        .customerId(savedCustomer.getId())
                                        .statusAccount(Const.STATUS_ACCOUNT.ACTIVE)
                                        .roles(Const.ROLE.USER)
                                        .build();
                                return Mono.zip(
                                                DataUtil.optional(addressRepository.save(address))
                                                        .doOnError(throwable -> log.error(
                                                                "Save address error: {}", throwable.getMessage())),
                                                DataUtil.optional(shopUserRepository.save(shopUser))
                                                        .doOnError(throwable -> log.error(
                                                                "Save shopUser error: {}", throwable.getMessage())))
                                        .thenReturn(savedCustomer);
                            })
                            .flatMap(savedCustomer ->
                                    Mono.just(DataResponse.success(CustomerDTO.fromModel(savedCustomer))))
                            .doOnSuccess(dto -> {
                                if (isSendMail) {
                                    AppUtils.runHiddenStream(sendMail(
                                            dto.getData().username(),
                                            dto.getData().id().toString(),
                                            dto.getData().email(),
                                            false));
                                }
                                // sending info account for customer
                                AppUtils.runHiddenStream(sendMail(
                                        dto.getData().username(),
                                        dto.getData().id().toString(),
                                        dto.getData().email(),
                                        true));
                            })
                            .doOnError(
                                    throwable -> log.error("Error saving customer data: {}", throwable.getMessage()));
                })
                .doOnError(throwable -> log.error("Insert error: {}", throwable.getMessage()));
    }

    @Override
    public Mono<DataResponse<AuthenticationResponse>> authenticateRequest(AuthenticationRequest authRequest) {
        return validateCustomerLogin(authRequest).flatMap(validate -> {
            if (!validate.isValid()) {
                return Mono.error(new BusinessException(validate.errorCode(), validate.errorMessage()));
            }
            return reactiveUserDetailsService
                    .findByUsername(authRequest.email())
                    .flatMap(cus -> {
                        log.info("Stored password: {}", cus.getPassword());
                        log.info("Input password: {}", authRequest.password());
                        if (passwordEncoder.matches(authRequest.password(), cus.getPassword())) {
                            String accessToken = tokenProvider.generateToken(cus);
                            String refreshToken = null;
                            AuthenticationResponse authResponse = new AuthenticationResponse(accessToken, refreshToken);
                            return Mono.just(DataResponse.success(authResponse));
                        } else {
                            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                        }
                    })
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
        });
    }

    private Mono<ValidateCustomerDto> validRequestCreateCustomer(CreateCustomerRequest customerRequest) {
        ValidateCustomerDto customerDto =
                new ValidateCustomerDto(Const.TRUE, Const.ErrorCode.ERROR_CODE_SUCCESS, Const.EMPTY);
        try {
            if (DataUtil.isNullOrEmpty(customerRequest.firstName())) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.notNull", "first.name");
            }
            if (DataUtil.isNullOrEmpty(customerRequest.lastName())) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.notNull", "last.name");
            }
            if (DataUtil.isNullOrEmpty(customerRequest.phoneNumber())) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.notNull", "contact.phone.number");
            }
            if (DataUtil.isNullOrEmpty(customerRequest.email())) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.notNull", "contact.email");
            }
            if (DataUtil.isNullOrEmpty(customerRequest.password())) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.notNull", "contact.password");
            }
            // Validate password length
            if (customerRequest.password().length() < Const.CUST_PASSWORD.MIN_LENGTH
                    || customerRequest.password().length() > Const.CUST_PASSWORD.MAX_LENGTH) {
                return Mono.error(new BusinessException(INVALID_PARAMS, "customer.password.length", "8", "20"));
            }
            // validate regex
            if (!customerRequest.phoneNumber().matches(PHONE_REGEX)) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.invalid", "customer.phone.number");
            }
            if (!customerRequest.email().matches(EMAIL_REGEX)) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.invalid", "contact.email");
            }
            if (!customerRequest.password().matches(PASSWORD_REGEX)) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.invalid", "contact.password");
            }
            return Mono.just(customerDto);
        } catch (BusinessException e) {
            return Mono.just(new ValidateCustomerDto(Const.FALSE, e.getErrorCode(), e.getMessage()));
        } catch (Exception ex) {
            return Mono.just(new ValidateCustomerDto(Const.FALSE, INVALID_PARAMS, ex.getMessage()));
        }
    }

    private Mono<ValidateCustomerDto> validateCustomerLogin(AuthenticationRequest auth) {
        ValidateCustomerDto customerDto =
                new ValidateCustomerDto(Const.TRUE, Const.ErrorCode.ERROR_CODE_SUCCESS, Const.EMPTY);
        try {
            if (DataUtil.isNullOrEmpty(auth.email())) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.notNull", "contact.email");
            }
            if (DataUtil.isNullOrEmpty(auth.password())) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.notNull", "contact.password");
            }
            if (!auth.email().matches(EMAIL_REGEX)) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.invalid", "contact.email");
            }
            if (!auth.password().matches(PASSWORD_REGEX)) {
                throw new BusinessException(INVALID_PARAMS, "customer.input.invalid", "contact.password");
            }
            return Mono.just(customerDto);
        } catch (BusinessException e) {
            return Mono.just(new ValidateCustomerDto(Const.FALSE, e.getErrorCode(), e.getMessage()));
        }
    }

    private Mono<Boolean> sendMail(String userName, String customerId, String email, boolean hasPassword) {
        String subTitle, title, template;
        if (hasPassword) {
            String password = PasswordGenerator.generateCommonLangPassword();
            subTitle = userName + "-" + password;
            title = Translator.toLocaleVi("email.title.register.customer.success");
            template = Const.TemplateMail.CUSTOMER_REGISTER_SUCCESS;
        } else {
            subTitle = userName;
            title = Translator.toLocaleVi("email.title.active.account.success");
            template = Const.TemplateMail.CUSTOMER_ACTIVE_SUCCESS;
        }
        CreateNotificationDTO createNotificationDTO = getNotificationDTO(
                subTitle,
                title,
                template,
                ReceiverDataDTO.builder().userId(customerId).email(email).build());
        return notiServiceClient
                .insertTransmission(createNotificationDTO)
                .flatMap(objects -> {
                    if (objects.isPresent()
                            && (DataUtil.isNullOrEmpty(objects.get().getErrorCode())
                                    && !DataUtil.isNullOrEmpty(objects.get().getMessage()))) {
                        log.info("Send mail to customer {} success ", email);
                        return Mono.just(true);
                    }
                    return Mono.error(new BusinessException(
                            INVALID_PARAMS,
                            (objects.isPresent()) ? objects.get().getMessage() : "params.invalid"));
                })
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "noti.service.error")));
    }

    private CreateNotificationDTO getNotificationDTO(
            String subTitle, String title, String template, ReceiverDataDTO data) {
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();
        createNotificationDTO.setSender("system");
        createNotificationDTO.setSeverity(Const.Notification.SEVERITY);
        createNotificationDTO.setTemplateMail(template);
        createNotificationDTO.setNotiContentDTO(
                NotiContentDTO.builder().subTitle(subTitle).title(title).build());
        createNotificationDTO.setContentType(Const.Notification.CONTENT_TYPE);
        createNotificationDTO.setCategoryType(Const.Notification.CATEGORY_TYPE);
        createNotificationDTO.setChannelType(Const.Notification.CHANNEL_TYPE);
        List<ReceiverDataDTO> list = new ArrayList<>();
        list.add(data);
        createNotificationDTO.setReceiverList(list);
        return createNotificationDTO;
    }
}
