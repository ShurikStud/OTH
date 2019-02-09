package com.omegaauto.shurik.mobilesklad;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.omegaauto.shurik.mobilesklad.HTTPservices.LogisticHttpService;
import com.omegaauto.shurik.mobilesklad.HTTPservices.TokenError;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;
import com.omegaauto.shurik.mobilesklad.user.MobileSkladUser;
import com.omegaauto.shurik.mobilesklad.utils.MySharedPref;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.Thread.sleep;

/**
 * A login screen that offers login via email/password.
 */
//public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    Window window;
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUserName;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignOutButton;
    private Button mEmailSignInButton;

    private LoginOnClickListener loginOnClickListener;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserName = (AutoCompleteTextView) findViewById(R.id.activity_login_user_name);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        loginOnClickListener = new LoginOnClickListener();

        mEmailSignOutButton = (Button) findViewById(R.id.email_sign_out_button);
        mEmailSignOutButton.setOnClickListener(loginOnClickListener);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(loginOnClickListener);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorBackgroundSeparatorCenter));
        }

        updateView();

    }

//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null, this);
//    }

//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }

//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // вопрос о продолжении авторизации, если пользователь уже авторизован
        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
        if(mobileSkladSettings.isAuthorized()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Авторизировать нового пользователя?");
            builder.setMessage("Внимание! Перед попыткой авторизоваться прошлая авторизация будет отменена. Все данные оффлайн будут очищены!");
            builder.setIcon(R.mipmap.ic_account_convert);
            builder.setCancelable(false);

            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
                    MobileSkladUser currentUser = mobileSkladSettings.getCurrentUser();
                    mobileSkladSettings.setAuthorized(false);
                    attemptLoginContinue();
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
        } else {
            attemptLoginContinue();
        }
    }

    private void attemptLoginContinue(){
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mUserName.setText("");
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            String passwordHASH = password2hashSHA256(password);

            mAuthTask = new UserLoginTask(email, passwordHASH);
            mAuthTask.execute((Void) null);
        }
    }

    static String password2hashSHA256(String password){

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        messageDigest.reset();
        byte[] digest = messageDigest.digest(password.getBytes());

        return String.format("%0" + (digest.length * 2) + "X", new BigInteger(1, digest));

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        String errorString = "";
        TokenError tokenError = null;
        //TokenError tokenError = null;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            tokenError = new TokenError();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            //String jsonText = "";

            boolean result = false;

            try {
                // Simulate network access.
                Thread.sleep(500);
                tokenError = getResponse();
                if (tokenError == null) {
                    result = false;
                } else {
                    result = tokenError.result;
                }
            }catch (IOException ioe){
                result = false;
                errorString = ioe.getLocalizedMessage();
            } catch (InterruptedException e) {
                result = false;
                errorString = e.getLocalizedMessage();
            }

            return result;

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

//            Gson gson = new Gson();
//            tokenError = gson.fromJson(jsonText, TokenError.class);

//            errorString = tokenError.error;
//            return tokenError.result;

            // TODO: register the new account here. Регистрацию не делаем - всегда только на сервере

//            result = true;
//            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {

                if (tokenError == null) {
                    Toast.makeText(context, R.string.error_autorisation, Toast.LENGTH_LONG).show();
                    return;
                }

                // проверка ошибка логин
                if (tokenError.error_email){
                    mEmailView.setError(getString(R.string.error_incorrect_email));
                    mEmailView.requestFocus();
                }

                // проверка ошибка пароль
                if (tokenError.error_password){
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }

//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private TokenError getResponse() throws IOException {

            LogisticHttpService logisticHttpService = new LogisticHttpService();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            return logisticHttpService.login();
            boolean result = logisticHttpService.userLogin(mEmail, mPassword);
            TokenError tokenError = logisticHttpService.getTokenError();

            MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
            MobileSkladUser currentUser = mobileSkladSettings.getCurrentUser();

            mobileSkladSettings.setAuthorized(result);
            if (result) {
                currentUser.setToken(tokenError.token);
                currentUser.setEmail(mEmail);
                currentUser.setName(tokenError.name);
                currentUser.setPasswordHash(mPassword);

            } else {
                currentUser.setDefault();;
            }
            MySharedPref.saveMobileSkladSettings(context);

            return tokenError;
        }

    }

    void showText(String textString){
        Toast.makeText(context, textString, Toast.LENGTH_LONG).show();
    }

    private void updateView(){
        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
        MobileSkladUser currentUser = mobileSkladSettings.getCurrentUser();

        if (mobileSkladSettings.isAuthorized()) {
            mEmailView.setText(currentUser.getEmail());
            mUserName.setText(currentUser.getName());
            mUserName.setVisibility(View.VISIBLE);
            mEmailSignOutButton.setVisibility(View.VISIBLE);
        } else {
            mEmailView.setText("");
            mUserName.setText("");
            mUserName.setVisibility(View.GONE);
            mEmailSignOutButton.setVisibility(View.GONE);
        }
        mPasswordView.setText("");
        mPasswordView.requestFocus();
    }

    class LoginOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.email_sign_in_button:
                    attemptLogin();
                    break;
                case R.id.email_sign_out_button:

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Отменить авторизацию?");
                    builder.setMessage("Внимание! После отмены авторизации очистится список загруженных заявок ТЭП. Режим оффлайн станет недоступным.");
                    builder.setIcon(R.mipmap.ic_close_circle_outline_red_48dp);
                    builder.setCancelable(false);

                    builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
                            MobileSkladUser currentUser = mobileSkladSettings.getCurrentUser();
                            mobileSkladSettings.setAuthorized(false);
                            MySharedPref.saveMobileSkladSettings(context);
                            MySharedPref.saveZayavkaTEPList(context);
                            updateView();
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
                    break;
                default:
            }
        }
    }
}

