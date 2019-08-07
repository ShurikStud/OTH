package com.omegaauto.shurik.mobilesklad.HTTPservices;

import com.omegaauto.shurik.mobilesklad.annotations.AnnoNL;
import com.omegaauto.shurik.mobilesklad.annotations.AnnoProperty;
import com.omegaauto.shurik.mobilesklad.container.Container;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Iterator;

public class ParserHttpResponse {

    private String errorString;

    private String stringContainerInfo = "";
    private String stringTokenInfo = "";

    public ParserHttpResponse() {
        errorString = new String();
    }

    public void setContainerInfoString(String jsonString){
        stringContainerInfo = jsonString;
    }

    public void setTokenInfoString(String jsonString){
        stringTokenInfo = jsonString;
    }

    public Container getContainerInfo(){

        errorString = "";

        if (stringContainerInfo==null || stringContainerInfo.isEmpty()){
            errorString = "getContainerInfo():: нет данных для расшифровки";
            Container container = new Container();
            container.setNoData();
            return container;
        }

        try {
            JSONObject jsonObject = new JSONObject(stringContainerInfo);
            JSONObject jsonObjectStatus = (JSONObject) jsonObject.get("Status");
            JSONObject jsonObjectData = (JSONObject) jsonObject.get("Data");

            String statusDescription = "";
            if (jsonObjectStatus.has("StatusDescription")) {
                statusDescription = jsonObjectStatus.getString("StatusDescription");
            }

            Container container = new Container();
            container.setNoData();

            if (statusDescription.equals("No data")) {
//                container.setNoData();
                errorString = "Контейнер не найден";
                return container;
            } else if (statusDescription.equals("SQL-connection has not been esteblished")) {
                errorString = "На сервере произошла ошибка при доступе к SQL-базе данных";
                return container;
            } else if (statusDescription.equals("OK")) {

                Class classContainer = container.getClass();

                for (Field field : classContainer.getDeclaredFields()) {
                    AnnoProperty propertyAnno = (AnnoProperty) field.getAnnotation(AnnoProperty.class);
                    //AnnoNL newLine = field.getAnnotation(AnnoNL.class);
                    if (propertyAnno != null) {
                        try {
                            String fieldName = field.getName();
                            if (!propertyAnno.apiName().isEmpty()){
                                fieldName = propertyAnno.apiName();
                            }
                            if (jsonObjectData.has(fieldName)){
                                field.set(container, jsonObjectData.getString(fieldName));
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                container.setNL();

                return container;
            } else {

                return container;
            }

        } catch (JSONException ex) {
            errorString = ex.toString();
        }

        Container container = new Container();
        container.setNoData();

        return container;

    }

    public String getErrorString() {
        return errorString;
    }
}
