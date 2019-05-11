package com.omegaauto.shurik.mobilesklad.settings;

import com.omegaauto.shurik.mobilesklad.user.MobileSkladUser;
import com.omegaauto.shurik.mobilesklad.utils.MySharedPref;
import com.omegaauto.shurik.mobilesklad.zayavkaTEP.ZayavkaTEPList;

public final class MobileSkladSettings {

    // При изменении реквизитного состава необходимо изменить (увеличить значение) версию.
    private static final int PROPERTIES_VERSION = 4;

    private static MobileSkladSettings instance;

    private int counter; // время в секундах отображения информации об контейнере при сканировании через камеру телефона
    private Boolean isCounterEnable = false;
    private MobileSkladFontSize fontSize;
    private int timeout = 0;
    private int quantityZayavkaTEP = 4; // количество загружаемых заявок ТЭП
    private MobileSkladUser currentUser;

    boolean authorized; // если истина - пользователь авторизирован, если ложь - не авторизирован.
    boolean modeOnline = true; // режим работы online
    boolean modeOffline = false; // режим работы offline
    boolean modeCrossDockFeedback = false; // отправлять обратную связь при сканировании на КД.

    int current_version;

    //================ МЕТОДЫ ==================

    private MobileSkladSettings(){
        currentUser = new MobileSkladUser();
    }

    static public MobileSkladSettings getInstance(){

        if (instance == null){
            instance = new MobileSkladSettings();
            instance.setCurrentVersion();
        }

        return instance;
    }

    public int getCounter() {

        return counter;
    }

    public void setCounter(int counter) {
        if (counter < 0) {
            this.counter = 0;
        }else {
            this.counter = counter;
        }
    }

    public Boolean getCounterEnable() {
        if (isCounterEnable == null){
            return false;
        } else {
            return isCounterEnable;
        }
    }

    public void setCounterEnable(Boolean counterEnable) {
        isCounterEnable = counterEnable;
    }

    public void initDefault(){
        isCounterEnable = false;
        counter = 0;
        timeout = 10;
        fontSize = ListFontSizes.getInstance().get("NORMAL");
        quantityZayavkaTEP = 4;
        currentUser.setDefault();
        authorized = false;
        modeOnline = true;
        modeOffline = false;
        modeCrossDockFeedback = false;
        setCurrentVersion();
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public MobileSkladFontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(MobileSkladFontSize fontSize) {
        this.fontSize = fontSize;
    }

    public void setProperties(MobileSkladSettings input){
        if (input.current_version != current_version) {
            initDefault();
            return;
        }
        setCounter(input.getCounter());
        setCounterEnable(input.getCounterEnable());
        setTimeout(input.getTimeout());
        if (input.getFontSize() == null) {
            setFontSize(ListFontSizes.getInstance().getDefault());
        } else {
            setFontSize(ListFontSizes.getInstance().get(input.getFontSize().getId()));
        }
        MobileSkladUser tempCurrentUser = input.getCurrentUser();
        currentUser.setToken(tempCurrentUser.getToken());
        currentUser.setPasswordHash(tempCurrentUser.getPasswordHash());
        currentUser.setEmail(tempCurrentUser.getEmail());
        currentUser.setName(tempCurrentUser.getName());

        setAuthorized(input.isAuthorized());
        setModeOnline(input.isModeOnline());
        setModeOffline(input.isModeOffline());
        setModeCrossDockFeedback(input.isModeCrossDockFeedback());
    }

    private void setCurrentVersion(){
        current_version = PROPERTIES_VERSION;
    }

    public MobileSkladUser getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
        if (!authorized){
            setModeOnline(true);
            setModeOffline(false);
            currentUser.setDefault();
            ZayavkaTEPList zayavkaTEPList = ZayavkaTEPList.getInstance();
            zayavkaTEPList.clear();
        }
    }

    public boolean isModeOnline() {
        return modeOnline;
    }

    public void setModeOnline(boolean modeOnline) {
        this.modeOnline = modeOnline;
    }

    public boolean isModeOffline() {
        return modeOffline;
    }

    public void setModeOffline(boolean modeOffline) {
        this.modeOffline = modeOffline;
    }

    public boolean isModeCrossDockFeedback() {
        return modeCrossDockFeedback;
    }

    public void setModeCrossDockFeedback(boolean modeCrossDockFeedback) {
        this.modeCrossDockFeedback = modeCrossDockFeedback;
    }

    public int getQuantityZayavkaTEP() {
        return quantityZayavkaTEP;
    }

    public void setQuantityZayavkaTEP(int quantityZayavkaTEP) {
        this.quantityZayavkaTEP = quantityZayavkaTEP;
    }
}
