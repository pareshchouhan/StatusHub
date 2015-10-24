package com.futuretraxex.statushub;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.futuretraxex.statushub.database.StatusHubContract;
import com.futuretraxex.statushub.database.StatusHubDBHelper;

import java.util.HashSet;

/**
 * Created by hudelabs on 10/24/2015.
 */
public class TestDB extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testDBCreate()  {

        String tableName = StatusHubContract.UsersSchema.TABLE_NAME;
        getContext().deleteDatabase(StatusHubDBHelper.DATABASE_NAME);
        StatusHubDBHelper dbHelper = new StatusHubDBHelper(getContext());
        //dbHelper.onCreate(dbHelper.getWritableDatabase());
        HashSet<String> tableList = new HashSet<>(1);
        tableList.add(tableName);
        SQLiteDatabase db = new StatusHubDBHelper(getContext()).getWritableDatabase();
        assertEquals("isOpen fails", true, db.isOpen());
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());
        //find table name in list of tables
        //remove if exists.
        do {
            tableList.remove(c.getString(0));
        }while(c.moveToNext());

        assertEquals("Error Table not found.", true, tableList.isEmpty());


    }
}
