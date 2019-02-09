package com.omegaauto.shurik.mobilesklad;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;

import com.omegaauto.shurik.mobilesklad.container.Container;
import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;

public class MainActivity extends AppCompatActivity {

    ScrollView scrollView_mainActivity;

    RadioGroup radioGroupMode;
    RadioButton radioButtonOnline;
    RadioButton radioButtonOffline;
    RadioButton radioButtonOfflineOnline;
    ProgressBar progressBarLogin;

    AutoCompleteTextView textEmail;
    EditText textPassword;

    Switch switchAutoLogin;

    Button buttonStart;

    ListenerMainActivity listenerMainActivity;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        listenerMainActivity = new ListenerMainActivity();

        scrollView_mainActivity = (ScrollView) findViewById(R.id.scrollView_mainActivity);
        radioGroupMode = (RadioGroup) findViewById(R.id.activity_main_radio_group);
        radioButtonOnline = (RadioButton) findViewById(R.id.radioButton_online);
        radioButtonOffline = (RadioButton) findViewById(R.id.radioButton_offline);
        radioButtonOfflineOnline = (RadioButton) findViewById(R.id.radioButton_offline_online);
        progressBarLogin = (ProgressBar) findViewById(R.id.login_progress);

        textEmail = (AutoCompleteTextView) findViewById(R.id.login_email);
        textPassword = (EditText) findViewById(R.id.login_password);

        switchAutoLogin = (Switch) findViewById(R.id.switch_autologin);

        buttonStart = (Button) findViewById(R.id.email_sign_in_button);

        radioGroupMode.clearCheck();
        radioGroupMode.check(radioButtonOnline.getId());

        // сделать недоступными элементы, которые пока не поддерживаются
        radioButtonOffline.setEnabled(false);
        radioButtonOfflineOnline.setEnabled(false);
        textEmail.setEnabled(false);
        textPassword.setEnabled(false);
        switchAutoLogin.setEnabled(false);

        buttonStart.setOnClickListener(listenerMainActivity);

    }

    class ListenerMainActivity implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            //startActivity(new Intent(context, ContainerActivity.class));

            startActivity(new Intent(context, ScanActivity.class));
            finish();

        }
    }

}
