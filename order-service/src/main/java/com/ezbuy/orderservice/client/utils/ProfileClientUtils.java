package com.ezbuy.orderservice.client.utils;

import com.ezbuy.ordermodel.dto.request.GetListProductOfferingRecordRequest;
import com.ezbuy.ordermodel.dto.request.GetListServiceRecordRequest;
import com.ezbuy.ordermodel.dto.ws.lstServiceDTO;
import com.reactify.util.DataUtil;
import java.text.MessageFormat;

public class ProfileClientUtils {
    private static final String PROFILE_CLIENT_REQUEST =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:proc=\"http://process.wsim.viettel.com/\">\n"
                    + "<soapenv:Header/>\n" + "   <soapenv:Body>\n" + "{0}" + "   </soapenv:Body>\n"
                    + "</soapenv:Envelope>";
    private static final String GET_LIST_SERVICE_REQUEST_TEMPLATE = "<proc:getListServiceRecord>"
            + "<username>{0}</username>\n" + "<password>{1}</password>\n" + "<actionCode>{2}</actionCode>\n" + "{3}"
            + "</proc:getListServiceRecord>\n";

    private static final String GET_LIST_PRODUCT_OFFERING_RECORD_REQUEST_TEMPLATE =
            "<proc:getListProductOfferingRecord>"
                    + "<username>{0}</username>\n" + "<password>{1}</password>\n" + "<actionCode>{2}</actionCode>\n"
                    + "{3}"
                    + "</proc:getListProductOfferingRecord>\n";

    public static String getListServiceRecord(GetListServiceRecordRequest request, String username, String password) {
        StringBuilder lstService = new StringBuilder();
        for (lstServiceDTO item : request.getLstService()) {
            lstService.append("<lstService>\n");
            lstService.append(DataUtil.convertObjectToXMLString(item, "lstService"));
            lstService.append("</lstService>\n");
        }

        String body = MessageFormat.format(
                GET_LIST_SERVICE_REQUEST_TEMPLATE, username, password, request.getActionCode(), lstService.toString());
        return MessageFormat.format(PROFILE_CLIENT_REQUEST, body);
    }

    public static String getListProductOfferingRecord(
            GetListProductOfferingRecordRequest request, String username, String password) {
        StringBuilder lstProductOffering = new StringBuilder();
        for (String item : request.getLstProductOffering()) {
            lstProductOffering.append("<lstProductOffering>");
            lstProductOffering.append(item);
            lstProductOffering.append("</lstProductOffering>\n");
        }

        String body = MessageFormat.format(
                GET_LIST_PRODUCT_OFFERING_RECORD_REQUEST_TEMPLATE,
                username,
                password,
                request.getActionCode(),
                lstProductOffering.toString());
        return MessageFormat.format(PROFILE_CLIENT_REQUEST, body);
    }
}
