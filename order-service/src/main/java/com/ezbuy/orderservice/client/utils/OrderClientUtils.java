package com.ezbuy.orderservice.client.utils;

import com.ezbuy.ordermodel.constants.Constants;
import com.ezbuy.ordermodel.dto.*;
import com.ezbuy.ordermodel.dto.request.CreateOrderPaidRequest;
import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.ordermodel.dto.ws.CustIdentity;
import com.ezbuy.ordermodel.dto.ws.ProductOrderItemWsDTO;
import com.ezbuy.ordermodel.dto.ws.SearchOrderStateRequest;
import com.ezbuy.orderservice.service.PartnerLicenseKeyService;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.DataWsUtil;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.factory.MarshallerFactory;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.BeanUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ezbuy.ordermodel.constants.Constants.GROUP_TYPE_ORGANIZATION;

public class OrderClientUtils {
    private static final String ORDER_REQUEST_TEMPLATE = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.order.bccs.viettel.com/\">\n" +
            "   <soapenv:Header>\n" +
            "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
            "         <wsse:UsernameToken>\n" +
            "            <wsse:Username>{0}</wsse:Username>\n" +
            "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n" +
            "         </wsse:UsernameToken>\n" +
            "      </wsse:Security>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "  {2}" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    private static final String ORDER_REQUEST_TEMPLATE_V2 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.order.viettel.com/\">" +
            "   <soapenv:Header>\n" +
            "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
            "         <wsse:UsernameToken>\n" +
            "            <wsse:Username>{0}</wsse:Username>\n" +
            "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n" +
            "         </wsse:UsernameToken>\n" +
            "      </wsse:Security>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "  {2}" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";


    private static final String PLACE_ORDER_BODY_TEMPLATE = "<ser:placeOrder>\n" +
            "\t  <orderType>{0}</orderType>         \n" +
            "\t  <data>{1}</data>\n" +
            "      </ser:placeOrder>";

    private static final String SEARCH_ORDER_STATE_TEMPLATE = "<ser:searchOrderStateByBpId>\n" +
            "{0}\n" +
            "      </ser:searchOrderStateByBpId>";

    private static final String PRICING_PRODUCT = "<ser:pricingProductsWithViewModeExt>\n"+
            "{0}\n" +
            "</ser:pricingProductsWithViewModeExt>";

    public final static String GET_PROFILE_KHDN = " <ser:getProfileKHDN>\n" +
            "         <data>\n" +
            "{0}" +
            "</data>\n" +
            "      </ser:getProfileKHDN>";

    public final static String CREATE_ORDER = "<ser:createOrder>\n" +
            "         <orderType>{0}</orderType>\n" +
            "         <data>{1}</data>\n" +
            "      </ser:createOrder>";

    private static final String VALIDATE_DATA_ORDER = "<ser:validateDataOrder>\n" +
            "         <orderType>{0}</orderType>\n" +
            "         <data>{1}</data>\n" +
            "      </ser:validateDataOrder>";

    public final static String GET_PROFILE_XNDLKH =
            " <ser:getProfileXNDLKH>\n" +
            "   <data>\n" +
            "       {0}" +
            "   </data>\n" +
            " </ser:getProfileXNDLKH>";

    //getFileContractToView
    public final static String GET_FILE_CONTRACT_TO_VIEW = "<ser:getFileContractToView>\n" +
            "\t<orderType>{0}</orderType>\n" +
            "\t<data>{1}</data>\n" +
            "</ser:getFileContractToView>";
    private static PartnerLicenseKeyService partnerLicenseKeyService;

    public static String getProfileKHDNRequest(String data, String username, String password){
        return MessageFormat.format(ORDER_REQUEST_TEMPLATE_V2,username, password, MessageFormat.format(GET_PROFILE_KHDN, StringEscapeUtils.escapeXml(data)));
    }

    public static String createOrder(String orderType, String data, String username, String password){
        return MessageFormat.format(ORDER_REQUEST_TEMPLATE_V2,username, password, MessageFormat.format(CREATE_ORDER, orderType, data));
    }

