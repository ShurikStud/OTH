package com.omegaauto.shurik.mobilesklad.zayavkaTEP;

import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;

import java.util.ArrayList;
import java.util.List;

public class ZayavkaTEPList {

    private static ZayavkaTEPList instance;
    private List<ZayavkaTEP> zayavkaTEPS;

    private ZayavkaTEPList() {
        zayavkaTEPS = new ArrayList<ZayavkaTEP>();
    }

    static public ZayavkaTEPList getInstance(){

        if (instance == null){
            instance = new ZayavkaTEPList();
        }
        return instance;
    }

    public int getCount(){
        return zayavkaTEPS.size();
    }

    public int findZayavkaTEPbyNumber(String number){

        for (ZayavkaTEP zayavkaTEP: zayavkaTEPS) {
            if (zayavkaTEP.getNumber().equals(number)){
                return zayavkaTEPS.indexOf(zayavkaTEP);
            }
        }
        return -1;
    }

    public void addZayavkaTEP(ZayavkaTEP zayavkaTEP){
        zayavkaTEPS.add(zayavkaTEP);
    }

    public boolean deleteZayavkaTEP(ZayavkaTEP zayavkaTEP){
        return zayavkaTEPS.remove(zayavkaTEP);
    }

    public boolean deleteZayavkaTEP(int position){
        if (zayavkaTEPS.size() >= position) {
            zayavkaTEPS.remove(position);
            return true;
        } else {
            return false;
        }
    }

    public ZayavkaTEP get(int index){
        if (index > zayavkaTEPS.size()){
            return null;
        }
        return zayavkaTEPS.get(index);
    }

    public void setProperties(ZayavkaTEPList input){
        zayavkaTEPS = input.zayavkaTEPS;
    }

    public void clear(){
        zayavkaTEPS.clear();
    }

}
