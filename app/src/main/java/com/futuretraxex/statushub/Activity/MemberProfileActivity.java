package com.futuretraxex.statushub.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.futuretraxex.statushub.DataModel.MemberModel;
import com.futuretraxex.statushub.R;
import com.futuretraxex.statushub.Utility.CircleTransform;
import com.futuretraxex.statushub.Utility.Utility;
import com.futuretraxex.statushub.database.StatusHubContract;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.internal.http.HttpMethod;
import com.squareup.picasso.Picasso;

public class MemberProfileActivity extends AppCompatActivity {


    private int mId;
    private int mUserId;
    private String mDOB;
    private String mImage;
    private float mWeight;
    private int mHeight;
    private boolean mDrink;
    private boolean mIsVeg;
    private String mStatus;
    private boolean mIsFavourite;
    private String mEthnicity;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        Logger.init();
        new MemberActivityViewHolder(getWindow().getDecorView().getRootView());

        mUri = Uri.parse(getIntent().getExtras().getString("uri"));

        Cursor data = getContentResolver().query(mUri,HomeActivity.USERS_COLUMNS,null,null,null,null);
        if(data != null)    {
            if(data.moveToFirst())  {
                mId = data.getInt(HomeActivity.COL_TABLE_ID);
                mUserId = data.getInt(HomeActivity.COL_TABLE_USER_ID);
                mDOB = data.getString(HomeActivity.COL_TABLE_DOB);
                mStatus = data.getString(HomeActivity.COL_TABLE_STATUS);
                mImage = data.getString(HomeActivity.COL_TABLE_IMAGE);
                mWeight = data.getFloat(HomeActivity.COL_TABLE_WEIGHT);
                mHeight = data.getInt(HomeActivity.COL_TABLE_HEIGHT);
                mIsFavourite = data.getInt(HomeActivity.COL_TABLE_IS_FAVOURITE) == 1;
                mIsVeg = data.getInt(HomeActivity.COL_TABLE_IS_VEG) == 1;
                mDrink = data.getInt(HomeActivity.COL_TABLE_DRINK) == 1;
                mEthnicity = Utility.getEthnicityFromId(data.getInt(HomeActivity.COL_TABLE_ETHNICITY));

                MemberActivityViewHolder.mEthnicTextView.setText(mEthnicity);
                Picasso.with(this).load(mImage)
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.ic_photo_black_24dp)
                        .into(MemberActivityViewHolder.mProfileImage);

                if(mIsFavourite)    {
                    MemberActivityViewHolder.mFavouritesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp,0,0,0);

                }
                else {
                    MemberActivityViewHolder.mFavouritesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_24dp,0,0,0);
                }
            }
        }

        ISetupListeners();

    }

    public void ISetupListeners()   {
        MemberActivityViewHolder.mFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri updateUri = StatusHubContract.UsersSchema.buildUsersUriWithFavouriteAndUserId(mId);
                ContentValues cvalue = new ContentValues();
                if(mIsFavourite)    {
                    cvalue.put(StatusHubContract.UsersSchema.COLUMN_IS_FAVOURITE, 0);
                    int rowsUpdated = getContentResolver().update(updateUri,cvalue,null,null);
                    if(rowsUpdated > 0) {
                        Toast.makeText(MemberProfileActivity.this, "Removed From Favourites", Toast.LENGTH_SHORT).show();
                        //getLoaderManager().restartLoader(USERS_FAV_LOADER, null, FavouritesActivity.this);
                        mIsFavourite = false;
                        ((Button)view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_24dp, 0, 0, 0);
                    }

                }
                else {
                    cvalue.put(StatusHubContract.UsersSchema.COLUMN_IS_FAVOURITE, 1);
                    int rowsUpdated = getContentResolver().update(updateUri,cvalue,null,null);
                    if(rowsUpdated > 0) {
                        Toast.makeText(MemberProfileActivity.this, "Added To Favourites", Toast.LENGTH_SHORT).show();
                        //getLoaderManager().restartLoader(USERS_FAV_LOADER, null, FavouritesActivity.this);
                        mIsFavourite = true;
                        ((Button)view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp,0,0,0);
                    }

                }
            }
        });
    }

    static class MemberActivityViewHolder {
        public static ImageView mProfileImage;
        public static TextView mEthnicTextView;
        public static Button mFavouritesButton;
        public static Button mShareButton;
        public static Button mBackButton;
        public static Button mSMSButton;

        public MemberActivityViewHolder(View view)  {
            mProfileImage = (ImageView) view.findViewById(R.id.large_profile_pic);
            mEthnicTextView = (TextView) view.findViewById(R.id.ethnicity);
            mFavouritesButton = (Button) view.findViewById(R.id.favourite_button);
            mShareButton = (Button) view.findViewById(R.id.share_button);
            mBackButton = (Button) view.findViewById(R.id.back_button);
            mSMSButton = (Button) view.findViewById(R.id.sms_button);

        }
    }
}
