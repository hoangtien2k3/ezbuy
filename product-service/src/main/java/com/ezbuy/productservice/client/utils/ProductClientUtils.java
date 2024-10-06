package com.ezbuy.productservice.client.utils;

import com.ezbuy.productmodel.constants.Constants;
import com.ezbuy.productmodel.request.*;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.DataWsUtil;
import io.hoangtien2k3.reactify.factory.MarshallerFactory;

import java.text.MessageFormat;
import java.util.List;

public class ProductClientUtils {

    private final static String PRODUCT_REQUEST_TEMPLATE = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.product.bccs.viettel.com/\">\n" +
            "   <soapenv:Header>\n" +
            "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
            "         <wsse:UsernameToken wsu:Id=\"UsernameToken-32ce50e8-4a5d-4040-af71-c3428d92daa7\">\n" +
            "            <wsse:Username>{0}</wsse:Username>\n" +
            "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n" +
            "         </wsse:UsernameToken>\n" +
            "      </wsse:Security>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "       {2}\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    private static final String GET_PRODUCT_TEMPLATE_BY_IDS =
            "<ser:getListProductOfferTemplateByListIds>\n" +
                "   {0}\n" +
                "   <inputConfigPhase>{1}</inputConfigPhase>\n" +
            "</ser:getListProductOfferTemplateByListIds>";

    private static final String GET_PRODUCT_TEMPLATE = "<ser:getLstTemplateOffer>\n" +
            "{0}\n" +
            "</ser:getLstTemplateOffer>";

    private static final String GET_PRODUCT_OFFERING = "<ser:getProductOfferingSpecificationForMbccs>\n" +
            "{0}\n" +
            "</ser:getProductOfferingSpecificationForMbccs>";

    private static final String GET_PRODUCT_FILTER = "<ser:getLstServiceCharacteristic>\n" +
            "{0}\n" +
            "</ser:getLstServiceCharacteristic>";

    public static final String CA_PREPAID_DURATION = "PREPAID_DURATION";
    public static final String CA_PREPAID_DURATION_DAY = "PREPAID_DURATION_DAY";

    public static final String GET_LIST_PRODUCT_OFFERING_COMBO_FOR_HUB_SME = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.product.bccs.viettel.com/\">\n" +
            "   <soapenv:Header>\n" +
            "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
            "         <wsse:UsernameToken wsu:Id=\"UsernameToken-32ce50e8-4a5d-4040-af71-c3428d92daa7\">\n" +
            "            <wsse:Username>{0}</wsse:Username>\n" +
            "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n" +
            "         </wsse:UsernameToken>\n" +
            "      </wsse:Security>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "       <ser:getListProductOfferingComboForHubSme>\n" +
            "           {2} \n" +
            "       </ser:getListProductOfferingComboForHubSme>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";


    public static final String GET_LIST_TEMPLATE_COMBO_FOR_HUB_SME = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.product.bccs.viettel.com/\">\n" +
            "   <soapenv:Header>\n" +
            "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
            "         <wsse:UsernameToken wsu:Id=\"UsernameToken-32ce50e8-4a5d-4040-af71-c3428d92daa7\">\n" +
            "            <wsse:Username>{0}</wsse:Username>\n" +
            "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n" +
            "         </wsse:UsernameToken>\n" +
            "      </wsse:Security>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "{2} \n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    private static final String GET_LIST_TEMPLATE_COMBO_FOR_HUB_SME_BODY = "<ser:getListTemplateComboForHubSme>\n" +
            "         {0}\n" +
            "      </ser:getListTemplateComboForHubSme>";

    private static final String GET_PRODUCT_TEMPLATE_DETAIL =
            "<ser:getProductTemplateDetail>\n" +
            "   {0}\n" +
            "   <inputConfigPhase>{1}</inputConfigPhase>\n" +
            "</ser:getProductTemplateDetail>";

    public static final String INPUT_CONFIG_PHASE = "SHOPPING_PHASE";

    private static String getProductRequest(String username, String password, String body) {
        return MessageFormat.format(PRODUCT_REQUEST_TEMPLATE, username, password, body);
    }

    public static String getSearchProductTemplateByIdsPayload(List<String> templateIds, String username, String password) {
        ProductTemplateByIdRequest request = new ProductTemplateByIdRequest();
        request.setTemplateIds(templateIds);

        String bodyReq = MarshallerFactory.convertObjectToXML(request, ProductTemplateByIdRequest.class);
        String bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<productTemplateByIdRequest>", "</productTemplateByIdRequest>");
        String body = MessageFormat.format(GET_PRODUCT_TEMPLATE_BY_IDS, bodyDataValue);
        return getProductRequest(username, password, body);
    }

