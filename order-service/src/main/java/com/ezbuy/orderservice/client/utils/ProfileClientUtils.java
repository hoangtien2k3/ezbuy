package com.ezbuy.orderservice.client.utils;

import com.ezbuy.sme.authmodel.model.Message;
import com.ezbuy.sme.framework.utils.DataUtil;
import com.ezbuy.sme.ordermodel.dto.request.GetListProductOfferingRecordRequest;
import com.ezbuy.sme.ordermodel.dto.request.GetListServiceRecordRequest;
import com.ezbuy.sme.ordermodel.dto.ws.lstServiceDTO;

import java.text.MessageFormat;

public class ProfileClientUtils {
    private static final String PROFILE_CLIENT_REQUEST =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:proc=\"http://process.wsim.viettel.com/\">\n" +
                "<soapenv:Header/>\n"+
            "   <soapenv:Body>\n" +
                    "{0}"+
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";
    private static final String GET_LIST_SERVICE_REQUEST_TEMPLATE =
            "<proc:getListServiceRecord>" +
                    "<username>{0}</username>\n" +
                    "<password>{1}</password>\n" +
                    "<actionCode>{2}</actionCode>\n" +
                    "{3}" +
            "</proc:getListServiceRecord>\n";

    private static final String GET_LIST_PRODUCT_OFFERING_RECORD_REQUEST_TEMPLATE =
            "<proc:getListProductOfferingRecord>" +
                    "<username>{0}</username>\n" +
                    "<password>{1}</password>\n" +
                    "<actionCode>{2}</actionCode>\n" +
                    "{3}" +
                    "</proc:getListProductOfferingRecord>\n";


    public static String getListServiceRecord(GetListServiceRecordRequest request, String username, String password) {
        String lstService= new String("");
        for (lstServiceDTO item : request.getLstService()) {
            lstService += "<lstService>\n";
            lstService += DataUtil.ConvertObjectToXMLString(item, "lstService");
            lstService += "</lstService>\n";
        }

        String body=  MessageFormat.format(GET_LIST_SERVICE_REQUEST_TEMPLATE,
                username,
                password,
                request.getActionCode(),
                lstService
                );
        return MessageFormat.format(PROFILE_CLIENT_REQUEST, body);
    }

    public static String getListProductOfferingRecord(GetListProductOfferingRecordRequest request, String username, String password) {
        String lstProductOffering= new String("");
        for (String item : request.getLstProductOffering()) {
            lstProductOffering += "<lstProductOffering>";
            lstProductOffering += item;
            lstProductOffering += "</lstProductOffering>\n";
        }

        String body=  MessageFormat.format(GET_LIST_PRODUCT_OFFERING_RECORD_REQUEST_TEMPLATE,
                username,
                password,
                request.getActionCode(),
                lstProductOffering
        );
        return MessageFormat.format(PROFILE_CLIENT_REQUEST, body);
    }
}
