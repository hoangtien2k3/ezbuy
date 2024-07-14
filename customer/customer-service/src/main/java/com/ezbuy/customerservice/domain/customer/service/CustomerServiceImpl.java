package com.ezbuy.customerservice.domain.customer.service;//package com.ezbuy.customer.domain.customer.service;

import com.ezbuy.customermodel.dto.CustomerDTO;
import com.ezbuy.customermodel.dto.NotiContentDTO;
import com.ezbuy.customermodel.dto.ReceiverDataDTO;
import com.ezbuy.customermodel.dto.request.CreateCustomerRequest;
import com.ezbuy.customermodel.dto.ValidateCustomerDto;
import com.ezbuy.customermodel.dto.request.CreateNotificationDTO;
import com.ezbuy.customermodel.model.Address;
import com.ezbuy.customermodel.model.Customer;
import com.ezbuy.customermodel.model.ShopUser;
import com.ezbuy.customerservice.app.common.constants.Const;
import com.ezbuy.customerservice.domain.address.repository.AddressRepository;
import com.ezbuy.customerservice.domain.customer.repository.CustomerGroupRepository;
import com.ezbuy.customerservice.domain.customer.repository.CustomerRepository;
//import com.ezbuy.customerservice.domain.notification.NotiServiceClient;
import com.ezbuy.customerservice.domain.shopuser.repository.ShopUserRepository;
import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.constants.Regex;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.framework.utils.AppUtils;
import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.framework.utils.PasswordGenerator;
import com.ezbuy.framework.utils.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ShopUserRepository shopUserRepository;
    private final CustomerGroupRepository customerGroupRepository;
//    private final NotiServiceClient notiServiceClient;

    @Override
    public Mono<DataResponse<CustomerDTO>> createCustomer(CreateCustomerRequest customerRequest) {
        log.info("create customer: {}", customerRequest);
        return validRequestCreateCustomer(customerRequest)
                .flatMap(validate -> {
                    if (!validate.getIsValid()) {
                        return Mono.error(new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()), validate.getErrorMessage()));
                    }
                    String displayName = DataUtil.isNullOrEmpty(customerRequest.displayName()) ? customerRequest.firstName() + " " + customerRequest.lastName() : customerRequest.displayName();
                    Boolean isSendMail = DataUtil.isNullOrEmpty(customerRequest.subscribedToNewsletter()) ? Const.FALSE : customerRequest.subscribedToNewsletter();
                    Customer customer = Customer.builder()
                            .customerGroupId(Const.CUST_GROUP.NEW)
                            .firstName(customerRequest.firstName())
                            .lastName(customerRequest.lastName())
                            .displayName(displayName)
                            .phoneNumber(customerRequest.phoneNumber())
                            .email(customerRequest.email())
                            .password(customerRequest.password())
                            .updatedAt(LocalDateTime.now())
                            .subscribedToNewsletter(isSendMail)
                            .build();
                    return customerRepository.save(customer)
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
                                        DataUtil.optional(addressRepository.save(address)).doOnError(throwable -> log.error("Save address error: {}", throwable.getMessage())),
                                        DataUtil.optional(shopUserRepository.save(shopUser)).doOnError(throwable -> log.error("Save shopUser error: {}", throwable.getMessage()))
                                ).thenReturn(savedCustomer);
                            }).flatMap(savedCustomer -> Mono.just(DataResponse.success(CustomerDTO.fromModel(savedCustomer))))
                            .doOnSuccess(dto -> {
                                if (isSendMail) {
//                                    AppUtils.runHiddenStream(sendMail(dto.getData().username(), dto.getData().id().toString(), dto.getData().email(), false));
                                }
                                // sending info account for customer
//                                AppUtils.runHiddenStream(sendMail(dto.getData().username(), dto.getData().id().toString(), dto.getData().email(), true));
                            })
                            .doOnError(throwable -> log.error("Error saving customer data: {}", throwable.getMessage()));
                }).doOnError(throwable -> log.error("Insert error: {}", throwable.getMessage()));
    }

    private Mono<ValidateCustomerDto> validRequestCreateCustomer(CreateCustomerRequest customerRequest) {
        ValidateCustomerDto customerDto = new ValidateCustomerDto(Const.TRUE, Const.EMPTY);
        try {
            if (DataUtil.isNullOrEmpty(customerRequest.firstName())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("first.name.null.or.empty"));
            }
            if (DataUtil.isNullOrEmpty(customerRequest.lastName())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("last.name.null.or.empty"));
            }
            if (DataUtil.isNullOrEmpty(customerRequest.phoneNumber())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("contact.phone.number.null.or.empty"));
            }
            if (DataUtil.isNullOrEmpty(customerRequest.email())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("contact.email.null.or.empty"));
            }
            if (DataUtil.isNullOrEmpty(customerRequest.password())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("contact.password.null.or.empty"));
            }
            // Validate password length
            if (customerRequest.password().length() < Const.CUST_PASSWORD.MIN_LENGTH ||
                    customerRequest.password().length() > Const.CUST_PASSWORD.MAX_LENGTH) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS,
                        Translator.toLocaleVi("customer.password.length", Const.CUST_PASSWORD.MIN_LENGTH, Const.CUST_PASSWORD.MAX_LENGTH)));
            }
            // validate regex
            if (!customerRequest.phoneNumber().matches(Regex.PHONE_REGEX)) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("customer.phone.number.invalid"));
            }
            if (!customerRequest.email().matches(Regex.EMAIL_REGEX)) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("customer.email.invalid"));
            }
            if (!customerRequest.password().matches(Regex.PASSWORD_REGEX)) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("customer.password.invalid"));
            }

            return Mono.just(customerDto);
        } catch (BusinessException e) {
            log.error("validate error: ", e);
            customerDto.setIsValid(Const.FALSE);
            customerDto.setErrorMessage(e.getMessage());
            return Mono.just(customerDto);
        } catch(Exception ex) {
            log.error("Exception: validate error: ", ex);
            customerDto.setIsValid(Const.FALSE);
            customerDto.setErrorMessage(ex.getMessage());
            return Mono.just(customerDto);
        }
    }

