package com.omegaauto.shurik.mobilesklad.view;

import com.omegaauto.shurik.mobilesklad.container.Container;
import com.omegaauto.shurik.mobilesklad.zayavkaTEP.ZayavkaTEP;
import com.omegaauto.shurik.mobilesklad.zayavkaTEP.ZayavkaTEPList;

public class ZayavkaTEPListAdapter {

    public static final int MAX_SIZE = 4;
    ZayavkaTEPList zayavkaTEPList;

    public ZayavkaTEPListAdapter() {

    }

    public void setZayavkaTEPList(ZayavkaTEPList zayavkaTEPList) {
        this.zayavkaTEPList = zayavkaTEPList;
    }

    public boolean addZayavkaTEP(ZayavkaTEP zayavkaTEP){
        if (zayavkaTEPList == null){
            return false;
        }

        int index = zayavkaTEPList.findZayavkaTEPbyNumber(zayavkaTEP.getNumber());

        if (index >= 0) {
            zayavkaTEPList.deleteZayavkaTEP(index);
        }

        zayavkaTEPList.addZayavkaTEP(zayavkaTEP);

        if (zayavkaTEPList.getCount() > MAX_SIZE){
//            zayavkaTEPList.deleteZayavkaTEP(ZayavkaTEPList.getMaxSize());
            zayavkaTEPList.deleteZayavkaTEP(0);
        }
        return false;
    }

    public boolean deleteZayavkaTEP(int index){
        if ( (index < 0) || (index > zayavkaTEPList.getCount()) ){
            return false;
        }
        zayavkaTEPList.deleteZayavkaTEP(index);
        return true;
    }

    public int getCount(){
        return zayavkaTEPList.getCount();
    }

    public ZayavkaTEP getZayavka(int index){
        return zayavkaTEPList.get(index);
    }

    public Container findContainer(String barcode){

       for (int i = 0; i < zayavkaTEPList.getCount(); i++) {
            ZayavkaTEP zayavkaTEP = zayavkaTEPList.get(i);

            for (int y = 0; y < zayavkaTEP.getCountNumbers(); y++){
                Container containerTemp = zayavkaTEP.getContainer(y);

                if (containerTemp.getNumberValue().equals(barcode)) {
                    return containerTemp.copy();
                }

            }
        }
        return null;
    }


    public static int getMaxSize() {
        return MAX_SIZE;
    }
}
