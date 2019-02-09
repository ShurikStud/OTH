package com.omegaauto.shurik.mobilesklad.settings;


import android.support.v4.app.Fragment;

import com.omegaauto.shurik.mobilesklad.SettingsAdditionalFragment;
import com.omegaauto.shurik.mobilesklad.SettingsContainerFragment;

public class BuilderSettingFragment {

    public static Fragment getSettingFragment(int index){
        //return SettingsContainerFragment.getInstance();
//        if (index == 0){
//            return SettingsContainerFragment.getInstance();
//        }

        switch (index){
            case 0:
                return SettingsContainerFragment.getInstance();
            case 1:
                return SettingsAdditionalFragment.getInstance();
            default:
                return null;
        }

    }

}
