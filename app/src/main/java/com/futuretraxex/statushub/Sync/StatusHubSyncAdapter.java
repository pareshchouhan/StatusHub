package com.futuretraxex.statushub.Sync;

/**
 * Created by hudelabs on 10/24/2015.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.futuretraxex.statushub.DataModel.MemberModel;
import com.futuretraxex.statushub.R;
import com.futuretraxex.statushub.Utility.Utility;
import com.futuretraxex.statushub.database.StatusHubContract;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;



/**
 * Created by paresh on 10/19/2015.
 */
public class StatusHubSyncAdapter extends AbstractThreadedSyncAdapter {


    private final String SYNC_LOG_TAG = this.getClass().getName();

    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int STATUSHUB_NOTIFICATION_ID = 3000;




    StatusHubSyncAdapter(Context context , boolean autoInitialize)  {
        super(context, autoInitialize);
        Logger.init();
    }


    /**
     *
     * @param account
     * @param extras
     * @param authority
     * @param provider
     * @param syncResult
     *
     * perform Network fetch here, fetch all data including diary , attendance, notifications , test results.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        NetworkService.FetchMembersData(getContext(), null, new NetworkService.NetworkServiceCB() {
            @Override
            public void onSuccess(Bundle message) {

                Gson gson = new Gson();

                ArrayList<ContentValues> membersValues = new ArrayList<ContentValues>();
                if(message.containsKey("members"))  {
                    ArrayList<String> membersList = message.getStringArrayList("members");

                    for(String member : membersList)    {
                        MemberModel memberModel = gson.fromJson(member,MemberModel.class);
                        ContentValues memberValue = new ContentValues();
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_TABLE_USER_ID,memberModel.id);
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_TABLE_DOB,memberModel.dob);
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_TABLE_STATUS,memberModel.status);
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_TABLE_ETHNICITY,memberModel.ethnicity);
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_TABLE_WEIGHT, Utility.convertWeightToKg(memberModel.weight));
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_TABLE_HEIGHT,memberModel.height);
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_TABLE_IS_VEG,memberModel.is_veg);
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_TABLE_DRINK,memberModel.drink);
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_TABLE_IMAGE,memberModel.image);
                        memberValue.put(StatusHubContract.UsersSchema.COLUMN_IS_FAVOURITE,0);

                        membersValues.add(memberValue);

                    }

                    ContentValues dataArray[] = new ContentValues[membersValues.size()];
                    membersValues.toArray(dataArray);
                    Logger.w("Bulk Inserting");
                    getContext().getContentResolver().bulkInsert(StatusHubContract.UsersSchema.CONTENT_URI, dataArray);

                }


            }

            @Override
            public void onFaliure(Bundle message) {
                Logger.w("Failed Sync : " + message.getString("message"));
            }
        });
    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        StatusHubSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
