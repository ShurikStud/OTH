package com.omegaauto.shurik.mobilesklad;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.omegaauto.shurik.mobilesklad.HTTPservices.LogisticHttpService;
import com.omegaauto.shurik.mobilesklad.HTTPservices.ParserHttpResponse;
import com.omegaauto.shurik.mobilesklad.cameraScanBarCode.AnyOrientationCaptureActivity;
import com.omegaauto.shurik.mobilesklad.container.NumberListLast;
import com.omegaauto.shurik.mobilesklad.controller.ControllerContainers;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;
import com.omegaauto.shurik.mobilesklad.container.Container;
import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.omegaauto.shurik.mobilesklad.user.MobileSkladUser;
import com.omegaauto.shurik.mobilesklad.utils.EnumBarcodeType;
import com.omegaauto.shurik.mobilesklad.utils.ErrorType;
import com.omegaauto.shurik.mobilesklad.utils.MySharedPref;
import com.omegaauto.shurik.mobilesklad.view.ContainerPropertiesAdapter;
import com.omegaauto.shurik.mobilesklad.view.ZayavkaTEPListAdapter;
import com.omegaauto.shurik.mobilesklad.zayavkaTEP.ZayavkaTEP;
import com.omegaauto.shurik.mobilesklad.zayavkaTEP.ZayavkaTEPList;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class ContainerActivity extends AppCompatActivity {

    private static final String LOG = "LOG_LOG";

    private static final int REQUEST_CONTAINER_NUMBER  = 100;
    private static final int REQUEST_RESULT_OK = 1;

    String lastBarcode = "";
    String scanBarcode = "";
    Boolean isCameraScanComplete = false; // флаг взвоится после удачного сканирования при помощи камеры
    Boolean isMenuDraw;
    Context context;
    ListView listView;
    ProgressBar progressBarHTTPService;
    ProgressBar progressBarZayavkaTEP;

    String errorMessage="";

    IntentIntegrator qrScan;

    Container container;
    ZayavkaTEP zayavkaTEP;

    ZayavkaTEPListAdapter zayavkaTEPListAdapter;

//
    Window window;
    TextView textViewContainer;
    TextView textViewCounter;
    EditText editTextContainer;
    ImageButton imageButtonMenu;
    LinearLayout linearLayoutCamera;
    NavigationView navigationView;

    private DrawerLayout drawer;

    Menu menu;

    MobileSkladSettings mobileSkladSettings;

    ControllerContainers controllerContainers;

    ContainerOnKeyListener containerOnKeyListener;
    ContainerOnClickListener containerOnClickListener;
    ContainerOnTouchListener containerOnTouchListener;
    ContainerOnCheckedChangeListener containerOnCheckedChangeListener;
    ContainerOnMenuItemClickListener containerOnMenuItemClickListener;
    ContainerOnSettingsContainerRedoListener containerOnSettingsContainerRedoListener;

    ContainerPropertiesSettings containerPropertiesSettings;
    ContainerPropertiesAdapter containerPropertiesAdapter;

    ImageView imageNetwork;
    ImageView imageNetworkOffline;
    ImageView imageLogin;

    ImageView imageViewUser; // картинка аватара юзера
    TextView textViewUserName; // имя пользователя
    TextView textViewUserEmail; // почта пользователя (она же - логин)


    SwitchCompat onlineSwitch;
    SwitchCompat offlineSwitch;

    long lastTimeBackPressed = 0;

    ProgressDialog zayavkaProgressDialog;
    ProgressZayavkaTask progressZayavkaTask;
    CounterTask counterTask;
    Handler h;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_container);

        isMenuDraw = false;
        containerPropertiesSettings = ContainerPropertiesSettings.getInstance();

        controllerContainers = new ControllerContainers();
        containerOnKeyListener = new ContainerOnKeyListener();
        containerOnClickListener = new ContainerOnClickListener();
        containerOnTouchListener = new ContainerOnTouchListener();
        containerOnCheckedChangeListener = new ContainerOnCheckedChangeListener();
        containerOnMenuItemClickListener = new ContainerOnMenuItemClickListener();
        containerOnSettingsContainerRedoListener = new ContainerOnSettingsContainerRedoListener();

        // инициализация диалога загрузки данных по заявке ТЭП
        zayavkaProgressDialog = new ProgressDialog(context);
        zayavkaProgressDialog.setOnCancelListener(new ContainerOnCanceled());
        // меняем стиль на индикатор
        zayavkaProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        zayavkaProgressDialog.setCanceledOnTouchOutside(false);

        drawer = (DrawerLayout) findViewById(R.id.activity_container_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mobileSkladSettings = MobileSkladSettings.getInstance();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new ContainerOnNavigationItemSelectedListener());
        navigationView.setItemIconTintList(null);
        menu = navigationView.getMenu();

        //navigationView.setOnClickListener(containerOnClickListener);

        imageNetwork = (ImageView) findViewById(R.id.activity_container_image_network);
        imageNetworkOffline = (ImageView) findViewById(R.id.activity_container_image_network_off);
        imageLogin = (ImageView) findViewById(R.id.activity_container_image_login);

        LinearLayout headerLayout = (LinearLayout) navigationView.getHeaderView(0);

        imageViewUser = headerLayout.findViewById(R.id.imageViewUser);
        imageViewUser.setOnClickListener(containerOnClickListener);


        textViewUserName = headerLayout.findViewById(R.id.text_view_user_name);
        textViewUserName.setOnClickListener(containerOnClickListener);
        textViewUserEmail = headerLayout.findViewById(R.id.text_view_user_email);
        textViewUserEmail.setOnClickListener(containerOnClickListener);

        window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorBackgroundSeparatorCenter));
        }

        qrScan = new IntentIntegrator(this);
        qrScan.setCaptureActivity(AnyOrientationCaptureActivity.class);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        qrScan.setPrompt("Сканируйте ШК");
        qrScan.setOrientationLocked(true);
        qrScan.setBeepEnabled(false);

        View view = findViewById(R.id.activity_container_drawer_layout);

        listView = (ListView) findViewById(R.id.activity_container_listView);
        textViewContainer = (TextView) findViewById(R.id.activity_container_textview_container_value);
        textViewCounter = (TextView) findViewById(R.id.activity_container_textview_counter);
        editTextContainer = (EditText) findViewById(R.id.activity_container_edittext_container_value);
        imageButtonMenu = (ImageButton) findViewById(R.id.activity_container_image_button_menu);
        progressBarHTTPService = (ProgressBar) findViewById(R.id.http_service_progress);
        progressBarZayavkaTEP = (ProgressBar) findViewById(R.id.zayavkaTEP_progress);
        linearLayoutCamera = (LinearLayout) findViewById(R.id.activity_container_layout_camera);

        //TODO переделать. убрать методы depricated
        onlineSwitch = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_online));
        offlineSwitch = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_offline));