    private static String getOrderRequest(String username, String password, String body) {
        return MessageFormat.format(ORDER_REQUEST_TEMPLATE, username, password, body);
    }

    public static String getPlaceOrderPayload(String type, String dataJson, String username, String password) {
        String body = MessageFormat.format(PLACE_ORDER_BODY_TEMPLATE, type, dataJson);
        return getOrderRequest(username, password, body);
    }

    public static String getSearchOrderStatePayload(List<String> orderCodeList, String username, String password) {
        SearchOrderStateRequest request = new SearchOrderStateRequest();
        request.setOrderCodeList(orderCodeList);
        String bodyReq = MarshallerFactory.convertObjectToXML(request, SearchOrderStateRequest.class);
        String bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<searchOrderStateRequest>", "</searchOrderStateRequest>");
        String body = MessageFormat.format(SEARCH_ORDER_STATE_TEMPLATE, bodyDataValue);
        return getOrderRequest(username, password, body);
    }

    public static String getPricingProduct(PricingProductRequest request, String username, String password) {
        String bodyReq = MarshallerFactory.convertObjectToXML(request, PricingProductRequest.class);
        String bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<pricingProductRequest>", "</pricingProductRequest>");
        String body = MessageFormat.format(PRICING_PRODUCT, bodyDataValue);
        return getOrderRequest(username, password, body);
    }

    public static String getFileContractToView(String data, String orderType, String username, String password) {
        return MessageFormat.format(ORDER_REQUEST_TEMPLATE_V2, username, password, MessageFormat.format(GET_FILE_CONTRACT_TO_VIEW, orderType, StringEscapeUtils.escapeXml(data)));
    }

