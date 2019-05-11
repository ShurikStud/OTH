package com.omegaauto.shurik.mobilesklad.view;

import android.view.View;

import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;

public class DrugDropSingleton {

    private static DrugDropSingleton instance;

    private View view;
    private ContainerPropertiesSettings.Property property;
    boolean beginDrug;

    // =========================================
    // методы
    // =========================================

    private DrugDropSingleton(){beginDrug = false;}

    public static DrugDropSingleton getInstance() {
        if (instance == null){
            instance = new DrugDropSingleton();
        }

        return instance;
    }

    public void startDrug(View v, ContainerPropertiesSettings.Property p){
        this.view = v;
        this.property = p;
        beginDrug = true;
    }

    public View getView() {
        return view;
    }

    public ContainerPropertiesSettings.Property getProperty() {
        return property;
    }

    public boolean isBeginDrug() {
        return beginDrug;
    }

    public void stopDrug(){
        beginDrug = false;
    }
}
