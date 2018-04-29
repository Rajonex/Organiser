package rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import sends.Ack;
import sends.Group;
import sends.Student;

/**
 * Created by micha on 12.04.2018.
 */

public interface StudentRetrofitService {

    @GET("student")
    Call<List<Student>> getStudents(@Query("token") String token);

    @POST("student")
    Call<Ack> addStudent(@Body Student student);

    @PUT("student")
    Call<Ack> updateStudent(@Body Student student);
}