    public static PlacePaidOrderData mapDataOrderBccs(CreateOrderPaidRequest request, String systemUser, String userId) {
        String idTypeCust = "";
        String customerDocument = request.getCustomerDocument();
        if (customerDocument.contains("CMT")) {
            idTypeCust = "ID";
        } else if (customerDocument.contains("CCCD")) {
            idTypeCust = "IDC";
        } else {
            idTypeCust = "PASS";
        }
        String national = request.getNational();
        String custTypeRepresentative = "VIE";
        if (!DataUtil.isNullOrEmpty(national) && !national.contains("Việt Nam"))  {
            custTypeRepresentative = "FOR";
        }
        CustomerPaidDTO representativeCust = new CustomerPaidDTO();
        representativeCust.setName(request.getCustomerName());
        representativeCust.setBirthDate(request.getBirthday());
        representativeCust.setAreaCode(request.getCustomerProvince() + request.getCustomerDistrict() + request.getCustomerPrecinct());
        representativeCust.setProvince(request.getCustomerProvince());
        representativeCust.setDistrict(request.getCustomerDistrict());
        representativeCust.setPrecinct(request.getCustomerPrecinct());
        representativeCust.setAddress(request.getCustomerDetailAddress());
        representativeCust.setTelMobile(request.getCustomerPhone());
        representativeCust.setContactEmail(request.getCustomerEmail());
        representativeCust.setCustType(custTypeRepresentative);
        representativeCust.setListCustIdentity(Collections.singletonList(new CustIdentity(null, request.getCustomerIdNo(), idTypeCust, request.getCustomerIssuedBy(), request.getCustomerIssueDate(), request.getExpiry()) ));
        String recipientInfoPosition = DataUtil.safeTrim(request.getCustInfoPosition());
        representativeCust.setPosition(recipientInfoPosition);
        representativeCust.setContactTitle(recipientInfoPosition);
        representativeCust.setGroupType(GROUP_TYPE_ORGANIZATION);

        CustomerPaidDTO receiverCust = new CustomerPaidDTO();
        String telecomServiceAlias = DataUtil.safeTrim(request.getTelecomServiceAlias());
        RecipientInfoDTO recipientInfoDTO = request.getRecipientInfoDTO();
        List<ServiceItemDTO> lstTelecomService = request.getLstTelecomService();
        List<String> lstTelecomServiceAlias = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(lstTelecomService)) {
            lstTelecomServiceAlias = lstTelecomService.stream().map(ServiceItemDTO::getTelecomServiceAlias).collect(Collectors.toList());
        }
        if (!DataUtil.safeEqual(request.getIsBusinessAuth(), true) && (Constants.TelecomServiceAlias.SINVOICE.equals(telecomServiceAlias) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.SINVOICE) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.OCA))) {
            //dich vu sinvoice lay tu thong tin nguoi nhan ban giao
            BeanUtils.copyProperties(recipientInfoDTO, receiverCust);
            receiverCust.setTelMobile(recipientInfoDTO.getPhone());
            receiverCust.setContactEmail(recipientInfoDTO.getEmail());
            receiverCust.setPosition(recipientInfoDTO.getPosition());
            receiverCust.setContactTitle(recipientInfoDTO.getContactTitle());
            CustIdentity custIdentity = new CustIdentity();
            custIdentity.setIdNo(recipientInfoDTO.getIdNo());
            custIdentity.setIdType(convertIdType(recipientInfoDTO.getIdType()));
            custIdentity.setIdIssueDate(recipientInfoDTO.getIssueDate());
            custIdentity.setIdIssuePlace(recipientInfoDTO.getIssueBy());
            receiverCust.setListCustIdentity(Collections.singletonList(custIdentity));
            receiverCust.setCustType(custTypeRepresentative);
            receiverCust.setProvince(recipientInfoDTO.getAddressDTO().getProvince());
            receiverCust.setDistrict(recipientInfoDTO.getAddressDTO().getDistrict());
            receiverCust.setPrecinct(recipientInfoDTO.getAddressDTO().getPrecinct());
            receiverCust.setAddress(recipientInfoDTO.getAddressDTO().getAddress());
            receiverCust.setAddressDTO(recipientInfoDTO.getAddressDTO());
        } else {
            BeanUtils.copyProperties(representativeCust, receiverCust);
        }

        List<CustIdentity> listCompanyIdentity = new ArrayList<>();
        listCompanyIdentity.add(new CustIdentity(null, request.getBusinessLicense(), Constants.CustIndentityType.BUS, request.getCompanyIssueBy(), request.getCompanyIssueDate(), null));
        listCompanyIdentity.add(new CustIdentity(null, request.getTaxCode(), Constants.CustIndentityType.TIN, request.getCompanyIssueBy(), request.getCompanyIssueDate(), null));
        CustomerPaidDTO customerPaidDTO = new CustomerPaidDTO();
        customerPaidDTO.setGroupType("2");
        customerPaidDTO.setName(request.getCompanyName());
        customerPaidDTO.setBirthDate(request.getFoundingDate());
        customerPaidDTO.setAreaCode(request.getCompanyProvince() + request.getCompanyDistrict() + request.getCompanyPrecinct());
        customerPaidDTO.setProvince(request.getCompanyProvince());
        customerPaidDTO.setDistrict(request.getCompanyDistrict());
        customerPaidDTO.setPrecinct(request.getCompanyPrecinct());
        customerPaidDTO.setAddress(request.getCompanyDetailAddress());
        customerPaidDTO.setTelMobile(request.getCompanyPhone());
        customerPaidDTO.setContactEmail(request.getCompanyEmail());
        customerPaidDTO.setListCustIdentity(listCompanyIdentity);
        customerPaidDTO.setRepresentativeCust(representativeCust);
        // bo sung luong xac thuc doanh nghiep
        if (DataUtil.safeEqual(request.getIsBusinessAuth(), true)) {
            customerPaidDTO.setReceiverCust(representativeCust);
            customerPaidDTO.setUserInfoDTO(representativeCust);
        } else {
            customerPaidDTO.setReceiverCust(receiverCust);
        }
        customerPaidDTO.setPortalAccountId(userId);
        customerPaidDTO.setCustType(request.getTypeCustomer());
        customerPaidDTO.setSystemType(Constants.SystemType.SME_HUB);
        customerPaidDTO.setIssueCompany(request.getIssueCompany());

        PayInfoPaidOrderDTO payInfo = new PayInfoPaidOrderDTO();
        payInfo.setImmediatePay(true);
        payInfo.setCardRecords(new ArrayList<>());
        payInfo.setPayMethod("CTT");

        PlacePaidOrderData placeOrderData = new PlacePaidOrderData();
        placeOrderData.setCustomer(customerPaidDTO);
        placeOrderData.setCustomerFile(request.getCustomerFile());
        placeOrderData.setSystemType(Constants.SystemType.SME_HUB);
        placeOrderData.setCustAcceptAIResult(request.getCustAcceptAIResult());
        placeOrderData.setCheckByAI(request.getCheckByAI());
        placeOrderData.setSystemStaffCode(systemUser);
        placeOrderData.setStaffRevenue(systemUser);
        placeOrderData.setTotalFee(request.getTotalFee());
        placeOrderData.setPayInfo(payInfo);
        placeOrderData.setNeedSignCAContract(true);
        placeOrderData.setNeedAssignStaff(false);
        placeOrderData.setNeedFillInfo(true);
        placeOrderData.setRecipientName(representativeCust.getName());
        placeOrderData.setRecipientPhone(representativeCust.getTelMobile());
        placeOrderData.setTransactionPlace(Constants.TransactionPlace.HOME);
        placeOrderData.setPayStatus(1L);
        placeOrderData.setSelfCare(true);
        if (request.getInvoiceInfoDTO() != null) {
            //set thong tin xuat hoa don
            placeOrderData.setInvoiceInfo(request.getInvoiceInfoDTO());
        }
        List<ProductTemplateCharacteristicDTO> productCharacteristic = new ArrayList<>();
        String subscriberEmail = DataUtil.safeToString(request.getSubscriberEmail());
        //email nhan tai khoan
        if (!DataUtil.isNullOrEmpty(subscriberEmail)) {
            productCharacteristic.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.subscriber.email"), Constants.CharacteristicKey.EMAIL_ACCOUNT, subscriberEmail, Constants.DataType.STRING, null));
        }
        String circularsType = DataUtil.safeToString(request.getCircularsType());
        //truyen them loai thong tu
        if (!DataUtil.isNullOrEmpty(circularsType)) {
            productCharacteristic.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.type.circulars"), Constants.CharacteristicKey.TYPE_CIRCULARS, circularsType, Constants.DataType.STRING, null));
        }
        String contactEmail = DataUtil.safeToString(request.getContactEmail());
        //truyen them thong tin email nguoi dai dien
        if (!DataUtil.isNullOrEmpty(contactEmail)) {
            productCharacteristic.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.contact.email"), Constants.CharacteristicKey.CONTACT_EMAIL, contactEmail, Constants.DataType.STRING, null));
        }
        String contactPhone = DataUtil.safeToString(request.getContactPhone());
        //truyen them thong tin email nguoi dai dien
        if (!DataUtil.isNullOrEmpty(contactEmail)) {
            productCharacteristic.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.contact.phone"), Constants.CharacteristicKey.CONTACT_PHONE, contactPhone, Constants.DataType.STRING, null));
        }
        Long telecomServiceId = request.getServiceId();

        String fromStaff = DataUtil.safeToString(request.getFromStaff());
        //truyen them nhan vien moi gioi
        if (!DataUtil.isNullOrEmpty(fromStaff)) {
            productCharacteristic.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.from.staff"), Constants.CharacteristicKey.FROM_STAFF, fromStaff, Constants.DataType.STRING, null));
        }
        String amStaffSupport = DataUtil.safeToString(request.getAmStaffSupport());
        //truyen them nhan vien am ho tro
        if (!DataUtil.isNullOrEmpty(amStaffSupport)) {
            productCharacteristic.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.am.staff.support"), Constants.CharacteristicKey.STAFF_AM_SUPPORT, amStaffSupport, Constants.DataType.STRING, null));
        }

        boolean isCombo = request.getIsCombo() != null ? request.getIsCombo() : false;
        List<ProductTemplateCharacteristicDTO> lstProductTemplateCharacteristicCA = new ArrayList<>(9);
        List<ProductTemplateCharacteristicDTO> lstProductTemplateCharacteristicSinvoice = new ArrayList<>(9);
        List<ProductTemplateCharacteristicDTO> lstProductTemplateCharacteristicVContract = new ArrayList<>(9);
        List<ProductTemplateCharacteristicDTO> lstProductTemplateCharacteristicVBhxh = new ArrayList<>(9);
        List<ProductTemplateCharacteristicDTO> lstProductTemplateCharacteristicESB = new ArrayList<>(9);
        lstProductTemplateCharacteristicCA.addAll(productCharacteristic);
        lstProductTemplateCharacteristicSinvoice.addAll(productCharacteristic);
        lstProductTemplateCharacteristicVContract.addAll(productCharacteristic);
        lstProductTemplateCharacteristicVBhxh.addAll(productCharacteristic);
        lstProductTemplateCharacteristicESB.addAll(productCharacteristic);

        if (!DataUtil.isNullOrEmpty(request.getLstLicenseKey())) {
            if (isCombo) {
                for (PartnerLicenseKeyDTO partnerLicenseKeyDTO : request.getLstLicenseKey()) {
                    if (!DataUtil.isNullOrEmpty(partnerLicenseKeyDTO.getLicenceKey())) {
                        if (Constants.TelecomServiceId.CA.equals(partnerLicenseKeyDTO.getTelecomServiceId())) {
                            lstProductTemplateCharacteristicCA.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.license.key"), Constants.CharacteristicKey.LICENSE_KEY, partnerLicenseKeyDTO.getLicenceKey(), Constants.DataType.STRING, null));
                        }
                        if (Constants.TelecomServiceId.SINVOICE.equals(partnerLicenseKeyDTO.getTelecomServiceId())) {
                            lstProductTemplateCharacteristicSinvoice.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.license.key"), Constants.CharacteristicKey.LICENSE_KEY, partnerLicenseKeyDTO.getLicenceKey(), Constants.DataType.STRING, null));
                        }
                        if (Constants.TelecomServiceId.VCONTRACT.equals(partnerLicenseKeyDTO.getTelecomServiceId())) {
                            lstProductTemplateCharacteristicVContract.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.license.key"), Constants.CharacteristicKey.LICENSE_KEY, partnerLicenseKeyDTO.getLicenceKey(), Constants.DataType.STRING, null));
                        }
                        if (Constants.TelecomServiceId.VBHXH.equals(partnerLicenseKeyDTO.getTelecomServiceId())) {
                            lstProductTemplateCharacteristicVBhxh.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.license.key"), Constants.CharacteristicKey.LICENSE_KEY, partnerLicenseKeyDTO.getLicenceKey(), Constants.DataType.STRING, null));
                        }
                        if (Constants.TelecomServiceId.EASY_BOOK.equals(partnerLicenseKeyDTO.getTelecomServiceId())) {
                            lstProductTemplateCharacteristicESB.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.license.key"), Constants.CharacteristicKey.LICENSE_KEY, partnerLicenseKeyDTO.getLicenceKey(), Constants.DataType.STRING, null));
                        }
                    }
                }
            } else {
                String licenseKey = request.getLstLicenseKey().get(0).getLicenceKey();
                if (!DataUtil.isNullOrEmpty(licenseKey)) {
                    productCharacteristic.add(new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.license.key"), Constants.CharacteristicKey.LICENSE_KEY, licenseKey, Constants.DataType.STRING, null));
                }
            }
        }

        // bo sung tham so chinh sach xu ly va bao ve du lieu
        if (!DataUtil.isNullOrEmpty(request.getDataPolicy())) {
            for (Map.Entry<String, String> dataPolicy : request.getDataPolicy().entrySet()) {
                productCharacteristic.add(new ProductTemplateCharacteristicDTO (null, dataPolicy.getKey(), dataPolicy.getValue(), Constants.DataType.STRING, null));
            }
        }
        if (Constants.TelecomServiceId.CA.equals(telecomServiceId) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.CA) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.OCA)) {
            //thong tin chuc vu
            String custInfoPosition = DataUtil.safeTrim(request.getCustInfoPosition());
            if (!DataUtil.isNullOrEmpty(custInfoPosition) && !DataUtil.safeEqual(request.getIsBusinessAuth(), true)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO (null, Constants.CharacteristicKey.CUS_INFO_POSITION, custInfoPosition, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicCA.add(productTemplateCharacteristicDTO);
            }
        }
        //thong tin ten nguoi lien he
        if (Constants.TelecomServiceAlias.CA.equals(telecomServiceAlias) || Constants.TelecomServiceAlias.EASY_BOOK.equals(telecomServiceAlias) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.CA) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.OCA) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.EASY_BOOK)) {
            String contactName = DataUtil.safeTrim(request.getContactName());
            if (!DataUtil.isNullOrEmpty(contactName)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO (null, Constants.CharacteristicKey.CONTACT_NAME, contactName, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicCA.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicESB.add(productTemplateCharacteristicDTO);
            }
        }

        //set thong tin characteristic dich vu SINVOICE
        if (Constants.TelecomServiceAlias.SINVOICE.equals(telecomServiceAlias) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.SINVOICE)) {
            String email = DataUtil.safeTrim(request.getCompanyEmail());
            if (!DataUtil.isNullOrEmpty(email)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO (null, Constants.CharacteristicKey.EMAIL, email, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicSinvoice.add(productTemplateCharacteristicDTO);

            }
            String phone = DataUtil.safeTrim(recipientInfoDTO.getPhone());
            if (!DataUtil.isNullOrEmpty(phone)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO (null, Constants.CharacteristicKey.CLUE_ISDN, phone, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicSinvoice.add(productTemplateCharacteristicDTO);
            }
            String contactName = null;
            String position = null;
            if (isCombo) {
                //neu la combo
                contactName = DataUtil.safeTrim(request.getContactName()); //lay thong tin tu nguoi lien he
                position = DataUtil.safeTrim(request.getCustInfoPosition()); //lay thong tin chuc vu cua nguoi dai dien
            } else {
                //neu la don le
                contactName = DataUtil.safeTrim(recipientInfoDTO.getName()); //lay ten nguoi ban giao
                position = DataUtil.safeTrim(recipientInfoDTO.getPosition()); //chuc vu nguoi ban giao
            }
            if (!DataUtil.isNullOrEmpty(contactName)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO (null, Constants.CharacteristicKey.CONTACT_NAME, contactName, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicSinvoice.add(productTemplateCharacteristicDTO);
            }
            if (!DataUtil.isNullOrEmpty(position)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO (null, Constants.CharacteristicKey.CUS_INFO_POSITION, position, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicSinvoice.add(productTemplateCharacteristicDTO);
            }
        }
        //set thong tin position
        if (Constants.TelecomServiceId.EASY_BOOK.equals(telecomServiceId) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.EASY_BOOK)) {
            customerPaidDTO.setPosition(request.getCustInfoPosition());
            String hashPwd = DataUtil.safeToString(request.getHashPwd());
            //truyen them pw dang nhap
            if (!DataUtil.isNullOrEmpty(hashPwd)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.hash.pws"), Constants.CharacteristicKey.ACCOUNT_PWD, hashPwd, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicESB.add(productTemplateCharacteristicDTO);
            }
            String userName = DataUtil.safeToString(request.getUserName());
            //truyen them thong tin user dang nhap
            if (!DataUtil.isNullOrEmpty(userName)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO(Translator.toLocaleVi("order.use.name"), Constants.CharacteristicKey.ACCOUNT_HUB, userName, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicESB.add(productTemplateCharacteristicDTO);
            }
        }

        if (Constants.TelecomServiceAlias.VCONTRACT.equals(telecomServiceAlias) || lstTelecomServiceAlias.contains(Constants.TelecomServiceAlias.VCONTRACT)) {
            String email = DataUtil.safeTrim(request.getCompanyEmail());
            if (!DataUtil.isNullOrEmpty(email)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO (null, Constants.CharacteristicKey.DVM_CUST_EMAIL, email, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicVContract.add(productTemplateCharacteristicDTO);

            }
            String phone = DataUtil.safeTrim(request.getCompanyPhone());
            if (!DataUtil.isNullOrEmpty(phone)) {
                ProductTemplateCharacteristicDTO productTemplateCharacteristicDTO = new ProductTemplateCharacteristicDTO (null, Constants.CharacteristicKey.DVM_CUST_TEL, phone, Constants.DataType.STRING, null);
                productCharacteristic.add(productTemplateCharacteristicDTO);
                lstProductTemplateCharacteristicVContract.add(productTemplateCharacteristicDTO);
            }
        }
        //truyen co len cong no AM sang Order
        Boolean revenueForAm = request.getRevenueForAm() != null ? request.getRevenueForAm() : false;
        placeOrderData.setRevenueForAm(revenueForAm);
        if (revenueForAm) {
            //neu co len cong no thi set thong tin nhan vien len hoa don = nv Am support
            placeOrderData.setStaffRevenue(amStaffSupport);
            //khong truyen payInfo
            placeOrderData.setPayInfo(null);
            placeOrderData.setPayStatus(0L);
            placeOrderData.setNeedSignCAContract(false);
            //neu truyen ho so dien tu
            Boolean isElectronicRecords = request.getIsElectronicRecords() != null ? request.getIsElectronicRecords() : false;
            if (isElectronicRecords) {
                placeOrderData.setNeedSignCAContract(true);
            }
        }
        if (isCombo) {
            List<ServiceItemPaidDTO> lstServiceItemDTO = new ArrayList<>();
            for (ServiceItemDTO telecomService : lstTelecomService) {
                Long serviceId = telecomService.getTelecomServiceId();
                //neu trung telecomsServiceId thi khong add them
                if (!DataUtil.isNullOrEmpty(lstServiceItemDTO)) {
                    List<Long> lstTelecomServiceId  = lstServiceItemDTO.stream().map(ServiceItemDTO::getTelecomServiceId).collect(Collectors.toList());
                    if (!DataUtil.isNullOrEmpty(lstTelecomServiceId) && lstTelecomServiceId.contains(serviceId)) {
                        continue;
                    }
                }
                ServiceItemPaidDTO serviceItemPaidDTO = new ServiceItemPaidDTO();
                serviceItemPaidDTO.setTelecomServiceId(serviceId);
                serviceItemPaidDTO.setTelecomServiceAlias(telecomService.getTelecomServiceAlias());
                serviceItemPaidDTO.setName(telecomService.getName());
                //set thong tin ho so dich vu
                List<OrderFileDTO> lstServiceFile = request.getServiceFile();
                if (!DataUtil.isNullOrEmpty(lstServiceFile)) {
                    //lay thong tin danh sach ho so dich vu theo telecomServiceId
                    List<OrderFileDTO> lstServiceFileById = lstServiceFile.stream().filter(x -> DataUtil.safeEqual(serviceId, x.getTelecomServiceId())).collect(Collectors.toList());
                    serviceItemPaidDTO.setUploadFile(lstServiceFileById);
                }
                lstServiceItemDTO.add(serviceItemPaidDTO);
            }
            placeOrderData.setServiceItem(lstServiceItemDTO);

            //lay ra danh sach cac goi chinh
            List<ProductOrderItemWsDTO> lstProductOrderItem = request.getProductOrderItem();

            if (!DataUtil.isNullOrEmpty(lstProductOrderItem)) {
                List<ProductOrderItemWsDTO> lstMainOffer = lstProductOrderItem.stream().filter(item -> DataUtil.isNullOrEmpty(item.getProduct().getLstProductRelationship())).collect(Collectors.toList());
                if (!DataUtil.isNullOrEmpty(lstMainOffer)) {
                    for (ProductOrderItemWsDTO productOrderItemWsDTO : lstMainOffer) {
                        Long id = productOrderItemWsDTO.getProductOffering().getTelecomServiceId();
                        String offeringCode = productOrderItemWsDTO.getProductOffering().getCode();
                        //set thong tin ho so thue bao
                        List<OrderFileDTO> lstSubscriberFile = request.getSubscriberFile();
                        if (!DataUtil.isNullOrEmpty(lstSubscriberFile)) {
                            //lay thong tin danh sach ho so thue bao theo ma goi cuoc
                            List<OrderFileDTO> lstSubscriberFileById = lstSubscriberFile.stream().filter(x -> DataUtil.safeEqual(offeringCode, x.getProductOfferingCode())).collect(Collectors.toList());
                            productOrderItemWsDTO.setFileUpload(lstSubscriberFileById);
                        }
                        if (Constants.TelecomServiceId.CA.equals(id)) {
                            productOrderItemWsDTO.getProduct().getProductCharacteristic().addAll(lstProductTemplateCharacteristicCA);
                        } else if (Constants.TelecomServiceId.SINVOICE.equals(id)) {
                            productOrderItemWsDTO.getProduct().getProductCharacteristic().addAll(lstProductTemplateCharacteristicSinvoice);
                        } else if (Constants.TelecomServiceId.VCONTRACT.equals(id)) {
                            productOrderItemWsDTO.getProduct().getProductCharacteristic().addAll(lstProductTemplateCharacteristicVContract);
                        } else if (Constants.TelecomServiceId.VBHXH.equals(id)) {
                            productOrderItemWsDTO.getProduct().getProductCharacteristic().addAll(lstProductTemplateCharacteristicVBhxh);
                        } else if (Constants.TelecomServiceId.EASY_BOOK.equals(id)) {
                            productOrderItemWsDTO.getProduct().getProductCharacteristic().addAll(lstProductTemplateCharacteristicESB);
                        } else {
                            break;
                        }
                    }
                }
            }

            placeOrderData.setProductOrderItem(request.getProductOrderItem());
            if (DataUtil.safeEqual(request.getIsBusinessAuth(), true)) {
                placeOrderData.setNeedCompleteProfile(true);
                placeOrderData.setNeedSignCAContract(false);
            }
            return placeOrderData;
        }

        ServiceItemPaidDTO serviceItemPaidDTO = new ServiceItemPaidDTO();
        serviceItemPaidDTO.setTelecomServiceId(telecomServiceId);
        serviceItemPaidDTO.setTelecomServiceAlias(request.getServiceAlias()); // bo sung alias cho PYCXXX/LuongToanTrinhScontract Order-006
        serviceItemPaidDTO.setName(request.getServiceName());

        ProductTemplateDTO productTemplateDTO = new ProductTemplateDTO();
        productTemplateDTO.setTemplateCode(request.getTemplateCode());
        productTemplateDTO.setProductCharacteristic(productCharacteristic);
        //set thong tin ho so thue bao
        List<OrderFileDTO> lstSubscriberFile = request.getSubscriberFile();
        if (!DataUtil.isNullOrEmpty(lstSubscriberFile)) {
            productTemplateDTO.setFileUpload(lstSubscriberFile);
        }
        //set thong tin ho so dich vu
        List<OrderFileDTO> lstServiceFile = request.getServiceFile();
        if (!DataUtil.isNullOrEmpty(lstServiceFile)) {
            //lay thong tin danh sach ho so dich vu theo telecomServiceId
            serviceItemPaidDTO.setUploadFile(lstServiceFile);
        }
        placeOrderData.setServiceItem(Collections.singletonList(serviceItemPaidDTO));
        placeOrderData.setLstProductTemplate(Collections.singletonList(productTemplateDTO));

        return placeOrderData;
    }

    public static String getValidateDataOrderRequest(String orderType, String data, String username, String password) {
        String body = MessageFormat.format(VALIDATE_DATA_ORDER, orderType, data);
        return getOrderRequest(username, password, body);
    }

    public static String convertIdType(String type){
        if (DataUtil.isNullOrEmpty(type)) {
            return null;
        }
        String idType;
        if (type.contains("CMT")) {
            idType = "ID";
        } else if (type.contains("CCCD")) {
            idType = "IDC";
        } else {
            idType = "PASS";
        }
        return idType;
    }

    public static String getProfileXNDLKHRequest(String data, String username, String password){
        return MessageFormat.format(ORDER_REQUEST_TEMPLATE_V2,username, password, MessageFormat.format(GET_PROFILE_XNDLKH, StringEscapeUtils.escapeXml(data)));
    }
}
