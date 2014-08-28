package com.nyuen.five00dp;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fivehundredpx.api.FiveHundredException;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.api.tasks.XAuth500pxTask;
import com.nyuen.five00dp.api.FiveHundred;
import com.nyuen.five00dp.util.AccountUtils;
import com.nyuen.five00dp.util.UIUtils;

public class LoginActivity extends AccountAuthenticatorActivity implements XAuth500pxTask.Delegate{
    private static final String TAG = "LoginActivity";
    private static final String KEY_TOKEN = "oauth_token";
    private static final String KEY_TOKEN_SECRET = "oauth_token_secret";

    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (AccountUtils.hasAccount(this)) {
            Toast.makeText(this, "Can only have 1 account", Toast.LENGTH_SHORT).show();
            finish();
        }
        
        initUi();
    }

    private void initUi() {
        View loginPanel = findViewById(R.id.loginPanel);
        TextView tvUsername = (TextView) findViewById(R.id.inputEmail);
        TextView tvPassword = (TextView) findViewById(R.id.inputPassword);
        Button btnLogin = (Button) findViewById(R.id.buttonLogin);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

        tvUsername.setError(null);
        tvPassword.setError(null);

        loginPanel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
    }

    private void onLogin() {
        View loginPanel = findViewById(R.id.loginPanel);
        TextView tvUsername = (TextView) findViewById(R.id.inputEmail);
        TextView tvPassword = (TextView) findViewById(R.id.inputPassword);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

        String username = tvUsername.getText().toString();
        String password = tvPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            tvUsername.setError(getString(R.string.no_name));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            tvPassword.setError(getString(R.string.no_password));
            return;
        }

        loginPanel.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        UIUtils.hideKeyboard(this, progressBar);

        XAuth500pxTask loginTask = new XAuth500pxTask(this);
        loginTask.execute(FiveHundred.CONSUMER_KEY, FiveHundred.CONSUMER_SECRET, username, password);
    }

    @Override
    public void onSuccess(AccessToken result) {
        TextView tvUsername = (TextView) findViewById(R.id.inputEmail);
        TextView tvPassword = (TextView) findViewById(R.id.inputPassword);

        String username = tvUsername.getText().toString();
        String password = tvPassword.getText().toString();

        Bundle userData = new Bundle();
        userData.putString(KEY_TOKEN, result.getToken());
        userData.putString(KEY_TOKEN_SECRET, result.getTokenSecret());
        Log.e(TAG, result.getToken());
                
        Account account = new Account(username, getString(R.string.account_type));
        AccountManager am = AccountManager.get(this);

        if (am.addAccountExplicitly(account, password, userData)) {
            Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            setAccountAuthenticatorResult(bundle);                  
            setResult(RESULT_OK);

            finish();
        }else {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    initUi();
                    Toast.makeText(LoginActivity.this , "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onFail(FiveHundredException e) {
        e.printStackTrace();
        this.runOnUiThread(new Runnable() {
            public void run() {
                initUi();
                Toast.makeText(LoginActivity.this , "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
