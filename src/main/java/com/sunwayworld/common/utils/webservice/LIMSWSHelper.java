package com.sunwayworld.common.utils.webservice;


import com.sunwayworld.common.dao.model.AppModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LIMSWSHelper {

    //命名空间
    public static final String NAMESPACE = "http://tempuri.org/";

    public static String runActionDirect(SoapObject soapObject, String methodName) {
        String result = null;
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;

        HttpTransportSE transport = new HttpTransportSE(AppModel.getSysConfig().getBaseurl());

        try {
            transport.call(methodName, envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object oriData = envelope.bodyIn;
        if (oriData instanceof SoapFault) {
            SoapFault fault = (SoapFault) envelope.bodyIn;
            result = fault.getMessage().toString();
        } else if (oriData instanceof SoapObject) {
            SoapObject object = (SoapObject) envelope.bodyIn;
            result = object.getProperty(0).toString();
        }

        if (result == null) {
            String errormsg = oriData == null ? "null" : oriData.getClass().getName();
            result = "原因未知[调试信息：使用SOAP调用接口返回值类型是<" + errormsg + ">]";
        }
        return result;
    }
}
