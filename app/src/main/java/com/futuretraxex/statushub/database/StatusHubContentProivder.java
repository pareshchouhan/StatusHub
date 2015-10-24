package com.futuretraxex.statushub.database;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

/**
 * Created by hudelabs on 10/24/2015.
 */
public class StatusHubContentProivder extends ContentProvider {

    private StatusHubDBHelper mDBHelper;

    private static final SQLiteQueryBuilder sUsersSchemaQueryBuilder;


    static {
        Logger.init();
        sUsersSchemaQueryBuilder = new SQLiteQueryBuilder();

    }



    //Build a URI Matcher.
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static final int USERS = 100;

    public static final int USERS_BY_ID = 101;
    //<content_authority>/users/height
    public static final int USERS_BY_HEIGHT_SORT = 102;
    //<content_authority>/users/weight
    public static final int USERS_BY_WEIGHT_SORT = 103;
    //<content_authority>/users/height/#
    public static final int USERS_BY_HEIGHT_FILTER = 104;
    //<content_authority>/users/weight/#
    public static final int USERS_BY_WEIGHT_FILTER = 105;
    //<content_authority>/users/ethnicity/#
    public static final int USERS_BY_ETHNICITY_FILTER = 106;
    //<content_authority>/users/favourites
    public static final int USERS_BY_FAVOURITES_FILTER = 107;

    public static final int USERS_BY_ETHNICITY_WEIGHT_FILTER = 108;

    public static final int USERS_BY_ETHNICITY_HEIGHT_FILTER = 109;


