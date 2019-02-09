package com.omegaauto.shurik.mobilesklad;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladFontSize;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;
import com.omegaauto.shurik.mobilesklad.utils.MySharedPref;
import com.omegaauto.shurik.mobilesklad.view.FontSizeAdapter;
import com.omegaauto.shurik.mobilesklad.view.ProgressButton;

import java.util.List;

import static java.lang.Thread.sleep;

public class SettingsAdditionalFragment extends Fragment {

    SwitchCompat switchCompatCounter;
    EditText editTextCounter;
    EditText editTextTimeout;
    ProgressButton buttonReset;
    TextView textFontSize;
    TextView textViewTimeout;
    Spinner spinnerFontSize;

    SettingsAdditionalOnCheckedListener onCheckedListener;
    SettingsAdditionalOnClickListener onClickListener;
    SettingsAdditionalOnTouchListener onTouchListener;
    SettingAdditionalOnItemSelectedListener onItemSelectedListener;

    Boolean resetIsDown;
    FontSizeAdapter fontSizeAdapter;

    public static SettingsAdditionalFragment getInstance(){
        SettingsAdditionalFragment settingsAdditionalFragment = new SettingsAdditionalFragment();
        return settingsAdditionalFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_additional, null);

        switchCompatCounter = (SwitchCompat) view.findViewById(R.id.fragment_settings_additional_switch_counter);
        editTextCounter = (EditText) view.findViewById(R.id.fragment_settings_additional_editText_counter);
        editTextTimeout = (EditText) view.findViewById(R.id.fragment_settings_additional_editText_timeout);
        buttonReset = (ProgressButton) view.findViewById(R.id.fragment_settings_additional_button_reset);
        textFontSize = (TextView) view.findViewById(R.id.fragment_settings_additional_text_view_spinner_name);
        textViewTimeout = (TextView) view.findViewById(R.id.fragment_settings_additional_text_view_timeout);
        spinnerFontSize = (Spinner) view.findViewById(R.id.fragment_settings_additional_spinner);

        fontSizeAdapter = new FontSizeAdapter(getContext(), R.layout.font_size_item);
        spinnerFontSize.setAdapter(fontSizeAdapter);
        spinnerFontSize.setSelection(fontSizeAdapter.getPosition(MobileSkladSettings.getInstance().getFontSize()));

        updateSettingsView();

        onCheckedListener = new SettingsAdditionalOnCheckedListener();
        onClickListener = new SettingsAdditionalOnClickListener();
        onTouchListener = new SettingsAdditionalOnTouchListener();
        onItemSelectedListener = new SettingAdditionalOnItemSelectedListener();

        switchCompatCounter.setOnCheckedChangeListener(onCheckedListener);
        //buttonReset.setOnClickListener(onClickListener);
        buttonReset.setOnTouchListener(onTouchListener);
        //buttonReset.setOnTouchListener();
        spinnerFontSize.setOnItemSelectedListener(onItemSelectedListener);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        String counter = editTextCounter.getText().toString();
        String timeout = editTextTimeout.getText().toString();

        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
        mobileSkladSettings.setCounter(Integer.valueOf(counter));
        mobileSkladSettings.setTimeout(Integer.valueOf(timeout));

    }

    private void settingsReset(){
        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
        mobileSkladSettings.initDefault();
        MySharedPref.saveMobileSkladSettings(getContext());
        spinnerFontSize.setSelection(fontSizeAdapter.getPosition(MobileSkladSettings.getInstance().getFontSize()));

        ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
        containerPropertiesSettings.initDefault();
        MySharedPref.saveSettings(getContext());
        //containerPropertiesSettings.notifyObservers(); // оповещение подписчиков об изменении настроек
    }

    private void updateSettingsView(){
        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();

        switchCompatCounter.setChecked(mobileSkladSettings.getCounterEnable());
        editTextCounter.setText(String.valueOf(mobileSkladSettings.getCounter()));
        editTextTimeout.setText(String.valueOf(mobileSkladSettings.getTimeout()));

        List<Fragment> fragmentList = getActivity().getSupportFragmentManager().getFragments();
        for (Fragment fragment: fragmentList) {
            if (fragment.getClass().getSimpleName().equals("SettingsContainerFragment")){
                SettingsContainerFragment settingsContainerFragment = (SettingsContainerFragment) fragment;
                settingsContainerFragment.updateView();
            }
            if (fragment.getClass().getSimpleName().equals("SettingsAdditionalFragment")){
                SettingsAdditionalFragment settingsAdditionalFragment = (SettingsAdditionalFragment) fragment;
                settingsAdditionalFragment.updateView();
            }
        }
    }

    public void updateView(){
        switchCompatCounter.setTextSize(TypedValue.COMPLEX_UNIT_SP, MobileSkladSettings.getInstance().getFontSize().getSizeValue());
        editTextCounter.setTextSize(TypedValue.COMPLEX_UNIT_SP, MobileSkladSettings.getInstance().getFontSize().getSizeValue());
        editTextTimeout.setTextSize(TypedValue.COMPLEX_UNIT_SP, MobileSkladSettings.getInstance().getFontSize().getSizeValue());
        textFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, MobileSkladSettings.getInstance().getFontSize().getSizeValue());
        textViewTimeout.setTextSize(TypedValue.COMPLEX_UNIT_SP, MobileSkladSettings.getInstance().getFontSize().getSizeValue());
    }

    class SettingsAdditionalOnCheckedListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
            mobileSkladSettings.setCounterEnable(isChecked);
        }

    }

    class SettingsAdditionalOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.fragment_settings_additional_button_reset){
                //settingsReset();
//                buttonReset.setRatio(ratio);
//                ratio = ratio +0.3f;
//                if (ratio >= 1) ratio = 0;
            }

        }
    }

    class SettingsAdditionalOnTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.fragment_settings_additional_button_reset){
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        resetIsDown = true;
                        new ProgressTask().execute("");
                        break;
                    case MotionEvent.ACTION_UP:
                        resetIsDown = false;
                        break;
                    default:
                        break;
                }

            }
            return false;
        }
    }

    private class ProgressTask extends AsyncTask<String, Float, String > {

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            buttonReset.setRatio(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                Float ratio = 0.1f;
                publishProgress(ratio);

                while (ratio < 1) {
                    ratio = ratio +0.02f;
                    sleep(5);
                    publishProgress(ratio);
                    if (resetIsDown == false) {
                        publishProgress(0f);
                        break;
                    }
                }

            } catch (InterruptedException exInterrupter){
                exInterrupter.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            publishProgress(0f);

            if (resetIsDown){
                // TODO сброс настроек
                settingsReset();
                updateSettingsView();
                Toast.makeText(getContext(), R.string.settings_reset_message, Toast.LENGTH_LONG).show();
            }

        }
    }


    class SettingAdditionalOnItemSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            MobileSkladFontSize fontSize = (MobileSkladFontSize) spinnerFontSize.getSelectedItem();
            if (fontSize != null) {
                MobileSkladSettings.getInstance().setFontSize(fontSize);
                updateSettingsView();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
