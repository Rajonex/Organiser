package rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sends.Ack;
import sends.GroupCalendar;
import sends.Lesson;
import sends.MiniLesson;

/**
 * Created by micha on 13.04.2018.
 */

public interface LessonRetrofitService {

    @GET("lesson")
    Call<List<MiniLesson>> getGroupLessons(@Query("groupid") long id, @Query("token") String token);

    @GET("lesson/{id}")
    Call<Lesson> getLesson(@Path("id") long id, @Query("token") String token);

    @POST("lesson")
    Call<Ack> addLesson(@Body Lesson lesson);
}