    public static UriMatcher buildUriMatcher() {
        // don't need Regex because UriMatcher

        // return corresponding code when a URI matches.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = StatusHubContract.CONTENT_AUTHORITY;

        //Create corresponding code for each type of URI.
        //First is insert
        matcher.addURI(authority, "users", USERS);
        matcher.addURI(authority, "users/#", USERS_BY_ID);
        matcher.addURI(authority, "users/weight", USERS_BY_WEIGHT_SORT);
        matcher.addURI(authority, "users/height", USERS_BY_HEIGHT_SORT);
        matcher.addURI(authority, "users/weight/*", USERS_BY_WEIGHT_FILTER);
        matcher.addURI(authority, "users/height/#", USERS_BY_HEIGHT_FILTER);
        matcher.addURI(authority, "users/ethnicity/#/weight", USERS_BY_ETHNICITY_WEIGHT_FILTER);
        matcher.addURI(authority, "users/ethnicity/#/height", USERS_BY_ETHNICITY_HEIGHT_FILTER);
        matcher.addURI(authority, "users/ethnicity/#", USERS_BY_ETHNICITY_FILTER);
        matcher.addURI(authority, "users/favourites", USERS_BY_FAVOURITES_FILTER);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDBHelper = new StatusHubDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor = null;
        final int match = sUriMatcher.match(uri);

        switch(match)   {
            case USERS:
                retCursor = mDBHelper.getReadableDatabase().query(
                        StatusHubContract.UsersSchema.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case USERS_BY_ID:
                retCursor = getUserById(uri, projection, sortOrder);
                break;
            case USERS_BY_HEIGHT_SORT:
                retCursor = getUsersByHeightSort(uri, projection, sortOrder);
                break;
            case USERS_BY_WEIGHT_SORT:
                retCursor = getUsersByWeightSort(uri, projection, sortOrder);
                break;
            case USERS_BY_HEIGHT_FILTER:
                retCursor = getUsersByGenericFilters(uri, projection, sortOrder);
                break;
            case USERS_BY_WEIGHT_FILTER:
                retCursor = getUsersByGenericFilters(uri, projection, sortOrder);
                break;
            case USERS_BY_ETHNICITY_FILTER:
                retCursor = getUsersByGenericFilters(uri, projection, sortOrder);
                break;
            case USERS_BY_FAVOURITES_FILTER:
                retCursor = getUsersByFavouriteFilter(uri, projection, sortOrder);
                break;
            case USERS_BY_ETHNICITY_HEIGHT_FILTER:
                retCursor = getUsersByGenericFilters(uri,projection,sortOrder);
                break;
            case USERS_BY_ETHNICITY_WEIGHT_FILTER:
                retCursor = getUsersByGenericFilters(uri,projection,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);

        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch(match)   {
            case USERS:
                return StatusHubContract.UsersSchema.CONTENT_DIR_TYPE;
            case USERS_BY_ID:
                return StatusHubContract.UsersSchema.CONTENT_ITEM_TYPE;
            case USERS_BY_WEIGHT_SORT:
                return StatusHubContract.UsersSchema.CONTENT_DIR_TYPE;
            case USERS_BY_HEIGHT_SORT:
                return StatusHubContract.UsersSchema.CONTENT_DIR_TYPE;
            case USERS_BY_WEIGHT_FILTER:
                return StatusHubContract.UsersSchema.CONTENT_DIR_TYPE;
            case USERS_BY_HEIGHT_FILTER:
                return StatusHubContract.UsersSchema.CONTENT_DIR_TYPE;
            case USERS_BY_ETHNICITY_FILTER:
                return StatusHubContract.UsersSchema.CONTENT_DIR_TYPE;
            case USERS_BY_FAVOURITES_FILTER:
                return StatusHubContract.UsersSchema.CONTENT_DIR_TYPE;
            case USERS_BY_ETHNICITY_HEIGHT_FILTER:
                return StatusHubContract.UsersSchema.CONTENT_DIR_TYPE;
            case USERS_BY_ETHNICITY_WEIGHT_FILTER:
                return StatusHubContract.UsersSchema.CONTENT_DIR_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match)   {
            case USERS:

                long _id = db.insert(StatusHubContract.UsersSchema.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = StatusHubContract.UsersSchema.buildUsersUriWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        getContext().getContentResolver().notifyChange(returnUri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch(match)   {
            case USERS:
                rowsDeleted = db.delete(
                        StatusHubContract.UsersSchema.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted != 0)    {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case USERS:
                rowsUpdated = db.update(StatusHubContract.UsersSchema.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(StatusHubContract.UsersSchema.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }




    @Override
    @TargetApi(11)
    public void shutdown() {
        mDBHelper.close();
        super.shutdown();
    }

    //Helper functions for querying.

    private Cursor getUserById(Uri uri, String[] projection, String sortOrder)   {
        String id = StatusHubContract.UsersSchema.getUserIdFromUsersByIdUri(uri);
        String selection = StatusHubContract.UsersSchema.SELECT_BY_USER_ID;
        String[] selectionArgs = {
                id
        };
        return mDBHelper.getReadableDatabase().query(
                StatusHubContract.UsersSchema.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getUsersByHeightSort(Uri uri, String[] projection, String sortOrder)   {
        String sort = StatusHubContract.UsersSchema.SORT_BY_HEIGHT;
        return mDBHelper.getReadableDatabase().query(
                StatusHubContract.UsersSchema.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sort);
    }
    private Cursor getUsersByWeightSort(Uri uri, String[] projection, String sortOrder)   {
        String sort = StatusHubContract.UsersSchema.SORT_BY_WEIGHT;
        return mDBHelper.getReadableDatabase().query(
                StatusHubContract.UsersSchema.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sort);
    }

    //Again below 3 functions can be merged into a single function to save space.
    //space I choose you ! gotta catch'em all!
    //that got redundant quite quickly pfft.


    private Cursor getUsersByGenericFilters(Uri uri, String[] projection, String sortOrder) {

        int match = sUriMatcher.match(uri);
        String selection = null;
        String sort = null;
        String[] selectionArgs = {
                StatusHubContract.UsersSchema.getGenericFilterValueFromUsersUri(uri)
        };
        switch(match)   {
            case USERS_BY_WEIGHT_FILTER:
                selection = StatusHubContract.UsersSchema.SELECT_BY_WEIGHT_FILTER;
                break;
            case USERS_BY_HEIGHT_FILTER:
                selection = StatusHubContract.UsersSchema.SELECT_BY_HEIGHT_FILTER;
                break;
            case USERS_BY_ETHNICITY_FILTER:
                selection = StatusHubContract.UsersSchema.SELECT_BY_ETHNICITY_FILTER;
                break;
            case USERS_BY_ETHNICITY_WEIGHT_FILTER:
                selection = StatusHubContract.UsersSchema.SELECT_BY_ETHNICITY_FILTER;
                sort = StatusHubContract.UsersSchema.SORT_BY_WEIGHT;
                break;
            case USERS_BY_ETHNICITY_HEIGHT_FILTER:
                selection = StatusHubContract.UsersSchema.SELECT_BY_ETHNICITY_FILTER;
                sort = StatusHubContract.UsersSchema.SORT_BY_HEIGHT;
                break;
        }


        return mDBHelper.getReadableDatabase().query(
                StatusHubContract.UsersSchema.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sort);


    }


//    private Cursor getUsersByHeightSortFilter(Uri uri, String[] projection, String sortOrder)   {
//        String id = StatusHubContract.UsersSchema.getUserIdFromUsersByIdUri(uri);
//        String selection = StatusHubContract.UsersSchema.SELECT_BY_USER_ID;
//        String[] selectionArgs = {
//                id
//        };
//        return mDBHelper.getReadableDatabase().query(
//                StatusHubContract.UsersSchema.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder);
//    }
//    private Cursor getUsersByWeightSortFilter(Uri uri, String[] projection, String sortOrder)   {
//        String id = StatusHubContract.UsersSchema.getUserIdFromUsersByIdUri(uri);
//        String selection = StatusHubContract.UsersSchema.SELECT_BY_USER_ID;
//        String[] selectionArgs = {
//                id
//        };
//        return mDBHelper.getReadableDatabase().query(
//                StatusHubContract.UsersSchema.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder);
//    }
//    private Cursor getUsersByEthnicityFilter(Uri uri, String[] projection, String sortOrder)   {
//        String id = StatusHubContract.UsersSchema.getUserIdFromUsersByIdUri(uri);
//        String selection = StatusHubContract.UsersSchema.SELECT_BY_USER_ID;
//        String[] selectionArgs = {
//                id
//        };
//        return mDBHelper.getReadableDatabase().query(
//                StatusHubContract.UsersSchema.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder);
//    }
    private Cursor getUsersByFavouriteFilter(Uri uri, String[] projection, String sortOrder)   {
        String selection = StatusHubContract.UsersSchema.SELECT_BY_FAVOURITES_FILTER;
        return mDBHelper.getReadableDatabase().query(
                StatusHubContract.UsersSchema.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                sortOrder);
    }


}