//        onlineSwitch.setHintTextColor(getResources().getColor(R.color.colorTextEnable));
//        onlineSwitch.setTextColor(getResources().getColor(R.color.colorTextEnable));

        onlineSwitch.setOnCheckedChangeListener(containerOnCheckedChangeListener);
        offlineSwitch.setOnCheckedChangeListener(containerOnCheckedChangeListener);

        editTextContainer.setOnKeyListener(containerOnKeyListener);
        textViewContainer.setOnClickListener(containerOnClickListener);
        imageButtonMenu.setOnClickListener(containerOnClickListener);
        linearLayoutCamera.setOnTouchListener(containerOnTouchListener);
        //containerPropertiesSettings.setOnSettingsContainerRedoListener(containerOnSettingsContainerRedoListener);

        lastBarcode = MySharedPref.loadBarcode(context);
        container = MySharedPref.loadContainer(context);
        if (container == null){
            container = new Container();
            container.setNoData();
        }
        zayavkaTEP = null;

        ZayavkaTEPList zayavkaTEPList = ZayavkaTEPList.getInstance();
        zayavkaTEPListAdapter = new ZayavkaTEPListAdapter();
        zayavkaTEPListAdapter.setZayavkaTEPList(zayavkaTEPList);

        scanBarcode = "";

        updateView(null);
        updateUserInfo();
        updateMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUserInfo();
        updateMenu();

        // блок необходим для перерисовки экрана при изменении настроек
        if (isMenuDraw){
            isMenuDraw = false;
            showContainer();
        }
