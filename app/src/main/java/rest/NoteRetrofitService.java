package rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sends.Ack;
import sends.Note;

/**
 * Created by Michał on 2018-04-07.
 */

public interface NoteRetrofitService {

    @GET("note")
    Call<List<Note>> getNotes(@Query("token") String token);

    @POST("note")
    Call<Ack> addNote(@Body Note note);

    @DELETE("note")
    Call<Ack> deleteNote(@Query("id") long id, @Query("token") String token);
}
