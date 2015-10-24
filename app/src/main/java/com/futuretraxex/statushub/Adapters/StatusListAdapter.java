package com.futuretraxex.statushub.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.futuretraxex.statushub.Activity.HomeActivity;
import com.futuretraxex.statushub.R;
import com.orhanobut.logger.Logger;

/**
 * Created by hudelabs on 10/24/2015.
 */
public class StatusListAdapter extends CursorAdapter{

    private static final int STATUS_TEXT_LENGTH = 30;

    public StatusListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public static class ViewHolder  {
        public final TextView mStatusTextView;

        public ViewHolder(View view)    {
            mStatusTextView = (TextView) view.findViewById(R.id.status_text);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

//        Logger.w("Got New View");
        View view = LayoutInflater.from(context).inflate(R.layout.layout_status_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String statusString = cursor.getString(HomeActivity.COL_TABLE_STATUS);
//        Logger.w(statusString);
        //TextView status = (TextView) view.findViewById(R.id.status_text);
        int mLength = statusString.length() < STATUS_TEXT_LENGTH ? statusString.length() : STATUS_TEXT_LENGTH;
        viewHolder.mStatusTextView.setText(statusString.substring(0,mLength) + "...");
    }
}