    public static String getSearchProductTemplateByIdsPayloadV2(List<String> templateIds, String username, String password) {
        StringBuilder sb = new StringBuilder();
        if (!DataUtil.isNullOrEmpty(templateIds)) {
            for(String templateId: templateIds) {
                sb.append("<lstTemplateIds>")
                        .append(templateId)
                        .append("</lstTemplateIds>");
            }
        }

        String body = MessageFormat.format(GET_PRODUCT_TEMPLATE_BY_IDS, sb.toString(), INPUT_CONFIG_PHASE);
        return getProductRequest(username, password, body);
    }

    public static String getSearchProductTemplatePayload(String telecomServiceId, ApiUtils utils, String username, String password, List<String> priceTypes) {
        GetLstTemplateOfferRequest request = new GetLstTemplateOfferRequest();
        request.setTelecomServiceId(telecomServiceId);
        request.setUtils(utils);
        request.setPriceTypes(priceTypes);

        String bodyReq = MarshallerFactory.convertObjectToXML(request, GetLstTemplateOfferRequest.class);
        String bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<getLstTemplateOfferRequest>", "</getLstTemplateOfferRequest>");
        String body = MessageFormat.format(GET_PRODUCT_TEMPLATE, bodyDataValue);
        return getProductRequest(username, password, body);
    }

    public static String getLstProductSpec(List<String> telecomServiceIds, String username, String password) {
        GetLstFilterRequest request = new GetLstFilterRequest();
        request.setTelecomServiceId(telecomServiceIds);
        String bodyReq = MarshallerFactory.convertObjectToXML(request, GetLstFilterRequest.class);
        String bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<getLstFilterRequest>", "</getLstFilterRequest>");
        String body = MessageFormat.format(GET_PRODUCT_FILTER, bodyDataValue);
        return getProductRequest(username, password, body);
    }

    public static String getProductOffering(String productOfferingId, String staffCode, String username, String password) {
        GetProductOfferingRequest getProductOfferingRequest = new GetProductOfferingRequest();
        getProductOfferingRequest.setProductOfferingId(productOfferingId);
        getProductOfferingRequest.setStaffCode(staffCode);
        getProductOfferingRequest.setPhase(Constants.PHASE_EXTEND_PACKAGE);

        String bodyReq = MarshallerFactory.convertObjectToXML(getProductOfferingRequest, GetProductOfferingRequest.class);
        String bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<getProductOfferingRequest>", "</getProductOfferingRequest>");
        String body = MessageFormat.format(GET_PRODUCT_OFFERING, bodyDataValue);
        return getProductRequest(username, password, body);
    }

    public static String getListProductOfferingComboForHubSme(GetListProductOfferingComboForHubSmeRequest request, String username, String password) {
        String bodyReq = MarshallerFactory.convertObjectToXML(request, GetListProductOfferingComboForHubSmeRequest.class);
        String bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<getListProductOfferingComboForHubSmeRequest>", "</getListProductOfferingComboForHubSmeRequest>");
        return MessageFormat.format(GET_LIST_PRODUCT_OFFERING_COMBO_FOR_HUB_SME, username, password, bodyDataValue);
    }

    private static String getListTemplateComboForHubSme(String username, String password, String body) {
        return MessageFormat.format(GET_LIST_TEMPLATE_COMBO_FOR_HUB_SME, username, password, body);
    }

    public static String getListTemplateComboForHubSmeBody(String productOfferingId, String username, String password) {
        String bodyDataValue = null;
        if (!DataUtil.isNullOrEmpty(productOfferingId)) {
            GetListTemplateComboForHubSmeRequest request = new GetListTemplateComboForHubSmeRequest();
            request.setProductOfferingId(productOfferingId);
            String bodyReq = MarshallerFactory.convertObjectToXML(request, GetListTemplateComboForHubSmeRequest.class);
            bodyDataValue = DataWsUtil.getDataByTag(bodyReq, "<getListTemplateComboForHubSmeRequest>", "</getListTemplateComboForHubSmeRequest>");
        }
        String body = MessageFormat.format(GET_LIST_TEMPLATE_COMBO_FOR_HUB_SME_BODY, bodyDataValue);
        return getListTemplateComboForHubSme(username, password, body);
    }

    public static String getProductTemplateDetailPayload(GetProductTemplateDetailRequest getProductTemplateDetailRequest, String username, String password) {
        StringBuilder sb = new StringBuilder("");
        if (!DataUtil.isNullOrEmpty(getProductTemplateDetailRequest.getTemplateCodeList())) {
            for(String templateCode: getProductTemplateDetailRequest.getTemplateCodeList()) {
                sb.append("<templateCodes>")
                        .append(templateCode)
                        .append("</templateCodes>");
            }
        }
        String body = MessageFormat.format(GET_PRODUCT_TEMPLATE_DETAIL, sb.toString(), INPUT_CONFIG_PHASE);
        return getProductRequest(username, password, body);
    }
}
