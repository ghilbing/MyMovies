package com.hilbing.mymovies.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.hilbing.mymovies.roomdb.AplicationDatabase;

public class AddFavoriteMovieVMFactory extends ViewModelProvider.NewInstanceFactory {

    private final AplicationDatabase mDataBase;
    private final int mFavoriteId;

    public AddFavoriteMovieVMFactory(AplicationDatabase database, int favoriteId){
        mDataBase = database;
        mFavoriteId = favoriteId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new AddFavoriteMovieVM(mDataBase, mFavoriteId);
    }

}
