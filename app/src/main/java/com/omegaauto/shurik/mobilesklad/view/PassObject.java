package com.omegaauto.shurik.mobilesklad.view;

import android.view.View;

import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;

public class PassObject{
    View view;
    ContainerPropertiesSettings.Property property;
    //ContainerPropertiesSettings containerPropertiesSettings;

    PassObject(View view, ContainerPropertiesSettings.Property property){
//    PassObject(View view, ContainerPropertiesSettings.Property property, List<ContainerPropertiesSettings.Property> s){
        this.view = view;
        this.property = property;
        //containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
    }
}