/*
        if (containerPropertiesAdapter != null){
            showContainer();
        }
*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        MySharedPref.saveBarcode(context, lastBarcode);
        MySharedPref.saveContainer(context, container);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        isCameraScanComplete = false;
        if (requestCode == REQUEST_CONTAINER_NUMBER) {
            if (resultCode == REQUEST_RESULT_OK) {
                String barcode = data.getStringExtra("Barcode");
                updateView(barcode);

            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, R.string.scan_not_found, Toast.LENGTH_LONG).show();
                    isCameraScanComplete = false;
                } else {
                    String barcode = result.getContents();
                    isCameraScanComplete = true;
                    updateView(barcode);
                }
            }

        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(Gravity.LEFT)){
            drawer.closeDrawers();
            return;
        }

        long currentTimeMillis = System.currentTimeMillis();

        if ( (currentTimeMillis - 1000) > lastTimeBackPressed) {
            lastTimeBackPressed = currentTimeMillis;
            Toast.makeText(context,R.string.scan_exit_message, Toast.LENGTH_SHORT).show();
            return;
        }

        finish();
        System.exit(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                startScan();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);

    }

    void updateMenu(){
        //invalidateOptionsMenu();
        menu.removeGroup(R.id.container_drawer_group_zayavkaTEP);

        if (zayavkaTEPListAdapter.getCount() > 0) {
            menu.setGroupVisible(R.id.container_drawer_group_zayavkaTEP, true);
            MenuItem itemZayavkaTEP = menu.add(R.id.container_drawer_group_zayavkaTEP, R.id.container_group_zayavkaTEP_item, 4, R.string.zayavkiTEP);
            itemZayavkaTEP.setCheckable(false);
            itemZayavkaTEP.setEnabled(false);

            for (int i = 0; i < zayavkaTEPListAdapter.getCount(); i++) {
                ZayavkaTEP zayavkaTEP = zayavkaTEPListAdapter.getZayavka(i);
//                itemZayavkaTEP = menu.add(R.id.container_drawer_group_zayavkaTEP, Menu.NONE, 5, zayavkaTEP.getNumber());
                itemZayavkaTEP = menu.add(R.id.container_drawer_group_zayavkaTEP, i, 5, zayavkaTEP.getNumber());
                itemZayavkaTEP.setIcon(R.mipmap.ic_close_circle_outline_red_36dp);
                TextView countView = new TextView(context);
                itemZayavkaTEP.setActionView(countView);
                itemZayavkaTEP.setOnMenuItemClickListener(containerOnMenuItemClickListener);
                countView.setText(Integer.toString(zayavkaTEP.getCountNumbers()));
                countView.setGravity(Gravity.CENTER_VERTICAL);
                countView.setTypeface(null, Typeface.BOLD);
                countView.setTextColor(getResources().getColor(R.color.colorTextEnable));
            }
        } else {
            menu.setGroupVisible(R.id.container_drawer_group_zayavkaTEP, false);
        }

    }

    private void updateView(String barcode){
        // вызывается при получении ШК (ввод вручную / выбор из истории / сканирование сканером / сканирование камерой)
        // далее необходимо отправить запрос на сервер и получить по нему информацию

        if (barcode == null) {
            showContainer();
            //showProgress(false);
            return;
        }

        showProgress(true);
        NumberListLast.getInstance().setCurrentNumber(barcode);
        //if (! barcode.equals(lastBarcode)) {

            boolean resultOK = false;

            lastBarcode = barcode.toString();
            setHeaderContainer();

            if (mobileSkladSettings.isModeOffline()){
                container = zayavkaTEPListAdapter.findContainer(barcode);
//                NumberListLast.getInstance().setCurrentNumber(barcode);

                if (container == null){
                    resultOK = false;
                    container = new Container();
                    container.setNoDataOffline();
                } else {
                    resultOK = true;
                }

                //showContainer();
            }

            if (!resultOK) {

                if (mobileSkladSettings.isModeOnline()){
                    // запуск фонового задания получить на сервере информацию о контейнере
                    new ProgressContainerTask().execute(barcode);
                    // запуск фонового задания получить на сервере информацию о всех контейнерах заявки ТЭП
                    progressZayavkaTask = new ProgressZayavkaTask();
                    progressZayavkaTask.execute("");
                } else {
                    // включен режим ТОЛЬКО оффлайн. раз не нашли - выведем сообщение.
                    Toast toast = Toast.makeText(context, R.string.offline_container_not_found, Toast.LENGTH_LONG );
//                    toast.setGravity(Gravity.CENTER, toast.getXOffset(), toast.getYOffset());
                    toast.show();

                    // в режиме только offline необходимо отобразить информацию по контейнеру
                    showContainer();
                    //showProgress(false);
                }
            } else {
                // если нашли в оффлайн - отобразим информацию по контейнеру
                showContainer();
                //showProgress(false);
            }

            //showContainer();


//        } else {
//            NumberListLast.getInstance().setCurrentNumber(barcode);
//            setHeaderContainer();
//            showContainer();
//        }
    }

    private void updateUserInfo(){
        MobileSkladUser mobileSkladUser = mobileSkladSettings.getCurrentUser();
        if (mobileSkladUser == null){
            return;
        }

        textViewUserEmail.setText(mobileSkladUser.getEmail());
        textViewUserName.setText(mobileSkladUser.getName());

        onlineSwitch.setEnabled(mobileSkladSettings.isAuthorized());
        offlineSwitch.setEnabled(mobileSkladSettings.isAuthorized());

        onlineSwitch.setChecked(mobileSkladSettings.isModeOnline());
        offlineSwitch.setChecked(mobileSkladSettings.isModeOffline());

        if (mobileSkladSettings.isAuthorized()) {
           imageLogin.setImageResource(R.mipmap.account_check_24);
        } else {
            imageLogin.setImageResource(R.mipmap.account_off_24);
        }

        if (mobileSkladSettings.isModeOnline()){
            imageNetwork.setImageResource(R.mipmap.access_point_network_24);
        } else {
            imageNetwork.setImageResource(R.mipmap.access_point_network_24_disable);
        }

        if (mobileSkladSettings.isModeOffline()){
            imageNetworkOffline.setImageResource(R.mipmap.access_point_network_off_24);
        } else {
            imageNetworkOffline.setImageResource(R.mipmap.access_point_network_off_24_disable);
        }

    }

    private void showContainer(){

        //containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
        containerPropertiesAdapter = new ContainerPropertiesAdapter(context, container, containerPropertiesSettings);
        listView.setAdapter(new ContainerPropertiesAdapter(context, container, containerPropertiesSettings));

        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        showProgress(false);

        if (isCameraScanComplete && mobileSkladSettings.getCounterEnable()) {
            startAutoScan();
        }

    }

    private void startAutoScan(){

        counterTask = new CounterTask();
        counterTask.execute("");
        isCameraScanComplete = false;
    }

    private void stopAutoScan(){
        if (counterTask != null && counterTask.getStatus()==AsyncTask.Status.RUNNING){
            counterTask.cancel(true);
        }
        textViewCounter.setText("");
    }

    private void setHeaderContainer(){
        // обновление заголовка контейнера (отображение ШК)
        textViewContainer.setText(lastBarcode);
    }

    public void OnClickAvatar(View view) {

//        Intent intentLogin = new Intent(context, LoginActivity.class);
//        startActivity(intentLogin);

        //new LoginTask().execute("login");

    }

    private void showProgressContainers(boolean show, int count, String message){

        if (show) {
            // устанавливаем заголовки
            zayavkaProgressDialog.setTitle("Загрузка данных");
            zayavkaProgressDialog.setMessage("Заявка ТЭП № " + message);
            // устанавливаем максимум
            zayavkaProgressDialog.setMax(count);
            // включаем анимацию ожидания
            zayavkaProgressDialog.setIndeterminate(true);
            zayavkaProgressDialog.show();
        } else {
            zayavkaProgressDialog.dismiss();
        }
    }

    private void setHeaderProgressContainers(String title, String message, int count){
        // устанавливаем заголовки
        zayavkaProgressDialog.setTitle("Загрузка данных");
        zayavkaProgressDialog.setMessage("Заявка ТЭП № " + message);
        // устанавливаем максимум
        zayavkaProgressDialog.setMax(count);
    }

    private void incrementProgressContainers(int value){

        if (zayavkaProgressDialog.isIndeterminate()) {
            // выключаем анимацию ожидания
            zayavkaProgressDialog.setIndeterminate(false);
        }
        if (value < zayavkaProgressDialog.getMax()) {
            // увеличиваем значения индикаторов
            zayavkaProgressDialog.setProgress(value);
            zayavkaProgressDialog.incrementSecondaryProgressBy(75);
        } else {
            zayavkaProgressDialog.dismiss();
        }
    }

    private class ProgressContainerTask extends AsyncTask<String, Void, EnumBarcodeType> {

        private EnumBarcodeType barcodeType;
        private String containerInfoString;
        String barcode = "";

        public ProgressContainerTask() {

            container.setNoData();
            zayavkaTEP = null;
            barcodeType = EnumBarcodeType.ERROR;
            containerInfoString = "";
            zayavkaTEP = new ZayavkaTEP();
        }

        public EnumBarcodeType getBarcodeType(){
            return barcodeType;
        }

        @Override
        protected EnumBarcodeType doInBackground(String... strings) {

            String jsonText;
            EnumBarcodeType barcodeType;
            barcode = strings[0];

            try {
                barcodeType = getResponse(barcode);
                if (barcodeType == EnumBarcodeType.ZAYAVKA){
                    // загрузим информацию по заявке ТЭП
                    //showProgressContainers(true);

                }
            } catch (IOException ex){
                barcodeType = EnumBarcodeType.ERROR;
            }
            return barcodeType;
        }

        @Override
        protected void onPostExecute(EnumBarcodeType result) {
            super.onPostExecute(result);

            switch (result) {
                case CONTAINER:
                    ParserHttpResponse parser = new ParserHttpResponse();
                    parser.setContainerInfoString(containerInfoString);
                    container = parser.getContainerInfo();
                    String errorJson = parser.getErrorString();
                    if (errorJson != "") {
                        Toast.makeText(context, errorJson, Toast.LENGTH_LONG).show();
                        container.setNoData();
                        lastBarcode = "";
                    }

                    NumberListLast.getInstance().setCurrentNumber(barcode);

                    showContainer();
                    //showProgress(false);

                    break;

                case ZAYAVKA:
                    // скроем бублик и отобразим прогресс загрузки ЗаявкиТЭП
                    showProgress(false);
                    if (!mobileSkladSettings.isAuthorized()){
                        // если не авторизован - сообщение и больше ничего не делаем
                        Toast.makeText(context, R.string.error_zayavkaTEP_autorization, Toast.LENGTH_LONG).show();
                        updateUserInfo();
                    } else {
                        if ((zayavkaTEP != null) && (zayavkaTEP.getZayavkaTEP_header().count != 0)) {
                            showProgressContainers(true, 0, zayavkaTEP.getNumber());
                        } else {
                            // здесь сообщение об ошибке
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                    break;

                case ERROR:
                    container.setError(errorMessage);
                    showContainer();
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    lastBarcode = "";
                    break;

                case ERROR_CONNECTION:
                    container.setNoConnect();
                    showContainer();
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    break;

                case ERROR_TIMEOUT:
                    errorMessage = "Превышено время ожидания (задается в настройках)";
                    container.setTimeout();
                    showContainer();
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

        private EnumBarcodeType getResponse(String barcode) throws IOException{

            LogisticHttpService logisticHttpService = new LogisticHttpService();
            barcodeType = logisticHttpService.getBarcodeType(barcode);

            switch (barcodeType){

                case ERROR_CONNECTION:
                    // TODO вынести в strings
                    errorMessage = "Проверьте подключение к Internet";
                    container.setNoConnect();
                    break;
                case ERROR:
                    errorMessage = logisticHttpService.getErrorString();
                    container.setNoData();
                    break;
                case CONTAINER:

                    if (logisticHttpService.getInfoFromServer(barcode)) {
                        containerInfoString = logisticHttpService.getContainerJson();
                    } else {
                        containerInfoString = "";

                        ErrorType errorType = logisticHttpService.getErrorType();

                        switch (errorType){
                            case TIMEOUT:
                                barcodeType = EnumBarcodeType.ERROR_TIMEOUT;
                                container.setTimeout();
                                break;
                            case NO_CONNECTION:
                                barcodeType = EnumBarcodeType.ERROR_CONNECTION;
                                errorMessage = "Превышено время ожидания (задается в настройках)";
                                container.setNoConnect();
                                break;
                            case UNKNOWN_ERROR:
                                barcodeType = EnumBarcodeType.ERROR_CONNECTION;
                                container.setNoData();
                                break;
                        }
                    }

                    break;
                case ZAYAVKA:
                    if (mobileSkladSettings.isAuthorized()) {
                        zayavkaTEP = logisticHttpService.getZayavkaTep(barcode, mobileSkladSettings.getCurrentUser().getToken());
                        errorMessage = logisticHttpService.getErrorString();
                    } else {
                        zayavkaTEP = null;
                        //errorMessage = "";
                    }
                    break;
                default:
                    container.setNoData();
                    return null;
            }
            return barcodeType;
        }
    }

    private class ProgressZayavkaTask extends AsyncTask<String, Integer, Boolean> {
        // класс, который загружает информацию по заявке ТЭП

        boolean overflowMaxZayavkaTEP = false;
        boolean overwriteZayavkaTEP = false;

        public ProgressZayavkaTask() {
            overflowMaxZayavkaTEP = false;
            overwriteZayavkaTEP = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == 1){
                setHeaderProgressContainers("Загрузка данных", zayavkaTEP.getNumber(), zayavkaTEP.getZayavkaTEP_header().count);
            }
            incrementProgressContainers(values[0]);
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            if (zayavkaTEP == null
                    || zayavkaTEP.getZayavkaTEP_header().count == 0) {
                return false;
            } else if (! mobileSkladSettings.isAuthorized()) {
                return false;
            } else if (zayavkaTEPListAdapter.getCount() >= ZayavkaTEPListAdapter.MAX_SIZE){
                overflowMaxZayavkaTEP = true;
                return false;
            } else {

                LogisticHttpService logisticHttpService = new LogisticHttpService();

                String containerInfoString = "";

                int count = 0;
                Container containerZayavkaTEP;
                for (String barcode : zayavkaTEP.getZayavkaTEP_header().numbers){
                    if (isCancelled()){
                        break;
                    }
                    try {

                        if (logisticHttpService.getInfoFromServer(barcode)) {
                            containerInfoString = logisticHttpService.getContainerJson();
                            ParserHttpResponse parser = new ParserHttpResponse();
                            parser.setContainerInfoString(containerInfoString);
                            containerZayavkaTEP = parser.getContainerInfo();
                            String errorJson = parser.getErrorString();
                            if (errorJson != "") {
                                Toast.makeText(context, errorJson, Toast.LENGTH_LONG).show();
                                containerZayavkaTEP.setNoData();
                                lastBarcode = "";
                            }
                            zayavkaTEP.addContainer(containerZayavkaTEP);
                        } else {
                            containerInfoString = "";
                        }
                        publishProgress(++count);
                    } catch (IOException ioe){
                        // ХЗ
                    }
                }
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                // добавим заявку ТЭП в список загруженных
                zayavkaTEPListAdapter.addZayavkaTEP(zayavkaTEP);
                MySharedPref.saveZayavkaTEPList(context);
                updateMenu();
            } else {

                if (overflowMaxZayavkaTEP){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Превышено количество Заявок ТЭП");
                    builder.setMessage("Одновременно разрешено загружать не более " + ZayavkaTEPListAdapter.MAX_SIZE + " Заявок ТЭП. \n" +
                            "Сначала необходимо удалить одну из загруженных заявок и повторить сканирование Заявки ТЭП.");
                    builder.setIcon(R.mipmap.ic_close_circle_outline_red_48dp);
                    builder.setCancelable(false);
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
            showProgressContainers(false, 0, "");

        }

        private boolean getResponse(String barcode) throws IOException{

           return false;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }
    }

    private class CounterTask extends AsyncTask<String, Integer, String >{

        // Таск, который отображает обратный отсчет на панели.

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            textViewCounter.setText(String.valueOf(values[0]));
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                int countertime = mobileSkladSettings.getCounter();
                publishProgress(countertime);

                while (countertime > 0) {

                    sleep(1000);
                    countertime -= 1;
                    publishProgress(countertime);
                }
            } catch (InterruptedException exInterrupter){
                exInterrupter.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textViewCounter.setText("");
            startScan();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);
            int shortAnimTime = 0;

            listView.setVisibility(show ? View.GONE : View.VISIBLE);
            listView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    listView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBarHTTPService.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBarHTTPService.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBarHTTPService.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBarHTTPService.setVisibility(show ? View.VISIBLE : View.GONE);
            listView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showZayavkaProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            try {
                listView.setVisibility(show ? View.GONE : View.VISIBLE);
                listView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                String res = e.getMessage();
                res = res + "22";
            }

            progressBarZayavkaTEP.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBarZayavkaTEP.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBarZayavkaTEP.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBarZayavkaTEP.setVisibility(show ? View.VISIBLE : View.GONE);
            listView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void startScan(){
        qrScan.initiateScan();
    }

    class ContainerOnKeyListener implements View.OnKeyListener{
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (v.getId() == R.id.activity_container_edittext_container_value){
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    if (editTextContainer.length() > 1){
                        //lastBarcode = editTextContainer.getText().toString();
                        String newBarcode = editTextContainer.getText().toString();
                        editTextContainer.setText("");
                        if (getCurrentFocus() != null) {
                            InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        updateView(newBarcode);
                    } else {
                        editTextContainer.setText("");
                    }
                }
            }
            return false;
        }
    }

    class ContainerOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intentLogin;
            switch (view.getId()){
                case R.id.activity_container_textview_container_value:
                    stopAutoScan();
                    startActivityForResult(new Intent(context, NumberContainerActivity.class), REQUEST_CONTAINER_NUMBER);
                    break;
                case R.id.imageViewUser:
                    intentLogin = new Intent(context, LoginActivity.class);
                    startActivity(intentLogin);
                    break;
                case R.id.text_view_user_email:
                    intentLogin = new Intent(context, LoginActivity.class);
                    startActivity(intentLogin);
                    break;
                case R.id.text_view_user_name:
                    intentLogin = new Intent(context, LoginActivity.class);
                    startActivity(intentLogin);
                    break;
                case R.id.activity_container_image_button_menu:
//                    drawer.openDrawer(0);
                    stopAutoScan();
                    isMenuDraw = true;
                    drawer.openDrawer(Gravity.LEFT);
                    break;
                default:
                    break;
            }
        }
    }

    class ContainerOnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getId() == R.id.activity_container_layout_camera && motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                stopAutoScan();
                startScan();
                return false;
            }
            return false;
        }
    }

    class ContainerOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_settings){
//                startActivity(new Intent(this, FamilyActivity.class));
                Intent intent = new Intent(context, SettingsPagersActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_online) {

            } else if (id == R.id.nav_offline) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_container_drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        }
    }

    class ContainerOnCheckedChangeListener implements SwitchCompat.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (compoundButton.getId() == R.id.nav_online){
                mobileSkladSettings.setModeOnline(b);
            }
            if (compoundButton.getId() == R.id.nav_offline){
                mobileSkladSettings.setModeOffline(b);
            }

            if ( !(mobileSkladSettings.isModeOnline() || mobileSkladSettings.isModeOffline()) ){
                mobileSkladSettings.setModeOnline(true);
            }
            updateUserInfo();
            MySharedPref.saveMobileSkladSettings(context);
        }
    }

    class ContainerOnMenuItemClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(final MenuItem menuItem) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Удалить Заявку ТЭП " + menuItem.getTitle() + " ?");
            builder.setMessage("Внимание! После удаления заявки ТЭП все контейнеры, которые содержатся в ней станут недоступными для сканирования в режиме offline.");
            builder.setIcon(R.mipmap.ic_close_circle_outline_red_48dp);
            builder.setCancelable(false);

            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    zayavkaTEPListAdapter.deleteZayavkaTEP(menuItem.getItemId());
                    MySharedPref.saveZayavkaTEPList(context);
                    updateMenu();
                    dialogInterface.cancel();
                }
            });

            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return false;
        }
    }

    class ContainerOnCanceled implements Dialog.OnCancelListener{
        @Override
        public void onCancel(DialogInterface dialogInterface) {

            // Перед отменой загрузки информации по Заявке ТЭП переспросим на всякий случай.
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Отменить загрузку Заявки ТЭП?");
            builder.setMessage("");
            builder.setIcon(R.mipmap.ic_close_circle_outline_red_48dp);
            builder.setCancelable(false);

            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Загрузка отменена", Toast.LENGTH_SHORT).show();
                if (progressZayavkaTask != null && progressZayavkaTask.getStatus() == AsyncTask.Status.RUNNING){
                    progressZayavkaTask.cancel(true);
                    //progressZayavkaTask.can
                }
                dialogInterface.cancel();
                }
            });

            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    zayavkaProgressDialog.show();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

//            Toast.makeText(context, "=========", Toast.LENGTH_SHORT).show();
//            if (progressZayavkaTask != null && progressZayavkaTask.getStatus() == AsyncTask.Status.RUNNING){
//                progressZayavkaTask.cancel(true);
//                //progressZayavkaTask.can
//            }
        }
    }

    class ContainerOnSettingsContainerRedoListener implements OnSettingsContainerRedoListener{
        @Override
        public void onSettingsContainerRedo() {
            // блок необходим для перерисовки экрана при изменении настроек
            if (containerPropertiesAdapter != null){
                showContainer();
            }
        }
    }
}
