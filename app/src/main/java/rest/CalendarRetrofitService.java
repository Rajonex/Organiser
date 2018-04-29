package rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sends.GroupCalendar;

/**
 * Created by micha on 13.04.2018.
 */

public interface CalendarRetrofitService {
    @GET("calendar/{token}")
    Call<List<GroupCalendar>> getTeachersCalendar(@Path("token") String token);

    @GET("calendar")
    Call<List<GroupCalendar>> getCalendarByDate(@Query("token") String token, @Query("day") String day);
}