//    private Mono<Boolean> sendMail(String userName, String customerId, String email, boolean hasPassword) {
//        String subTitle, title, template;
//        if (hasPassword) {
//            String password = PasswordGenerator.generateCommonLangPassword();
//            subTitle = userName + "-" + password;
//            title = Translator.toLocaleVi("email.title.register.customer.success");
//            template = Const.TemplateMail.CUSTOMER_REGISTER_SUCCESS;
//        } else {
//            subTitle = userName;
//            title = Translator.toLocaleVi("email.title.active.account.success");
//            template = Const.TemplateMail.CUSTOMER_ACTIVE_SUCCESS;
//        }
//        CreateNotificationDTO createNotificationDTO = getNotificationDTO(subTitle, title, template, ReceiverDataDTO.builder().userId(customerId).email(email).build());
//        return notiServiceClient.insertTransmission(createNotificationDTO).flatMap(objects -> {
//            if (objects.isPresent() && (DataUtil.isNullOrEmpty(objects.get().getErrorCode()) && !DataUtil.isNullOrEmpty(objects.get().getMessage()))) {
//                log.info("Send mail to customer {} success ", email);
//                return Mono.just(true);
//            }
//            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, (objects.isPresent()) ? objects.get().getMessage() : "params.invalid"));
//        }).onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "noti.service.error")));
//    }

    private CreateNotificationDTO getNotificationDTO(String subTitle, String title, String template, ReceiverDataDTO data) {
        CreateNotificationDTO createNotificationDTO = new CreateNotificationDTO();
        createNotificationDTO.setSender("system");
        createNotificationDTO.setSeverity(Const.Notification.SEVERITY);
        createNotificationDTO.setTemplateMail(template);
        createNotificationDTO.setNotiContentDTO(NotiContentDTO.builder().subTitle(subTitle).title(title).build());
        createNotificationDTO.setContentType(Const.Notification.CONTENT_TYPE);
        createNotificationDTO.setCategoryType(Const.Notification.CATEGORY_TYPE);
        createNotificationDTO.setChannelType(Const.Notification.CHANNEL_TYPE);
        List<ReceiverDataDTO> list = new ArrayList<>();
        list.add(data);
        createNotificationDTO.setReceiverList(list);
        return createNotificationDTO;
    }

}
