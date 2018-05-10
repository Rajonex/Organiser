package rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import sends.Ack;
import sends.Saldo;
import sends.Student;

/**
 * Created by micha on 10.05.2018.
 */

public interface SaldoRetrofitService {

    @GET("saldo")
    Call<List<Saldo>> getSaldos(@Query("token") String token, @Query("month") int month, @Query("year") int year, @Query("groupId") long groupId);

    @PUT("saldo")
    Call<Ack> updateSaldo(@Body Saldo saldo);
}
