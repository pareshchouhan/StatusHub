package com.futuretraxex.statushub.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hudelabs on 10/24/2015.
 */
public class StatusHubDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "statushub.db";
    public static final int DATABASE_VERSION = 1;


    public StatusHubDBHelper(Context context)    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(StatusHubContract.UsersSchema.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop previous table if app is re-installed
        //Too lazy to check versions and do upgrade and transfer data
        //because test application.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StatusHubContract.UsersSchema.TABLE_NAME);

        //Drop and recreate.
        onCreate(sqLiteDatabase);
    }
}
