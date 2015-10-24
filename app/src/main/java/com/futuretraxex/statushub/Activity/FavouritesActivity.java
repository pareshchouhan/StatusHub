package com.futuretraxex.statushub.Activity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.futuretraxex.statushub.Adapters.StatusListAdapter;
import com.futuretraxex.statushub.R;
import com.futuretraxex.statushub.Sync.StatusHubSyncAdapter;
import com.futuretraxex.statushub.Utility.SwipeDismissListViewTouchListener;
import com.futuretraxex.statushub.database.StatusHubContract;
import com.orhanobut.logger.Logger;

public class FavouritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    StatusListAdapter mFavouriteListAdapter;

    private Uri mDataUri = null;

    public static final int USERS_FAV_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        mFavouriteListAdapter = new StatusListAdapter(this,null,true);
        new FavouritesActivityViewHolder(getWindow().getDecorView().getRootView());

        Logger.init();

        //TODO : Patch this to work with CursorAdapter, or Find a work around
        //TODO : Clicking on the link and removing from Favourites works.
//        SwipeDismissListViewTouchListener touchListener =
//                new SwipeDismissListViewTouchListener(
//                        FavouritesActivityViewHolder.mListView,
//                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
//                            @Override
//                            public boolean canDismiss(int position) {
//                                return true;
//                            }
//
//                            @Override
//                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
//                                //Notify server that notification has been acknowledged.
//                                for (int position : reverseSortedPositions) {
//                                    //mFavouriteListAdapter.remove(position);
//
//                                    Uri updateUri = StatusHubContract.UsersSchema.buildUsersUriWithFavouriteAndUserId(position + 1);
//                                    ContentValues cvalue = new ContentValues();
//                                    cvalue.put(StatusHubContract.UsersSchema.COLUMN_IS_FAVOURITE, 0);
//                                    int rowsUpdated = getContentResolver().update(updateUri,cvalue,null,null);
//                                    if(rowsUpdated > 0) {
//                                        Toast.makeText(FavouritesActivity.this, "Removed From Favourites", Toast.LENGTH_SHORT).show();
//                                        getLoaderManager().restartLoader(USERS_FAV_LOADER, null, FavouritesActivity.this);
//                                    }
//                                }
//                                //mFavouriteListAdapter.notifyDataSetChanged();
//                            }
//                        });
//        FavouritesActivityViewHolder.mListView.setOnTouchListener(touchListener);
//        // Setting this scroll listener is required to ensure that during ListView scrolling,
//        // we don't look for swipes.
//        FavouritesActivityViewHolder.mListView.setOnScrollListener(touchListener.makeScrollListener());



        FavouritesActivityViewHolder.mListView.setAdapter(mFavouriteListAdapter);


        FavouritesActivityViewHolder.mListView.setEmptyView(FavouritesActivityViewHolder.mEmptyLayout);

        ISetupListeners();

        getLoaderManager().initLoader(USERS_FAV_LOADER, null, this);
    }

    public void ISetupListeners() {
        FavouritesActivityViewHolder.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent memberDetailActivity = new Intent(FavouritesActivity.this, MemberProfileActivity.class);
                Uri uri = StatusHubContract.UsersSchema.buildUsersUriWithId(l);
                    Logger.w("Uri : " + uri);
                Cursor data = getContentResolver().query(uri, HomeActivity.USERS_COLUMNS, null, null, null, null);
                if (data != null) {
                    if (data.moveToFirst()) {
                            Logger.w("Uri : " + data.getInt(HomeActivity.COL_TABLE_USER_ID) + " " + data.getString(HomeActivity.COL_TABLE_STATUS));
                    }
                    data.close();
                }

                memberDetailActivity.putExtra("uri", uri.toString());
                startActivity(memberDetailActivity);
            }
        });
    }

    static class FavouritesActivityViewHolder {
        public static ListView mListView;
        public static ProgressBar mProgressBar;
        public static TextView mEmptyLayout;

        public FavouritesActivityViewHolder(View view)  {
            mListView = (ListView) view.findViewById(R.id.favourites_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_fav);
            mEmptyLayout = (TextView) view.findViewById(R.id.empty_fav_view);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if(mDataUri == null)    {
            mDataUri = StatusHubContract.UsersSchema.buildUsersUriWithFavourites();
        }
        FavouritesActivityViewHolder.mProgressBar.setVisibility(ProgressBar.VISIBLE);
        Logger.w("Loading data...");
        return new CursorLoader(this,
                mDataUri,
                HomeActivity.USERS_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Logger.w("Swapping cursor data " + data.getCount());
        FavouritesActivityViewHolder.mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mFavouriteListAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavouriteListAdapter.swapCursor(null);
    }

}
