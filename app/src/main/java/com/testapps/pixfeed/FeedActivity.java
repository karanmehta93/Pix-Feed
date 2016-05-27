package com.testapps.pixfeed;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, Integer> allPreferences = (Map<String, Integer>)sharedPreferences.getAll();

    }
}
