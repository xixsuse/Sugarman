package com.sugarman.myb.api.clients;

import com.sugarman.myb.App;
import com.sugarman.myb.api.models.requests.CheckPhoneRequest;
import com.sugarman.myb.api.models.responses.CheckPhoneResponse;
import com.sugarman.myb.listeners.ApiBaseListener;
import com.sugarman.myb.listeners.ApiCheckPhoneListener;
import java.lang.ref.WeakReference;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by yegoryeriomin on 10/11/17.
 */

public class CheckPhonesClient extends BaseApiClient {
  private static final String TAG = CheckPhonesClient.class.getName();
  @Override public void registerListener(ApiBaseListener listener) {
    clientListener = new WeakReference<>(listener);
  }

  private final Callback<CheckPhoneResponse> mCallback = new Callback<CheckPhoneResponse>() {

    @Override public void onResponse(Call<CheckPhoneResponse> call, Response<CheckPhoneResponse> dataResponse) {
      ResponseBody errorBody = dataResponse.errorBody();
      if (dataResponse != null) {
        Timber.e("SET INVITABLE 0");
        ((ApiCheckPhoneListener) clientListener.get()).onApiCheckPhoneSuccess(dataResponse.body().getPhones());
      } else if (errorBody != null) {
        String errorMessage = parseErrorBody(errorBody);
        responseFailure(TAG, errorMessage);
        if (dataResponse.code() == 401) {
          clientListener.get().onApiUnauthorized();
        } else if (dataResponse.code() == OLD_VERSION_CODE) {
          clientListener.get().onUpdateOldVersion();
        } else {
          ((ApiCheckPhoneListener) clientListener.get()).onApiCheckPhoneFailure(errorMessage);
        }
      } else {
        responseIsNull(TAG);
        ((ApiCheckPhoneListener) clientListener.get()).onApiCheckPhoneFailure(RESPONSE_IS_NULL);
      }
  }



    @Override public void onFailure(Call<CheckPhoneResponse> call, Throwable t) {

    }
  };

  public void checkPhones(List<String> phones)
  {

    CheckPhoneRequest request = new CheckPhoneRequest();
    request.setPhones(phones);

    Call<CheckPhoneResponse> call = App.getApiInstance().checkPhone(request);
    call.enqueue(mCallback);
}
}
