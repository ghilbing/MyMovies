package com.hilbing.mymovies.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

import com.hilbing.mymovies.R;

public class MainSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    private static final String TAG = MainSettingsFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        //Go to all the preferences and setup the summary
        for (int i = 0; i <count ; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            String value = sharedPreferences.getString(preference.getKey(), "");
            setPreferenceSummary(preference, value);
        }
        Preference preference = findPreference(getString(R.string.pref_sort_order_key));
        preference.setOnPreferenceChangeListener(this);

    }

    private void setPreferenceSummary(Preference preference, String value){
        if (preference instanceof ListPreference){
            //Figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index >= 0){
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        }
    }

//    private static void bindSummary(Preference preference){
//        preference.setOnPreferenceChangeListener(listener);
//        listener.onPreferenceChange(preference,
//                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
//    }

//    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object newValue) {
//            String stringSortSelected = newValue.toString();
//            if (preference instanceof ListPreference){
//                ListPreference listPreference = (ListPreference) preference;
//                int index = listPreference.findIndexOfValue(stringSortSelected);
//                //Set the summary to show the value
//                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
//            }
//
//            return true;
//        }
//    };


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Which preference was changed
        Preference preference = findPreference(key);
        if (null != preference){
            //update summary
            if (!(preference instanceof CheckBoxPreference)){
                String prefValue = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, prefValue);
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringSortSelected = newValue.toString();
        if (preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringSortSelected);
            //Set the summary to show the value
            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
        }

        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
