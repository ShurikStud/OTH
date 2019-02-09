package com.omegaauto.shurik.mobilesklad;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.omegaauto.shurik.mobilesklad.cameraScanBarCode.AnyOrientationCaptureActivity;

public class ScanActivity extends AppCompatActivity {

    Context context;

    IntentIntegrator qrScan;

/*
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
*/

    ScanOnKeyListener scanOnKeyListener;
    ScanOnClickListener scanOnClickListener;

    EditText textBarCode;
    EditText textBarCodeLast;

    FloatingActionButton buttonSettings;

    Button buttonSearch;
    Button buttonScan;

    long lastTimeBackPressed = 0;

/*
    static {
        System.loadLibrary("iconv");
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);

        context = this;

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.colorTextEnable));
//        setSupportActionBar(toolbar);

        scanOnKeyListener = new ScanOnKeyListener();
        scanOnClickListener = new ScanOnClickListener();

        textBarCode = (EditText) findViewById(R.id.activity_scan_edit_text_barcode);
        textBarCodeLast = (EditText) findViewById(R.id.activity_scan_edit_text_barcode_last);
        buttonSearch = (Button) findViewById(R.id.activity_scan_button_search);
        buttonScan = (Button) findViewById(R.id.activity_scan_button_scan);
        buttonSettings = (FloatingActionButton) findViewById(R.id.activity_scan_button_settings);

        buttonSearch.setOnClickListener(scanOnClickListener);
        buttonScan.setOnClickListener(scanOnClickListener);
        buttonSettings.setOnClickListener(scanOnClickListener);
        textBarCodeLast.setOnClickListener(scanOnClickListener);

        textBarCode.setOnKeyListener(scanOnKeyListener);
        buttonScan.setOnKeyListener(scanOnKeyListener);
        textBarCode.setOnClickListener(scanOnClickListener);

/*
        qrScan = new IntentIntegrator(this);
        //Fragment fragment = new Fragment();
        //qrScan = IntentIntegrator.forFragment(fragment);
        //qrScan.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        qrScan.setOrientationLocked(false);
        //qrScan.setBarcodeImageEnabled(true);
        Intent scanIntent = qrScan.createScanIntent();
*/


        qrScan = new IntentIntegrator(this);
        qrScan.setCaptureActivity(AnyOrientationCaptureActivity.class);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        qrScan.setPrompt("Scan something");
        qrScan.setOrientationLocked(true);
        qrScan.setBeepEnabled(false);
        //integrator.initiateScan();


  /*      autoFocusHandler = new Handler();

        mCamera = getCameraInstance();

        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3); //почему именно эти параметры нигде не указано
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        textBarCode.setFocusableInTouchMode(true);
        textBarCode.requestFocus();
    }

    /*private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {

        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    textBarCode.setText("barcode result " + sym.getData());
                    barcodeScanned = true;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };*/


    @Override
    public void onBackPressed() {

        long currentTimeMillis = System.currentTimeMillis();

        if ( (currentTimeMillis - 1000) > lastTimeBackPressed) {
            lastTimeBackPressed = currentTimeMillis;
            Toast.makeText(context,R.string.scan_exit_message, Toast.LENGTH_SHORT).show();
            return;
        }

        finish();
        System.exit(0);

        //super.onBackPressed();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar, menu);
//        return true;
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item.getItemId() == R.id.action_settings){
////            Intent intent = new Intent(this, SettingsContainerFragment.class);
//            Intent intent = new Intent(this, SettingsPagersActivity.class);
//            startActivity(intent);
//        }
//        return true;
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, R.string.scan_not_found, Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                //textBarCode.setText(result.getContents());
                startSearch(result.getContents(), true);
            }
        }
    }

    public void startScan() {
        qrScan.initiateScan();
/*
        if (barcodeScanned) {
            barcodeScanned = false;
            textBarCode.setText("Scanning...");
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            previewing = true;
            mCamera.autoFocus(autoFocusCB);
        }
*/
    }

    void startSearch(String barcode, boolean counterEnable){

        textBarCodeLast.setText(barcode);
        //textBarCodeLast.setText(textBarCode.getText());
        textBarCode.setText("");

        Intent containerIntent = new Intent(context, ContainerActivity.class);
        containerIntent.putExtra("Barcode", barcode);
        containerIntent.putExtra("CounterEnable", counterEnable);

        startActivity(containerIntent);
    }

/*    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }*/

    class ScanOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.activity_scan_button_scan){
                startScan();
                return;
            }
            if (v.getId() == R.id.activity_scan_button_search){
                if (textBarCode.length() > 0) {
                    startSearch(textBarCode.getText().toString(), false);
                }
                return;
            }
            if (v.getId() == R.id.activity_scan_edit_text_barcode_last){
                if (textBarCodeLast.length() > 0){
                    startSearch(textBarCodeLast.getText().toString(), false);
                }
            }
            if (v.getId() == R.id.activity_scan_edit_text_barcode) {
                textBarCode.setInputType(InputType.TYPE_MASK_CLASS);
            }

            if (v.getId() == R.id.activity_scan_button_settings){
                Intent intent = new Intent(context, SettingsPagersActivity.class);
                startActivity(intent);
            }

        }
    }

    class ScanOnKeyListener implements View.OnKeyListener{
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            //Toast.makeText(context, keyCode, Toast.LENGTH_SHORT).show();
            if (v.getId() == R.id.activity_scan_edit_text_barcode){
                //textBarCodeLast.setText(textBarCodeLast.getText() + String.valueOf(keyCode)+ '_');
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    if (textBarCode.length() > 0){
                        startSearch(textBarCode.getText().toString(), false);
                    }
                }
//                    startSearch();
//                    //Toast.makeText(context,String.valueOf(keyCode), Toast.LENGTH_SHORT).show();
//                }
            } else if (v.getId() == R.id.activity_scan_button_scan){
                return false;
            }
            return false;
        }
    }



}
