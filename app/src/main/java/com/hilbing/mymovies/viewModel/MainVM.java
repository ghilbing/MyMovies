package com.hilbing.mymovies.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hilbing.mymovies.roomdb.AplicationDatabase;
import com.hilbing.mymovies.roomdb.Favorite;

import java.util.List;

public class MainVM extends AndroidViewModel {

    private static final String TAG = MainVM.class.getSimpleName();
    private LiveData<List<Favorite>> favorite;


    public MainVM(@NonNull Application application) {
        super(application);
        AplicationDatabase database = AplicationDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Retrieving data");
        favorite = database.favoriteDAO().loadAllFavorites();
    }

    public LiveData<List<Favorite>> getFavorite(){
        return favorite;
    }
}
