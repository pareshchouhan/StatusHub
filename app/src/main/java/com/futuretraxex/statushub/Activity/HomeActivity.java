package com.futuretraxex.statushub.Activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.futuretraxex.statushub.Adapters.StatusListAdapter;
import com.futuretraxex.statushub.R;
import com.futuretraxex.statushub.Sync.StatusHubSyncAdapter;
import com.futuretraxex.statushub.Utility.Utility;
import com.futuretraxex.statushub.database.StatusHubContract;
import com.orhanobut.logger.Logger;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private StatusListAdapter mStatusListAdapter;

    public static final String[] USERS_COLUMNS = {
            StatusHubContract.UsersSchema._ID,
            StatusHubContract.UsersSchema.COLUMN_TABLE_USER_ID,
            StatusHubContract.UsersSchema.COLUMN_TABLE_DOB,
            StatusHubContract.UsersSchema.COLUMN_TABLE_WEIGHT,
            StatusHubContract.UsersSchema.COLUMN_TABLE_HEIGHT,
            StatusHubContract.UsersSchema.COLUMN_TABLE_ETHNICITY,
            StatusHubContract.UsersSchema.COLUMN_TABLE_STATUS,
            StatusHubContract.UsersSchema.COLUMN_TABLE_IS_VEG,
            StatusHubContract.UsersSchema.COLUMN_TABLE_DRINK,
            StatusHubContract.UsersSchema.COLUMN_IS_FAVOURITE
    };

    public static final int COL_TABLE_ID = 0;
    public static final int COL_TABLE_USER_ID = 1;
    public static final int COL_TABLE_DOB = 2;
    public static final int COL_TABLE_WEIGHT = 3;
    public static final int COL_TABLE_HEIGHT = 4;
    public static final int COL_TABLE_ETHNICITY = 5;
    public static final int COL_TABLE_STATUS = 6;
    public static final int COL_TABLE_IS_VEG = 7;
    public static final int COL_TABLE_DRINK = 8;
    public static final int COL_TABLE_IS_FAVOURITE = 9;

    private Uri mDataUri = null;
    private int mEthnicity = -1;


    public static final int USERS_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize viewholder.
        Logger.init();

        new HomeActivityViewHolder(getWindow().getDecorView().getRootView());



        mStatusListAdapter = new StatusListAdapter(this,null,true);

        HomeActivityViewHolder.mUsersListView.setAdapter(mStatusListAdapter);

        ISetupListeners();

        StatusHubSyncAdapter.initializeSyncAdapter(this);
        getLoaderManager().initLoader(USERS_LOADER, null, this);
    }

    //Setup Listeners in here.

    void ISetupListeners()   {

        //Filter by ethnic group if we select this.
        HomeActivityViewHolder.mSelectEthnicitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    mDataUri = StatusHubContract.UsersSchema.CONTENT_URI;
                    mEthnicity = -1;
                } else {
                    mEthnicity = i - 1;
                    mDataUri = StatusHubContract.UsersSchema.buildUsersWithEthnicityIdFilter(i - 1);
                }
                getLoaderManager().restartLoader(USERS_LOADER, null, HomeActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        HomeActivityViewHolder.mHeightSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEthnicity != -1)    {
                    mDataUri = StatusHubContract.UsersSchema.buildUsersWithEthnicityIdHeightFilter(mEthnicity);
                }
                else {
                    mDataUri = StatusHubContract.UsersSchema.buildUsersUriWithHeightSortFilter();
                }

                getLoaderManager().restartLoader(USERS_LOADER, null, HomeActivity.this);
            }
        });

        HomeActivityViewHolder.mWeightSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEthnicity != -1)    {
                    mDataUri = StatusHubContract.UsersSchema.buildUsersWithEthnicityIdWeightFilter(mEthnicity);
                }
                else {
                    mDataUri = StatusHubContract.UsersSchema.buildUsersUriWithWeightSortFilter();
                }
                getLoaderManager().restartLoader(USERS_LOADER, null, HomeActivity.this);
            }
        });

        HomeActivityViewHolder.mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        HomeActivityViewHolder.mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {

                    String tempString = s;
                    if(tempString.toLowerCase().contains("cm"))  {
                        int start = tempString.toLowerCase().indexOf("cm");
                        tempString = tempString.toLowerCase().substring(0,start);
                    }
                    int height = Integer.valueOf(tempString);
                    mDataUri = StatusHubContract.UsersSchema.buildUsersUriWithHeightFilter(height);
                    getLoaderManager().restartLoader(USERS_LOADER, null, HomeActivity.this);
                } catch (NumberFormatException iox) {
                    try {

                        String tempString = s;
                        if(tempString.toLowerCase().contains("kg"))  {
                            int start = tempString.toLowerCase().indexOf("kg");
                            tempString = tempString.toLowerCase().substring(0,start);
                        }
                        float weight = Float.valueOf(tempString);
                        mDataUri = StatusHubContract.UsersSchema.buildUsersUriWithWeightFilter(weight);
                        getLoaderManager().restartLoader(USERS_LOADER, null, HomeActivity.this);
                    } catch (NumberFormatException nox) {
                        int ethnicity = Utility.getIdFromEthnicity(s);
                        mDataUri = StatusHubContract.UsersSchema.buildUsersWithEthnicityIdFilter(ethnicity);
                        if (s.length() == 0) {
                            mDataUri = StatusHubContract.UsersSchema.CONTENT_URI;
                        }

                        getLoaderManager().restartLoader(USERS_LOADER, null, HomeActivity.this);
                    }
                }
                return true;
            }
        });
    }




    static class HomeActivityViewHolder   {

        public static SearchView mSearchView;

        public static TextView mOpenFavouritesTextView;
        public static Spinner mSelectEthnicitySpinner;
        public static ListView mUsersListView;

        public static Button mWeightSortButton;
        public static Button mHeightSortButton;

        public static ProgressBar mProgressBar;

        public HomeActivityViewHolder(View view)    {
            mOpenFavouritesTextView = (TextView) view.findViewById(R.id.open_favourites);
            mSelectEthnicitySpinner = (Spinner) view.findViewById(R.id.spinner_ethnic);
            mUsersListView = (ListView) view.findViewById(R.id.status_list);
            mWeightSortButton = (Button) view.findViewById(R.id.sortByWeight);
            mHeightSortButton = (Button) view.findViewById(R.id.sortByHeight);
            mSearchView = (SearchView) view.findViewById(R.id.searchbar);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if(mDataUri == null)    {
            mDataUri = StatusHubContract.UsersSchema.CONTENT_URI;
        }
        HomeActivityViewHolder.mProgressBar.setVisibility(ProgressBar.VISIBLE);
//        Logger.w("Loading data...");
        return new CursorLoader(this,
                mDataUri,
                USERS_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        Logger.w("Swapping cursor data " + data.getCount());
        HomeActivityViewHolder.mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mStatusListAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mStatusListAdapter.swapCursor(null);
    }

}
