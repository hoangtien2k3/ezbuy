package com.ezbuy.orderservice.client.utils;

import com.ezbuy.ordermodel.dto.request.GetGroupsCAinfoRequest;
import io.hoangtien2k3.reactify.DataUtil;
import java.text.MessageFormat;

public class CmClientUtils {

    private static final String CM_REQUEST_TEMPLATE =
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                    + "   <soap:Body>\n"
                    + "      <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ext=\"http://externalSystem.service.sale.bccs.viettel.com/\">\n"
                    + "         <soapenv:Header>\n"
                    + "            <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" \n"
                    + "            xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
                    + "               <wsse:UsernameToken wsu:Id=\"UsernameToken-32ce50e8-4a5d-4040-af71-c3428d92daa7\">\n"
                    + "                  <wsse:Username>{0}</wsse:Username>\n"
                    + "                  <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">{1}</wsse:Password>\n"
                    + "               </wsse:UsernameToken>\n" + "            </wsse:Security>\n"
                    + "         </soapenv:Header>\n" + "         <soapenv:Body>\n" + "{2}" + "    </soapenv:Body>\n"
                    + "      </soapenv:Envelope>\n" + "   </soap:Body>\n" + "</soap:Envelope>";

    private static final String GET_CUSTOMER_SUBSCRIBER_SME_INFO_TEMPLATE = "<ext:getCustomerSubscriberSmeInfo>\n"
            + "          <idNo>{0}</idNo>\n" + "          <isdn>{1}</isdn>\n"
            + "          <telecomServiceId>{2}</telecomServiceId>\n" + "      </ext:getCustomerSubscriberSmeInfo>";

    private static final String GET_LIST_SUBSCRIBER_BY_IDNO_TEMPLATE = "<ext:getListSubscriberByIdNo>\n"
            + "          <idNo>{0}</idNo>\n" + "          <telecomServiceId>{1}</telecomServiceId>\n"
            + "      </ext:getListSubscriberByIdNo>";

    private static String getCmRequestTemplate(String username, String password, String body) {
        return MessageFormat.format(CM_REQUEST_TEMPLATE, username, password, body);
    }

    public static String getGetListSubscriberByIdnoTemplate(
            String idNo, String telecomServiceId, String username, String password) {
        String body = MessageFormat.format(GET_LIST_SUBSCRIBER_BY_IDNO_TEMPLATE, idNo, telecomServiceId);
        return getCmRequestTemplate(username, password, body);
    }

    public static String getGetCustomerSubscriberSmeInfoTemplate(
            String idNo, String isdn, String telecomServiceId, String username, String password) {
        String body = MessageFormat.format(GET_CUSTOMER_SUBSCRIBER_SME_INFO_TEMPLATE, idNo, isdn, telecomServiceId);
        return getCmRequestTemplate(username, password, body);
    }

    public static String getGroupsInfo(GetGroupsCAinfoRequest request, String username, String password) {
        return "      <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ext=\"http://externalSystem.service.sale.bccs.viettel.com/\">\n"
                + "    <soapenv:Header>\n"
                + "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
                + "         <wsse:UsernameToken wsu:Id=\"UsernameToken-32ce50e8-4a5d-4040-af71-c3428d92daa7\">\n"
                + "            <wsse:Username>" + username + "</wsse:Username>\n"
                + "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">"
                + password + "</wsse:Password>\n" + "         </wsse:UsernameToken>\n" + "      </wsse:Security>\n"
                + "   </soapenv:Header> \n" + "   <soapenv:Body>\n" + "      <ext:getGroupsInfoForPortalV2> \n"
                + DataUtil.convertObjectToXMLString(request, "getGroupsCAinfoRequest")
                + "      </ext:getGroupsInfoForPortalV2>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>";
    }

    public static String getGroupsMemberInfo(GetGroupsCAinfoRequest request, String username, String password) {
        return "      <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ext=\"http://externalSystem.service.sale.bccs.viettel.com/\">\n"
                + "    <soapenv:Header>\n"
                + "      <wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
                + "         <wsse:UsernameToken wsu:Id=\"UsernameToken-32ce50e8-4a5d-4040-af71-c3428d92daa7\">\n"
                + "            <wsse:Username>" + username + "</wsse:Username>\n"
                + "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">"
                + password + "</wsse:Password>\n" + "         </wsse:UsernameToken>\n" + "      </wsse:Security>\n"
                + "   </soapenv:Header> \n" + "   <soapenv:Body>\n" + "      <ext:getListSubscriberByGroup> \n"
                + DataUtil.convertObjectToXMLString(request, "getGroupsCAinfoRequest")
                + "      </ext:getListSubscriberByGroup>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>";
    }
}
