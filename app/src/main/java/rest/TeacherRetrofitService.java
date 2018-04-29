package rest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sends.ResultTeacher;
import sends.Teacher;

/**
 * Created by Michał on 2018-04-06.
 */

public interface TeacherRetrofitService {

    @GET("teacher")
    Call<Teacher> getTeacher(@Query("name") String name, @Query("password") String password);

    @POST("teacher")
    Call<ResultTeacher> addTeacher(@Body Teacher teacher);
}
