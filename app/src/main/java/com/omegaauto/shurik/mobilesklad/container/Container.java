/*
 * Copyright (c) 2018.
 * shurik
 */

package com.omegaauto.shurik.mobilesklad.container;

import com.omegaauto.shurik.mobilesklad.annotations.AnnoNL;
import com.omegaauto.shurik.mobilesklad.annotations.AnnoProperty;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Container {

    /*
        Для добавления нового отображаемого поля необходимо:
        1) добавить свойство в классе Container (тип - String)
        2) добавить get-ер и set-ер в классе Container
        3) в методе setAllProperties добавить установку значения нового свойства
        4) в методе copy() реализовать копирование свойства из основания
        5) в методе toString() реализовать вывод значения нового свойства
        6) в классе ContainerPropertiesSettings в методе initDefault() необходимо добавить новое свойство,
            если оно требует отображения отображения. Важно: свойство name класса Property должно совпадать
            с методом get-ера, но без приставки 'get'
        7) в классе ParserHttpResponse в методе getContainerInfo() дабавить запись значения нового свойства,
            если оно вернулось с сервера
     */


    @AnnoProperty
    String driver_name; // ФИО водителя
    @AnnoProperty
    String vehicle_name; //№ транспортного средства)

    @AnnoProperty
    String zayavkaTEP_highway_date; // (Дата Заявки ТЭП магистральной)
    @AnnoProperty
    String zayavkaTEP_highway_number; // (№ Заявки ТЭП магистральной)
    @AnnoProperty
    String zayavkaTEP_number; // (№ Заявки ТЭП подчиненной)
    @AnnoProperty
    String route_name; // Наименование маршрута
    @AnnoProperty
    String trip_number = ""; // (№ рейса)
    @AnnoProperty
    String nn; // (№ по порядку в карте погрузки)
    @AnnoProperty
    String nnMax; // (№ по порядку в карте погрузки)

    @AnnoProperty
    @AnnoNL
    String partner_address; // (Адрес доставки (Строка))
    @AnnoProperty
    String partner_name; // (Наименование контрагента)

    @AnnoProperty
    @AnnoNL
    String partner_phone; // (Тел. Номера контрагента)

    @AnnoNL
    @AnnoProperty
    String invoice_numbers; // (Номера РН )
    @AnnoProperty
    String type_pack; // (Тип упаковки)
    @AnnoProperty
    String weight; // (Вес, кг.)
    @AnnoProperty
    String weightTotal; // (Вес, кг.) общий вес

    @AnnoProperty
    String amount_goods; // (Количество грузов)
    @AnnoProperty
    String amount_goodsTotal; // (Количество грузов) общее количество
    @AnnoProperty
    String sum_amount_cont; // (Количество единиц товара в контейнере)

    @AnnoProperty
    String number; // (Номер тарного места)
    @AnnoProperty
    String containersTotal; // общее число контейнеров на клиента
    @AnnoProperty
    String volume; // (Объем ТарногоМеста)
    @AnnoProperty
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

    public String getSum_amount_cont() {
        return sum_amount_cont;
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

    public void setSum_amount_cont(String sum_amount_cont) {
        this.sum_amount_cont = sum_amount_cont;
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

        Class currentClass = getClass();

        for (Field field : currentClass.getDeclaredFields()) {
            AnnoProperty property = (AnnoProperty) field.getAnnotation(AnnoProperty.class);
            if (property != null) {
                try {
                    field.set(this, value); ;
                    //s = (String) method.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

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

        Class currentClass = this.getClass();

        for (Field field: currentClass.getFields()) {
            AnnoProperty property = field.getAnnotation(AnnoProperty.class);
            AnnoNL newLine = field.getAnnotation(AnnoNL.class);

            try {
                if (property != null) {
                    field.set(containerCopy, (String) field.get(this));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

//        containerCopy.setAmount_goods(this.amount_goods);
//        containerCopy.setAmount_goodsTotal(this.amount_goodsTotal);
//        containerCopy.setContainersTotal(this.containersTotal);
//        containerCopy.setDriver_name(this.driver_name);
//        containerCopy.setInvoice_numbers(this.invoice_numbers);
//        containerCopy.setNn(this.nn);
//        containerCopy.setNnMax(this.nnMax);
//        containerCopy.setNumber(this.number);
//        containerCopy.setPartner_address(this.partner_address);
//        containerCopy.setPartner_name(this.partner_name);
//        containerCopy.setPartner_phone(this.partner_phone);
//        containerCopy.setSum_amount_cont(this.sum_amount_cont);
//        containerCopy.setTrip_number(this.trip_number);
//        containerCopy.setType_pack(this.type_pack);
//        containerCopy.setVehicle_name(this.vehicle_name);
//        containerCopy.setVolume(this.volume);
//        containerCopy.setVolumeTotal(this.volumeTotal);
//        containerCopy.setWeight(this.weight);
//        containerCopy.setWeightTotal(this.weightTotal);
//        containerCopy.setZayavkaTEP_highway_date(this.zayavkaTEP_highway_date);
//        containerCopy.setZayavkaTEP_highway_number(this.zayavkaTEP_highway_number);
//        containerCopy.setZayavkaTEP_number(this.zayavkaTEP_number);

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
