package com.omegaauto.shurik.mobilesklad.HTTPservices;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;
import com.omegaauto.shurik.mobilesklad.utils.Const;
import com.omegaauto.shurik.mobilesklad.utils.EnumBarcodeType;
import com.omegaauto.shurik.mobilesklad.utils.ErrorType;
import com.omegaauto.shurik.mobilesklad.zayavkaTEP.ZayavkaTEP;
import com.omegaauto.shurik.mobilesklad.zayavkaTEP.ZayavkaTEP_header;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Thread.sleep;

public class LogisticHttpService {

    MobileSkladSettings mobileSkladSettings;
    String errorString = "";
    ErrorType errorType = ErrorType.NO_ERROR;

    TokenError tokenError = null;
    ZayavkaTEP_header zayavkaTEP_header = null;
    String containerJson = "";


    private final OkHttpClient client = new OkHttpClient();

    public LogisticHttpService() {
        containerJson = "";
        mobileSkladSettings = MobileSkladSettings.getInstance();
    }

    public EnumBarcodeType getBarcodeType(String barcode){

        BarcodeClass barcodeClass = new BarcodeClass();
        barcodeClass.barcode = barcode;

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String url = Const.URL_HTTP_SERVICE_1C_LOGISTIC_BARCODE_TYPE;

        Gson gson   = new Gson();
        String json = gson.toJson(barcodeClass);
        gson = null;

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();

            String jsonText = response.body().string();

            // данные, которые вернул http-сервис преобразовываем в значение класса BarcodeTypeError
            gson = new Gson();
            BarcodeTypeError result = gson.fromJson(jsonText, BarcodeTypeError.class);
            errorString = result.error;
            return result.type;

        } catch (ConnectException ce){
            errorString = ce.getMessage();
            tokenError = null;
            return EnumBarcodeType.ERROR_CONNECTION;
        } catch (IOException e) {
            //e.printStackTrace();
            errorString = e.getMessage();
            tokenError = null;
            return EnumBarcodeType.ERROR;
        } catch (Exception ee){
            errorString = ee.getMessage();
            tokenError = null;
            return EnumBarcodeType.ERROR;
        }

    }

    public String getContainerJson(){

        return containerJson;
    }

    public boolean getInfoFromServer(String barcode) throws IOException {

        boolean result = true;
        containerJson = "";
        errorType = ErrorType.NO_ERROR;

        BufferedReader bufferedReader = null;
        //String errorMessage = "";

        try {
            URL url = new URL(Const.URL_HTTP_SERVICE_1C_LOGISTIC + barcode);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setReadTimeout(mobileSkladSettings.getTimeout() * 1000); // 2 секунды
            connection.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            containerJson = stringBuffer.toString();
            errorType = ErrorType.NO_ERROR;
        } catch (SocketTimeoutException exTimeout) {
            result = false;
            errorString = "Превышено время ожидания...";
            errorType = ErrorType.TIMEOUT;
        } catch (UnknownHostException exUnknownHostException){
            result = false;
            errorString = "Проверьте подключение к Интернет";
            errorType = ErrorType.NO_CONNECTION;
        } catch (IOException ex) {
            result = false;
            errorString = ex.getMessage();
            errorType = ErrorType.UNKNOWN_ERROR;
        } catch (Exception ee){
            result = false;
            errorString = ee.getMessage();
            errorType = ErrorType.UNKNOWN_ERROR;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        return result;
    }

    public ErrorType getErrorType(){
        return errorType;
    }

    // отправляет на сервер номер Заявки ТЭП + токен.
    // получает json со всеми контейнерами этой заявки ТЭП или пустую строку, если заявка не найдена на сервере.
    public ZayavkaTEP getZayavkaTep(String barcode, String token){

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String url = Const.URL_HTTP_SERVICE_1C_LOGISTIC_GET_ZAYAVKA;

        BarcodeTokenClass barcodeToken = new BarcodeTokenClass();
        barcodeToken.barcode = barcode;
        barcodeToken.token = token;

        Gson gson   = new Gson();
        String json = gson.toJson(barcodeToken);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();

            if (response.code() == 200) {
                // если http-запрос закончился удачно...

                String jsonText = response.body().string();

                // данные, которые вернул http-сервис преобразовываем в экземпляр класса ZayavkaTEP_header
                zayavkaTEP_header = gson.fromJson(jsonText, ZayavkaTEP_header.class);
                errorString = zayavkaTEP_header.error;

                if (zayavkaTEP_header.result) {
                    ZayavkaTEP zayavkaTEP = new ZayavkaTEP();
                    zayavkaTEP.setNumber(barcode);
                    //zayavkaTEP.setCountNumbers(zayavkaTEP_header.count);
                    zayavkaTEP.setZayavkaTEP_header(zayavkaTEP_header);
    //                for (String numberContainer:zayavkaTEP_header.numbers) {
    //                    String containerJSON = getContainerJson(numberContainer);
    //
    //                }

                    return zayavkaTEP;
                } else {
                    if (zayavkaTEP_header.errorType.equals("ERROR_TOKEN")){
                        // не верный токен. необходима повторная авторизация
                        mobileSkladSettings.getCurrentUser().setDefault();
                        mobileSkladSettings.setAuthorized(false);
                    }
    //                return null;
                    return new ZayavkaTEP();
                }
            } else {
                // ...или неудачно
                zayavkaTEP_header = null;
                errorString = response.message();
                return new ZayavkaTEP();
            }

        } catch (IOException e) {
            //e.printStackTrace();
            errorString = e.getMessage();
            zayavkaTEP_header = null;
            //return null;
            return new ZayavkaTEP();
        } catch (Exception ee){
            errorString = ee.getMessage();
            zayavkaTEP_header = null;
            //return null;
            return new ZayavkaTEP();
        }
    }

    public boolean putContainerCrossDockFeedback(String barcode, String token){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String url = Const.URL_HTTP_SERVICE_1C_LOGISTIC_PUT_CONTAINER;

        BarcodeTokenClass barcodeToken = new BarcodeTokenClass();
        barcodeToken.barcode = barcode;
        barcodeToken.token = token;

        Gson gson   = new Gson();
        String json = gson.toJson(barcodeToken);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        try {
            Response response = client.newCall(request).execute();

            if (response.code() == 200) {

                String jsonText = response.body().string();

                // данные, которые вернул http-сервис преобразовываем в экземпляр класса ContainerCrossDockFeedbackClass
                ContainerCrossDockFeedbackClass containerCrossDockFeedback = gson.fromJson(jsonText, ContainerCrossDockFeedbackClass.class);
                errorString = containerCrossDockFeedback.error;

                if (containerCrossDockFeedback.result) {
                    return true;
                } else {
                    if (containerCrossDockFeedback.errorType.equals("ERROR_TOKEN")) {
                        // не верный токен. необходима повторная авторизация
                        mobileSkladSettings.getCurrentUser().setDefault();
                        mobileSkladSettings.setAuthorized(false);
                    }
                    return false;
                }
            } else {
                errorString = response.message();
                return false;
            }

        } catch (IOException e) {
            //e.printStackTrace();
            errorString = e.getMessage();
            return false;
        } catch (Exception ee){
            errorString = ee.getMessage();
            return false;
        }
    }

    // попытка авторизации по email и паролю. если успешная - возвращаем истину, иначе - ложь
    // далее необходимо вызвать метод getTokenError(), который возвращает токен пользователя и возможные ошибки.
    // его необходимо сохранить в настройках и использовать при любых обращениях к серверу.
    public boolean userLogin(String login, String password){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String url = Const.URL_HTTP_SERVICE_1C_LOGISTIC_LOGIN;

        LoginPassword loginPassword = new LoginPassword();
        loginPassword.login = login;
//        loginPassword.password = hashPassword(password);
        loginPassword.password = password;

        Gson gson   = new Gson();
        String json = gson.toJson(loginPassword);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();

            String jsonText = response.body().string();

            // данные, которые вернул http-сервис преобразовываем в экземпляр класса TokenError
            tokenError = gson.fromJson(jsonText, TokenError.class);
            errorString = tokenError.error;
            return tokenError.result;

        } catch (IOException e) {
            //e.printStackTrace();
            errorString = e.getMessage();
            tokenError = null;
            return false;
        } catch (Exception ee){
            errorString = ee.getMessage();
            tokenError = null;
            return false;
        }

    }

    public TokenError getTokenError(){
        return tokenError;
    }

    // проверка, что токен, хранимый в настройках привязан к активному пользователю на сервере.
    public boolean isTokenCorrect(String token){
        return false;
    }

    public String getErrorString() {
        return errorString;
    }

    private class LoginPassword{
        String login;
        String password;
    }

    private class BarcodeClass{
        String barcode = "";
    }

    private class BarcodeTokenClass{
        String barcode = "";
        String token = "";
    }

    private class ContainerCrossDockFeedbackClass{
        public Boolean result;
        public String error;
        public String errorType;
    }
}
