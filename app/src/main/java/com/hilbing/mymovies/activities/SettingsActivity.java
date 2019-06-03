package com.hilbing.mymovies.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.hilbing.mymovies.fragments.MainSettingsFragment;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainSettingsFragment()).commit();
    }






}
