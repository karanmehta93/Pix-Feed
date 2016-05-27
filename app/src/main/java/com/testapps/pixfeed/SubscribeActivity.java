package com.testapps.pixfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SubscribeActivity extends AppCompatActivity {


    Button subscribeButton;
    EditText channelNameEditText;
    Button viewFeedButton;
    public static final String Preferences = "MySubscriptions";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        channelNameEditText = (EditText) findViewById(R.id.channelNameEditText);

        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(subscribeButtonHandler);

        viewFeedButton = (Button) findViewById(R.id.viewFeedButton);
        viewFeedButton.setOnClickListener(viewFeedButtonHandler);

        sharedPreferences = getSharedPreferences(Preferences, Context.MODE_PRIVATE);
    }

    View.OnClickListener subscribeButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("SubscribeActivity", "Subscribe Button Clicked");
            final String channelName = channelNameEditText.getText().toString().trim().toLowerCase();
            if (sharedPreferences.getInt(channelName, -1) != -1) {
                Toast.makeText(getApplicationContext(), "Already subscribed to this channel", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "https://testing-rahulskn86.c9users.io/subscribe/" + channelName;
            StringRequest subscribeButtonRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            Log.d("Response", s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONObject getOffset = new JSONObject(jsonObject.get(channelName).toString());
                                Log.d("Json", getOffset.get("0").toString());
                                int channelOffset = Integer.parseInt(getOffset.get("0").toString().trim().replaceAll("\\[", "").replaceAll("\\]", ""));
                                editor = sharedPreferences.edit();
                                editor.putInt(channelName, channelOffset);
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "Channel Subscribed: " + channelName + "  " + channelOffset, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Topic not found", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), "Internet Connection error. Timeout!", Toast.LENGTH_LONG).show();
                        }
                    });
            Volley.newRequestQueue(getApplicationContext()).add(subscribeButtonRequest);
        }
    };

    View.OnClickListener viewFeedButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentToFeedActivity = new Intent(getApplicationContext(), FeedActivity.class);
            startActivity(intentToFeedActivity);
        }
    };
}
