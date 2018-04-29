package rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sends.Ack;
import sends.Group;
import sends.MiniGroup;

/**
 * Created by micha on 12.04.2018.
 */

public interface GroupRetrofitService {

    @POST("group")
    Call<Ack> addGroup(@Body Group group);

    @GET("group/{token}")
    Call<List<MiniGroup>> getMiniGroups(@Path("token") String token);

    @GET("group")
    Call<Group> getGroup(@Query("id") long id, @Query("token") String token);
}
