package com.futuretraxex.statushub.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.futuretraxex.statushub.R;

public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize viewholder.

        new HomeActivityViewHolder(getWindow().getDecorView().getRootView());


        ISetupListeners();
    }

    //Setup Listeners in here.

    void ISetupListeners()   {

        //Filter by ethnic group if we select this.
        HomeActivityViewHolder.mSelectEthnicitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    static class HomeActivityViewHolder   {
        public static TextView mOpenFavouritesTextView;
        public static Spinner mSelectEthnicitySpinner;

        public HomeActivityViewHolder(View view)    {
            mOpenFavouritesTextView = (TextView) view.findViewById(R.id.open_favourites);
            mSelectEthnicitySpinner = (Spinner) view.findViewById(R.id.spinner_ethnic);
        }
    }
}
