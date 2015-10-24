package com.futuretraxex.statushub.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretraxex.statushub.Activity.HomeActivity;
import com.futuretraxex.statushub.R;
import com.futuretraxex.statushub.Utility.CircleTransform;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

/**
 * Created by paresh on 10/24/2015.
 */

//Refrence


public class StatusListAdapter extends CursorAdapter{

    private static final int STATUS_TEXT_LENGTH = 30;

    public StatusListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public static class ViewHolder  {
        public final TextView mStatusTextView;
        public final ImageView mProfilePicSmall;
        public final int id;

        public ViewHolder(View view, int _id)    {
            mStatusTextView = (TextView) view.findViewById(R.id.status_text);
            mProfilePicSmall = (ImageView) view.findViewById(R.id.small_profile_pic);
            id = _id;
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

//        Logger.w("Got New View");
        View view = LayoutInflater.from(context).inflate(R.layout.layout_status_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view,cursor.getInt(HomeActivity.COL_TABLE_USER_ID));
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {


        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        String statusString = cursor.getString(HomeActivity.COL_TABLE_STATUS);
        final String imageUrl = cursor.getString(HomeActivity.COL_TABLE_IMAGE);

        Picasso.with(context)
                .load(imageUrl)
                .transform(new CircleTransform())
                .placeholder(R.drawable.ic_photo_black_24dp)
                .into(viewHolder.mProfilePicSmall);
//        Logger.w(statusString);
        //TextView status = (TextView) view.findViewById(R.id.status_text);
        int mLength = statusString.length() < STATUS_TEXT_LENGTH ? statusString.length() : STATUS_TEXT_LENGTH;
        viewHolder.mStatusTextView.setText(statusString.substring(0,mLength) + "...");
    }
}
