package com.futuretraxex.statushub.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.futuretraxex.statushub.Utility.Utility;

/**
 * Created by hudelabs on 10/24/2015.
 */
public class StatusHubContract {
    //Content authority to be register by provider.
    public static final String CONTENT_AUTHORITY = "com.futuretraxex.statushub.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    //Possible paths appended to BASE_CONTENT_URI.
    public static final String PATH_USERS = "users";

    public static final String PATH_USERS_FAVOURITES = "favourites";

    public static final String PATH_USERS_WEIGHT = "weight";

    public static final String PATH_USERS_HEIGHT = "height";

    public static final String PATH_USERS_ETHNICITY = "ethnicity";


    public static final class UsersSchema implements BaseColumns   {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();

        //For multiple items.
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
        //For single item.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;

        public static final String TABLE_NAME = "users";

        public static final String COLUMN_TABLE_DOB = "dob";
        public static final String COLUMN_TABLE_STATUS = "status";
        public static final String COLUMN_TABLE_ETHNICITY = "ethnicity";
        public static final String COLUMN_TABLE_WEIGHT = "weight";
        public static final String COLUMN_TABLE_HEIGHT = "height";
        public static final String COLUMN_TABLE_IS_VEG = "is_veg";
        public static final String COLUMN_TABLE_DRINK = "drink";
        public static final String COLUMN_TABLE_IMAGE = "image";
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + "(\n" +
                "\t`"+ _ID  +"`\tINTEGER NOT NULL PRIMARY KEY,\n" +
                "\t`" + COLUMN_TABLE_DOB + "`\tTEXT NOT NULL,\n" +
                "\t`" + COLUMN_TABLE_STATUS + "`\tTEXT NOT NULL,\n" +
                "\t`" + COLUMN_TABLE_ETHNICITY + "`\tINTEGER NOT NULL,\n" +
                "\t`"+ COLUMN_TABLE_WEIGHT +"`\tLONG NOT NULL,\n" +
                "\t`"+ COLUMN_TABLE_HEIGHT +"`\tINTEGER NOT NULL,\n" +
                "\t`"+ COLUMN_TABLE_IS_VEG +"`\tINTEGER NOT NULL\n" +
                "\t`"+ COLUMN_TABLE_DRINK +"`\tINTEGER NOT NULL\n" +
                "\t`"+ COLUMN_TABLE_IMAGE +"`\tTEXT\n" +
                "\t`"+ COLUMN_IS_FAVOURITE +"`\tINTEGER NOT NULL\n" +
                ")";



        //below build Uris are working, See TestUriGenerator.java for detailed Tests.

        public static Uri buildUsersUriWithId(long id)   {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri buildUsersUriWithFavourites() {
            return CONTENT_URI.buildUpon()
                    .appendEncodedPath(PATH_USERS)
                    .appendEncodedPath(PATH_USERS_FAVOURITES)
                    .build();
        }

        public static Uri buildUsersUriWithWeightSortFilter()   {
            return CONTENT_URI.buildUpon()
                    .appendEncodedPath(PATH_USERS)
                    .appendEncodedPath(PATH_USERS_WEIGHT)
                    .build();
        }

        public static Uri buildUsersUriWithHeightSortFilter()   {
            return CONTENT_URI.buildUpon()
                    .appendEncodedPath(PATH_USERS)
                    .appendEncodedPath(PATH_USERS_HEIGHT)
                    .build();
        }

        public static Uri buildUsersUriWithWeightFilter(long weight)   {
            return CONTENT_URI.buildUpon()
                    .appendEncodedPath(PATH_USERS)
                    .appendEncodedPath(PATH_USERS_WEIGHT)
                    .appendEncodedPath(String.valueOf(weight))
                    .build();
        }

        public static Uri buildUsersUriWithHeightFilter(int height)   {
            return CONTENT_URI.buildUpon()
                    .appendEncodedPath(PATH_USERS)
                    .appendEncodedPath(PATH_USERS_HEIGHT)
                    .appendEncodedPath(String.valueOf(height))
                    .build();
        }

        public static Uri buildUsersWithEthnicityFilter(String ethnicity)   {
            String ethnic = String.valueOf(Utility.getIdFromEthnicity(ethnicity));
            return CONTENT_URI.buildUpon()
                    .appendEncodedPath(PATH_USERS)
                    .appendEncodedPath(PATH_USERS_ETHNICITY)
                    .appendEncodedPath(ethnic)
                    .build();
        }


    }

}
