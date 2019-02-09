package com.omegaauto.shurik.mobilesklad;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.omegaauto.shurik.mobilesklad.settings.BuilderSettingFragment;
import com.omegaauto.shurik.mobilesklad.utils.MySharedPref;

public class SettingsPagersActivity extends FragmentActivity {

    ViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pagers);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.colorBackgroundSeparatorCenter));
        }


        pager = (ViewPager) findViewById(R.id.activity_settings_view_pager);
        pagerAdapter = new MobileSkladPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);



    }

    @Override
    protected void onPause() {
        super.onPause();
        MySharedPref.saveSettings(this);
        MySharedPref.saveMobileSkladSettings(this);

    }

    class MobileSkladPagerAdapter extends FragmentPagerAdapter {

        public MobileSkladPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return null;
            return BuilderSettingFragment.getSettingFragment(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0){
                return "Поля контейнера";
            }
            switch (position){
                case 0:
                    return "Поля контейнера";
                case 1:
                    return "Дополнительно";
                default:
                    return "";
            }

        }
    }

}
