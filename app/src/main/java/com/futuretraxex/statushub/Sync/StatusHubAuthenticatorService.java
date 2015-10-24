package com.futuretraxex.statushub.Sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by hudelabs on 10/24/2015.
 */
public class StatusHubAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private StatusHubAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new StatusHubAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}