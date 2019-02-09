package com.omegaauto.shurik.mobilesklad.HTTPservices;

import com.omegaauto.shurik.mobilesklad.container.Container;

import org.json.JSONException;
import org.json.JSONObject;

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

                if (jsonObjectData.has("vehicle_name"))
                    container.setVehicle_name(jsonObjectData.getString("vehicle_name"));
                //            if (jsonObjectData.has("trip_number"))
                //            container.setTrip_number(jsonObjectData.getString("trip_number"));
                if (jsonObjectData.has("IdTMS"))
                    container.setTrip_number(jsonObjectData.getString("IdTMS"));
                if (jsonObjectData.has("zayavkaTEP_number"))
                    container.setZayavkaTEP_number(jsonObjectData.getString("zayavkaTEP_number"));
                if (jsonObjectData.has("partner_name"))
                    container.setPartner_name(jsonObjectData.getString("partner_name"));
                if (jsonObjectData.has("number"))
                    container.setNumber(jsonObjectData.getString("number"));
                if (jsonObjectData.has("containersTotal"))
                    container.setContainersTotal(jsonObjectData.getString("containersTotal"));
                if (jsonObjectData.has("nn"))
                    container.setNn(jsonObjectData.getString("nn"));
                if (jsonObjectData.has("nnMax"))
                    container.setNnMax(jsonObjectData.getString("nnMax"));
                if (jsonObjectData.has("invoice_numbers"))
                    container.setInvoice_numbers(jsonObjectData.getString("invoice_numbers"));
                if (jsonObjectData.has("driver_name"))
                    container.setDriver_name(jsonObjectData.getString("driver_name"));
                if (jsonObjectData.has("amount_goods"))
                    container.setAmount_goods(jsonObjectData.getString("amount_goods"));
                if (jsonObjectData.has("amount_goodsTotal"))
                    container.setAmount_goodsTotal(jsonObjectData.getString("amount_goodsTotal"));
                if (jsonObjectData.has("partner_address"))
                    container.setPartner_address(jsonObjectData.getString("partner_address"));
                if (jsonObjectData.has("partner_phone"))
                    container.setPartner_phone(jsonObjectData.getString("partner_phone"));
                if (jsonObjectData.has("type_pack"))
                    container.setType_pack(jsonObjectData.getString("type_pack"));
                if (jsonObjectData.has("volume"))
                    container.setVolume(jsonObjectData.getString("volume"));
                if (jsonObjectData.has("volumeTotal"))
                    container.setVolumeTotal(jsonObjectData.getString("volumeTotal"));
                if (jsonObjectData.has("weight"))
                    container.setWeight(jsonObjectData.getString("weight"));
                if (jsonObjectData.has("weightTotal"))
                    container.setWeightTotal(jsonObjectData.getString("weightTotal"));
                if (jsonObjectData.has("zayavkaTEP_highway_date"))
                    container.setZayavkaTEP_highway_date(jsonObjectData.getString("zayavkaTEP_highway_date"));
                if (jsonObjectData.has("zayavkaTEP_highway_number"))
                    container.setZayavkaTEP_highway_number(jsonObjectData.getString("zayavkaTEP_highway_number"));
                if (jsonObjectData.has("RouteName"))
                    container.setRoute_name(jsonObjectData.getString("RouteName"));

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
