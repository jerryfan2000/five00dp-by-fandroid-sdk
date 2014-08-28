package com.nyuen.five00dp.account;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountAuthenticatorService extends Service {

    private static AccountAuthenticator sAccountAuthenticator;
    
    public AccountAuthenticatorService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder ret = null;
        if (intent.getAction().equals(AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
            ret = getAuthenticator().getIBinder();
        }
        return ret;
    }
    
    private AccountAuthenticator getAuthenticator() {
        if (sAccountAuthenticator == null) {
            sAccountAuthenticator = new AccountAuthenticator(this);
        }
        return sAccountAuthenticator;
    }
}
