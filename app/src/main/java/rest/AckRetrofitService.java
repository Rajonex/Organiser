package rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import sends.Ack;

//import retrofit.Callback;
//import retrofit.http.GET;


//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.http.GET;

/**
 * Created by Zofia on 4/5/2018.
 */

public interface AckRetrofitService {

    @GET("hello")
    Call<List<Ack>> getAll();

    @POST("hello")
    Call<Ack> sendAcks(@Body List<Ack> acks);

//    @GET("/hello")
//    void getAll(Callback<List<Ack>> callback);
}
