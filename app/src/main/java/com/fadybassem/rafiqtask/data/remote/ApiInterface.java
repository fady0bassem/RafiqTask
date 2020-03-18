package com.fadybassem.rafiqtask.data.remote;

import com.fadybassem.rafiqtask.data.remote.pojo.LocationDataModel;
import com.fadybassem.rafiqtask.data.remote.pojo.LocationModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("getLocationAutocompleteV1?localeCode=eg")
    @FormUrlEncoded
    Call<LocationModel> LOAD_HOME_MODEL_CALL(@Field("query") String query);


    @POST("getLocationDetailsV1?localeCode=eg")
    @FormUrlEncoded
    Call<LocationDataModel> LOCATION_DATA_MODEL_CALL(@Field("id") String id,
                                                     @Field("provider") String provider,
                                                     @Field("addressLine1") String addressLine1,
                                                     @Field("addressLine2") String addressLine2
    );
}