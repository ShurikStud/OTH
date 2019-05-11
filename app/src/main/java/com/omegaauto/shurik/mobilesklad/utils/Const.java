package com.omegaauto.shurik.mobilesklad.utils;

import com.omegaauto.shurik.mobilesklad.R;

public final class Const {

//    public static final int colorBackgroundVisible = 0xFF64FFDA;
    public static final int colorBackgroundVisible = R.drawable.shape_row_visible;
    public static final int colorBackgroundInvisible = 0xF0ab9d37;
    public static final int colorBackgroundSeparator = 0xF040364C;
    public static final int colorBackgroundShadow = 0xF06E6E6E;

    public static final int colorTextVisible = R.color.colorTextEnable;
    public static final int colorTextInvisible = R.color.colorTextDisable;


    //public static final String HTTP_SERVER = "http://194.24.246.71:35248";
    public static final String HTTP_SERVER = "http://oth.omega-auto.biz:35248";

    public static final String URL_HTTP_SERVICE_1C_LOGISTIC = "http://vb-api.omega-auto.biz:35241/LogisticaGate/hs/Services/Methods/ID/";
    public static final String URL_HTTP_SERVICE_1C_LOGISTIC_LOGIN = HTTP_SERVER + "/oth_server/hs/autorisation/login/";
    public static final String URL_HTTP_SERVICE_1C_LOGISTIC_BARCODE_TYPE = HTTP_SERVER + "/oth_server/hs/data/getType/";
    public static final String URL_HTTP_SERVICE_1C_LOGISTIC_GET_ZAYAVKA = HTTP_SERVER + "/oth_server/hs/data/zayavka/";
    public static final String URL_HTTP_SERVICE_1C_LOGISTIC_PUT_CONTAINER = HTTP_SERVER + "/oth_server/hs/sendData/scanKD/";

    public static final String HTTP_SERVICE_1C_LOGISTIC_LOGIN = "OutsideUser";
    public static final String HTTP_SERVICE_1C_LOGISTIC_PASSWORD = "gfhjkm";

    //public static final String URL_HTTP_SERVICE_1C_LOGISTIC = "http://shurik.asuscomm.com:8080/route/hs/Services/GetContainer/";
    //public static final String URL_HTTP_SERVICE_1C_LOGISTIC = "http://127.0.0.1/route/hs/Services/GetContainer/";
}
