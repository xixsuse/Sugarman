package com.sugarman.myb.di.modules;

import android.content.ContentResolver;
import android.content.Context;
import com.google.gson.Gson;
import com.sugarman.myb.api.ApiRx;
import com.sugarman.myb.api.RestApi;
import com.sugarman.myb.api.RestApiSpika;
import com.sugarman.myb.api.SpikaOSRetroApiInterfaceRx;
import com.sugarman.myb.data.DataManager;
import com.sugarman.myb.data.db.DbHelper;
import com.sugarman.myb.data.local.PreferencesHelper;
import com.sugarman.myb.di.scopes.AppScope;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Vrungel on 26.01.2017.
 */

@Module(includes = { RetrofitModule.class }) public class DataModule {

  @Provides @AppScope SpikaOSRetroApiInterfaceRx provideSpikaApi(@Named("Spika") Retrofit retrofit) {
    return retrofit.create(SpikaOSRetroApiInterfaceRx.class);
  }

  @Provides @AppScope ApiRx provideApi(@Named("Sugarman") Retrofit retrofit) {
    return retrofit.create(ApiRx.class);
  }

  @Provides @AppScope RestApi provideRestApi(ApiRx api) {
    return new RestApi(api);
  }

  @Provides @AppScope RestApiSpika provideRestApiSpika(SpikaOSRetroApiInterfaceRx api) {
    return new RestApiSpika(api);
  }

  @Provides @AppScope public ContentResolver provideContentResolver(Context context) {
    return context.getContentResolver();
  }

  @Provides @AppScope DbHelper provideDbHelper() {
    return new DbHelper();
  }

  @Provides @AppScope DataManager provideDataManager(RestApiSpika restApiSpika, RestApi restApi,
      PreferencesHelper preferencesHelper, ContentResolver contentResolver, DbHelper dbHelper) {
    return new DataManager(restApiSpika,restApi, preferencesHelper, contentResolver, dbHelper);
  }

  @Provides @AppScope PreferencesHelper providePreferencesHelper(Context context, Gson gson) {
    return new PreferencesHelper(context, gson);
  }
}
