/*
 * Copyright (c) 2018.
 * shurik
 */

package com.omegaauto.shurik.mobilesklad.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Container {

    String driver_name; // ФИО водителя
    String vehicle_name; //№ транспортного средства)

    String zayavkaTEP_highway_date; // (Дата Заявки ТЭП магистральной)
    String zayavkaTEP_highway_number; // (№ Заявки ТЭП магистральной)
    String zayavkaTEP_number; // (№ Заявки ТЭП подчиненной)
    String route_name; // Наименование маршрута
    String trip_number = ""; // (№ рейса)
    String nn; // (№ по порядку в карте погрузки)
    String nnMax; // (№ по порядку в карте погрузки)
    String partner_address; // (Адрес доставки (Строка))
    String partner_name; // (Наименование контрагента)
    String partner_phone; // (Тел. Номера контрагента)
    String invoice_numbers; // (Номера РН )
    String type_pack; // (Тип упаковки)
    String weight; // (Вес, кг.)
    String weightTotal; // (Вес, кг.) общий вес

    String amount_goods; // (Количество грузов)
    String amount_goodsTotal; // (Количество грузов) общее количество

    String number; // (Номер тарного места)
    String containersTotal; // общее число контейнеров на клиента
    String volume; // (Объем ТарногоМеста)
    String volumeTotal; // (Объем ТарногоМеста) общий объем

//    @ColorInt int colorMaster;
//    @ColorInt int colorSlave;

    public Container() {

        setNoData();
//        colorMaster = context.getResources().getColor(R.color.colorTextEnable);
//        colorSlave = context.getResources().getColor(R.color.black);

    }

    public String getPropertyValueString(String propertyName){
        // возвращает значение свойства, переданного в параметре, в текстовом формате

        Class thisClass = getClass();


        try {
            Method method = thisClass.getMethod("get" + propertyName, null);

            if (method == null) {
                return "";
            } else {
                String result = String.valueOf(method.invoke(this, null));
                return result;
            }
        }catch (NoSuchMethodException e1){
            return "";
        }catch (InvocationTargetException e2){
            return "";
        }catch (IllegalAccessException e3){
            return "";
        }

    }

    public String getDriver_name() {
        return driver_name;
    }

//    public String getStringDriver_name() {
//        return driver_name;
//    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getZayavkaTEP_highway_date() {
        return zayavkaTEP_highway_date;
    }

    public void setZayavkaTEP_highway_date(String zayavkaTEP_highway_date) {
        this.zayavkaTEP_highway_date = zayavkaTEP_highway_date;
    }

    public String getZayavkaTEP_highway_number() {
        return zayavkaTEP_highway_number;
    }

    public void setZayavkaTEP_highway_number(String zayavkaTEP_highway_number) {
        this.zayavkaTEP_highway_number = zayavkaTEP_highway_number;
    }

    public String getZayavkaTEP_number() {
        return zayavkaTEP_number;
    }

    public void setZayavkaTEP_number(String zayavkaTEP_number) {
        this.zayavkaTEP_number = zayavkaTEP_number;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getTrip_number() {
        return trip_number;
    }

    public void setTrip_number(String trip_number) {
        this.trip_number = trip_number;
    }

//    public SpannableStringBuilder getNn() {
//        String textResult = nn + " / " + nnMax;
//
//        final SpannableStringBuilder str = new SpannableStringBuilder(textResult);
//        str.setSpan(
//                new ForegroundColorSpan(colorSlave),
//                textResult.indexOf('/'),
//                textResult.length()-1,
//                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        return str;
//    }

    public String getNn() {
        return nn + " / " + nnMax;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

    public String getPartner_address() {
        return convertToNL(partner_address, ';');
    }

    public void setPartner_address(String partner_address) {
        this.partner_address = partner_address;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPartner_phone() {
        return convertToNL(partner_phone, ',');
    }

    public void setPartner_phone(String partner_phone) {
        this.partner_phone = partner_phone;
    }

    public String getInvoice_numbers() {
        return invoice_numbers;
    }

    public void setInvoice_numbers(String invoice_numbers) {
        this.invoice_numbers = convertToNL(invoice_numbers, ',');
    }

    public String getType_pack() {
        return type_pack;
    }

    public void setType_pack(String type_pack) {
        this.type_pack = type_pack;
    }

    public String getWeight() {
        return weight + " / " + weightTotal;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAmount_goods() {
        return amount_goods + " / " + amount_goodsTotal;
    }

    public void setAmount_goods(String amount_goods) {
        this.amount_goods = amount_goods;
    }

    public String getNumber() {
        return number + " из " + containersTotal;
    }

    public String getNumberValue() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setContainersTotal(String containersTotal) {
        this.containersTotal = containersTotal;
    }

    public String getVolume() {
        return volume + " / " + volumeTotal;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getNnMax() {
        return nnMax;
    }

    public void setNnMax(String nnMax) {
        this.nnMax = nnMax;
    }

    public void setWeightTotal(String weightTotal) {
        this.weightTotal = weightTotal;
    }

    public void setAmount_goodsTotal(String amount_goodsTotal) {
        this.amount_goodsTotal = amount_goodsTotal;
    }

    public void setVolumeTotal(String volumeTotal) {
        this.volumeTotal = volumeTotal;
    }

    private String convertToNL(String inputString, char oldSymbol){
        String outputString;

        outputString = inputString.replace(oldSymbol, '\n');

        return outputString;
    }

    private void setAllProperties(String value){
        driver_name = value;
        vehicle_name = value;

        zayavkaTEP_highway_date = value;
        zayavkaTEP_highway_number = value;
        zayavkaTEP_number = value;
        route_name = value;
        trip_number = value;
        nn = value;
        nnMax = value;
        partner_address = value;
        partner_name = value;
        partner_phone = value;
        invoice_numbers = value;
        type_pack = value;
        weight = value;
        weightTotal = value;

        amount_goods = value;
        amount_goodsTotal = value;

        number = value;
        containersTotal = value;
        volume = value;
        volumeTotal = value;
    }

    public void setNoData(){
        setAllProperties("нет данных");
    }

    public void setNoDataOffline(){
        setAllProperties("OFFLINE: нет данных");
    }

    public void setNoConnect(){
        setAllProperties("нет связи");
    }

    public void setTimeout(){
        setAllProperties("превышено ожидание");
    }

    public void setError(String errorString){
        setAllProperties(errorString);
    }

    public Container copy(){
        Container containerCopy = new Container();
        containerCopy.setAmount_goods(this.amount_goods);
        containerCopy.setAmount_goodsTotal(this.amount_goodsTotal);
        containerCopy.setContainersTotal(this.containersTotal);
        containerCopy.setDriver_name(this.driver_name);
        containerCopy.setInvoice_numbers(this.invoice_numbers);
        containerCopy.setNn(this.nn);
        containerCopy.setNnMax(this.nnMax);
        containerCopy.setNumber(this.number);
        containerCopy.setPartner_address(this.partner_address);
        containerCopy.setPartner_name(this.partner_name);
        containerCopy.setPartner_phone(this.partner_phone);
        containerCopy.setTrip_number(this.trip_number);
        containerCopy.setType_pack(this.type_pack);
        containerCopy.setVehicle_name(this.vehicle_name);
        containerCopy.setVolume(this.volume);
        containerCopy.setVolumeTotal(this.volumeTotal);
        containerCopy.setWeight(this.weight);
        containerCopy.setWeightTotal(this.weightTotal);
        containerCopy.setZayavkaTEP_highway_date(this.zayavkaTEP_highway_date);
        containerCopy.setZayavkaTEP_highway_number(this.zayavkaTEP_highway_number);
        containerCopy.setZayavkaTEP_number(this.zayavkaTEP_number);

        return containerCopy;
    }

    @Override
    public String toString() {
        return "Container{" +
                "driver_name='" + driver_name + '\'' +
                ", vehicle_name='" + vehicle_name + '\'' +
                ", zayavkaTEP_highway_date=" + zayavkaTEP_highway_date +
                ", zayavkaTEP_highway_number=" + zayavkaTEP_highway_number +
                ", zayavkaTEP_number='" + zayavkaTEP_number + '\'' +
                ", trip_number='" + trip_number + '\'' +
                ", nn=" + nn +
                ", partner_address='" + partner_address + '\'' +
                ", partner_name='" + partner_name + '\'' +
                ", partner_phone='" + partner_phone + '\'' +
                ", invoice_numbers='" + invoice_numbers + '\'' +
                ", type_pack='" + type_pack + '\'' +
                ", weight=" + weight +
                ", amount_goods=" + amount_goods +
                ", number=" + number +
                ", volume=" + volume +
                '}';
    }
}
