package com.futuretraxex.statushub.Sync;

import android.content.Context;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by paresh on 10/24/2015.
 */
public class NetworkService {

    //generally I would use Uri builder to build Uris base on custom params
    //but since we have two static Uris I am hardcoding them.
    //and yes I do know how to use Uri.parse().buildUpon().build();
    //just saying :D

    static public final String BASE_URL = "https://tipstat.0x10.info/";

    static public final String FETCH_MEMBER_DETAILS_URL = "https://tipstat.0x10.info/api/tipstat?type=json&query=list_status";

    static public final String FETCH_API_HITS_URL = "https://tipstat.0x10.info/api/tipstat?type=json&query=api_hits";

    public interface NetworkServiceCB {
        void onSuccess(Bundle message);
        void onFaliure(Bundle message);
    }

    public static void FetchMembersData(final Context context, final Bundle _data, final NetworkServiceCB networkServiceCB)    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Bundle temp = new Bundle();
                try {
                    URL _url = new URL(FETCH_MEMBER_DETAILS_URL);
                    RequestBody body = new FormEncodingBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url(_url)
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();

                    //Parse JSON data here.
                    if(response.code() == 200)  {
                        JSONObject output = new JSONObject(responseBody);
                        JSONArray members = output.getJSONArray("members");
                        ArrayList<String> membersList = new ArrayList<String>(members.length());
                        for(int i=0 ;i < members.length(); i++) {
                            membersList.add(members.get(i).toString());
                        }
                        temp.putStringArrayList("members", membersList);
                        networkServiceCB.onSuccess(temp);
                    }
                    else {
                        temp.putString("message", "HTTP Error Code : " + response.code());
                        networkServiceCB.onFaliure(temp);
                    }

                } catch (IOException|JSONException iox) {
                    Logger.e("Exception : " + iox.toString());
                    temp.putString("message", "Exception : " + iox.toString());
                    networkServiceCB.onFaliure(temp);
                }
            }
        }).start();
    }

    public static void FetchAPIHits(final Context context, final NetworkServiceCB networkServiceCB)   {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Do network access here.

                OkHttpClient client = new OkHttpClient();
                Bundle temp = new Bundle();
                try {
                    URL _url = new URL(FETCH_API_HITS_URL);
                    RequestBody body = new FormEncodingBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url(_url)
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();

                    //Parse JSON data here.
                    if(response.code() == 200)  {
                        JSONObject output = new JSONObject(responseBody);
                        temp.putString("api_hits",output.getString("api_hits"));
                        networkServiceCB.onSuccess(temp);
                    }
                    else {
                        temp.putString("message", "HTTP Error Code : " + response.code());
                        networkServiceCB.onFaliure(temp);
                    }

                } catch (IOException|JSONException iox) {
                    Logger.e("Exception : " + iox.toString());
                    temp.putString("message", "Exception : " + iox.toString());
                    networkServiceCB.onFaliure(temp);
                }
            }
        }).start();
    }
}
