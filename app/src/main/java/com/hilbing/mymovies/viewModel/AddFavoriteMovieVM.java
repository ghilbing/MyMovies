package com.hilbing.mymovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.hilbing.mymovies.roomdb.AplicationDatabase;
import com.hilbing.mymovies.roomdb.Favorite;

public class AddFavoriteMovieVM extends ViewModel {

  private LiveData<Favorite> favoriteLiveData;

  public AddFavoriteMovieVM(AplicationDatabase database, int favoriteId){
      favoriteLiveData = database.favoriteDAO().loadFavoriteById(favoriteId);
  }

  public LiveData<Favorite> getFavoriteLiveData(){
      return favoriteLiveData;
  }

}
