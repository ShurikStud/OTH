package com.omegaauto.shurik.mobilesklad.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.omegaauto.shurik.mobilesklad.container.Container;
import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.google.gson.Gson;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;
import com.omegaauto.shurik.mobilesklad.zayavkaTEP.ZayavkaTEPList;

public class MySharedPref {

    private static final String CONTAINER_PROPERTIES_SETTINGS = "ContainerPropertiesSettings";
    private static final String CONTAINER_BARCODE = "ContainerBarcode";
    private static final String CONTAINER_PROPERTIES = "ContainerProperties";
    private static final String MOBILE_SKLAD_SETTINGS = "MobileSkladSettings";
    private static final String ZAYAVKA_TEP_LIST = "ZayavkaTEPList";

    public static void saveSettings(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONTAINER_PROPERTIES_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor    shPreferencesEditor  = sharedPreferences.edit();

        ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
        //containerPropertiesSettings.notifyObservers();

        Gson gson   = new Gson();
        String json = gson.toJson(containerPropertiesSettings);

        shPreferencesEditor.putString(CONTAINER_PROPERTIES_SETTINGS, json);
        shPreferencesEditor.commit();
    }

    public static void saveBarcode(Context context, String barcode){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONTAINER_BARCODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor    shPreferencesEditor  = sharedPreferences.edit();

        Gson gson   = new Gson();
        String json = gson.toJson(barcode);

        shPreferencesEditor.putString(CONTAINER_BARCODE, json);

        shPreferencesEditor.commit();

    }

    public static void saveContainer(Context context, Container container){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONTAINER_PROPERTIES, Context.MODE_PRIVATE);
        SharedPreferences.Editor    shPreferencesEditor  = sharedPreferences.edit();

        Gson gson   = new Gson();
        String json = gson.toJson(container);

        shPreferencesEditor.putString(CONTAINER_PROPERTIES, json);

        shPreferencesEditor.commit();

    }

    public static void saveMobileSkladSettings(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(MOBILE_SKLAD_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor    shPreferencesEditor  = sharedPreferences.edit();

        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();

        Gson gson   = new Gson();
        String json = gson.toJson(mobileSkladSettings);

        shPreferencesEditor.putString(MOBILE_SKLAD_SETTINGS, json);

        shPreferencesEditor.commit();
    }

    public static void saveZayavkaTEPList(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ZAYAVKA_TEP_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor    shPreferencesEditor  = sharedPreferences.edit();

        ZayavkaTEPList zayavkaTEPList = ZayavkaTEPList.getInstance();

        Gson gson   = new Gson();
        String json = gson.toJson(zayavkaTEPList);

        shPreferencesEditor.putString(ZAYAVKA_TEP_LIST, json);

        shPreferencesEditor.commit();
    }

    public static void loadSettings(Context context){

        ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();

        SharedPreferences sharedPreferences = context.getSharedPreferences(CONTAINER_PROPERTIES_SETTINGS, Context.MODE_PRIVATE);
        Gson gson   = new Gson();

        String json = sharedPreferences.getString(CONTAINER_PROPERTIES_SETTINGS, "");

        if (json.equals("")){
            containerPropertiesSettings.initDefault();
        } else {
            containerPropertiesSettings.setProperties(gson.fromJson(json, ContainerPropertiesSettings.class));
            //gson.fromJson(json, ContainerPropertiesSettings.class);
        }

    }

    public static String loadBarcode(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(CONTAINER_BARCODE, Context.MODE_PRIVATE);
        Gson gson   = new Gson();

        String json = sharedPreferences.getString(CONTAINER_BARCODE, "");

        if (json.equals("")){
            return "";
        } else {
            return gson.fromJson(json, String.class);
        }

    }

    public static Container loadContainer(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(CONTAINER_PROPERTIES, Context.MODE_PRIVATE);
        Gson gson   = new Gson();

        String json = sharedPreferences.getString(CONTAINER_PROPERTIES, "");

        if (json.equals("")){
            return null;
        } else {
            return gson.fromJson(json, Container.class);
        }

    }

    public static void loadMobileSkladSettings(Context context){
        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();

        SharedPreferences sharedPreferences = context.getSharedPreferences(MOBILE_SKLAD_SETTINGS, Context.MODE_PRIVATE);
        Gson gson   = new Gson();

        String json = sharedPreferences.getString(MOBILE_SKLAD_SETTINGS, "");

        if (json.equals("")){
            mobileSkladSettings.initDefault();
        } else {
            mobileSkladSettings.setProperties(gson.fromJson(json, MobileSkladSettings.class));
            //gson.fromJson(json, ContainerPropertiesSettings.class);
        }

    }

    public static void loadZayavkaTEPList(Context context){
        ZayavkaTEPList zayavkaTEPList = ZayavkaTEPList.getInstance();

        SharedPreferences sharedPreferences = context.getSharedPreferences(ZAYAVKA_TEP_LIST, Context.MODE_PRIVATE);
        Gson gson   = new Gson();

        String json = sharedPreferences.getString(ZAYAVKA_TEP_LIST, "");

        if (json.equals("")){
            //zayavkaTEPList.initDefault();
        } else {
            zayavkaTEPList.setProperties(gson.fromJson(json, ZayavkaTEPList.class));
            //gson.fromJson(json, ContainerPropertiesSettings.class);
        }
    }

}
