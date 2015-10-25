package com.futuretraxex.statushub.Sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Created by paresh on 10/19/2015.
 */

public class StatusHubSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static StatusHubSyncAdapter sStatusHubSyncAdapter = null;

    @Override
    public void onCreate() {
        Logger.init();
        Logger.w("StatusHub Sync Service");
        synchronized (sSyncAdapterLock) {
            if (sStatusHubSyncAdapter == null) {
                sStatusHubSyncAdapter = new StatusHubSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sStatusHubSyncAdapter.getSyncAdapterBinder();
    }
}