package com.omegaauto.shurik.mobilesklad.zayavkaTEP;

import com.omegaauto.shurik.mobilesklad.container.Container;

import java.util.ArrayList;
import java.util.List;

public class ZayavkaTEP {
    private String number;
    //private int countNumbers;

    private List<Container> containers;

    private ZayavkaTEP_header zayavkaTEP_header;

    public ZayavkaTEP() {
        number = "0";
        zayavkaTEP_header = new ZayavkaTEP_header();
        //countNumbers = 0;
        containers = new ArrayList<Container>();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCountNumbers() {
//        return countNumbers;
        return containers.size();
    }

//    private void setCountNumbers(int countNumbers) {
//        this.countNumbers = countNumbers;
//    }

    public void addContainer(Container container){
        containers.add(container);
        //countNumbers++;
    }

    public Container getContainer(int position){
        if ( (position < 0) || (position >= containers.size()) ){
            return null;
        }

        return containers.get(position);
    }

    public ZayavkaTEP_header getZayavkaTEP_header() {
        return zayavkaTEP_header;
    }

    public void setZayavkaTEP_header(ZayavkaTEP_header zayavkaTEP_header) {
        this.zayavkaTEP_header = zayavkaTEP_header;
    }
}
