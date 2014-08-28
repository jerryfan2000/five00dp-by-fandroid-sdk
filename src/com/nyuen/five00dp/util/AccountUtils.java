package com.nyuen.five00dp.util;

import com.fivehundredpx.api.auth.AccessToken;
import com.nyuen.five00dp.R;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

public class AccountUtils{

    private static final String KEY_TOKEN = "oauth_token";
    private static final String KEY_TOKEN_SECRET = "oauth_token_secret";
    
    public static boolean hasAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getDefaultAccount(context, accountManager);
        return account != null;
    }

    public static void removeAccount(Context context,
            AccountManagerCallback<Boolean> callback, Handler handler) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getDefaultAccount(context, accountManager);
        if (account != null) {
            accountManager.removeAccount(account, callback, handler);
        }
    }

    public static String getUserNickname(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getDefaultAccount(context, accountManager);
        return accountManager.getUserData(account, "nickname");
    }

    public static AccessToken getAccessToken(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getDefaultAccount(context, accountManager);
        String token = accountManager.getUserData(account, KEY_TOKEN);
        String tokenSecret = accountManager.getUserData(account, KEY_TOKEN_SECRET);
        
        return new AccessToken(token, tokenSecret);
    }
    
    public static String getPassword(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getDefaultAccount(context, accountManager);
        return accountManager.getPassword(account);
    }

    public static Account getDefaultAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        return getDefaultAccount(context, accountManager);
    }

    private static Account getDefaultAccount(Context context,
            AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType(context
                .getString(R.string.account_type));
        return accounts.length > 0 ? accounts[0] : null;
    }
}