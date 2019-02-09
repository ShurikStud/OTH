package com.omegaauto.shurik.mobilesklad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.omegaauto.shurik.mobilesklad.settings.ListFontSizes;
import com.omegaauto.shurik.mobilesklad.utils.MySharedPref;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity    = this;

        Thread logoThread   = new Thread(){


            public void run(){

                try {

                    int logotime    = 800;

                    ListFontSizes listFontSizes = ListFontSizes.getInstance();
                    listFontSizes.init(activity.getBaseContext());

                    MySharedPref.loadSettings(activity.getBaseContext());
                    MySharedPref.loadMobileSkladSettings(activity.getBaseContext());
                    MySharedPref.loadZayavkaTEPList(activity.getBaseContext());

                    while (logotime > 0){

                        sleep(200);
                        logotime-=200;
                    }

                    //startActivity(new Intent(activity, MainActivity.class));
                    startActivity(new Intent(activity, ContainerActivity.class));

                } catch (InterruptedException interrEx){

                    interrEx.printStackTrace();

                }finally {
                    finish();
                }

            }

        };

        logoThread.start();

    }

}
