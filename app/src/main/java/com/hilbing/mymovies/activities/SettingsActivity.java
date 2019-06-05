package com.hilbing.mymovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.hilbing.mymovies.R;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            final Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (upIntent == null){
                onBackPressed();
            } else {
                //optionally add this flag to the intent:Intent.FRAG_ACTIVITY_SINGLE_TOP
                NavUtils.navigateUpTo(this, upIntent);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

}
