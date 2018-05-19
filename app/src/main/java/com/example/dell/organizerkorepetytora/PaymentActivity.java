package com.example.dell.organizerkorepetytora;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import dialog.PaymentDialogFragment;
import rest.SaldoRetrofitService;
import rest.StudentRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.MiniGroup;
import sends.Saldo;
import sends.Student;
import utils.Adress;

public class PaymentActivity extends AppCompatActivity {


    public static final String PREFS = "teacherToken";

    //    List<Student> listOfStudents;
    private TableLayout tableLayout;

    private SharedPreferences teacherToken;
    private String token;

    private ImageButton buttonHome;
    private ImageButton buttonRight;
    private ImageButton buttonLeft;
    private TextView lessonMonth;

    //    TableRow tableRow;
    private TextView nameLastname;
    private TextView nrLessonsDone;
    private TextView sumToPay;
    private TextView sumPayed;

    private long groupId;
    private int year;
    private int month;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        Bundle bundle = getIntent().getExtras();
        groupId = bundle.getLong("groupId");
        year = bundle.getInt("year");
        month = bundle.getInt("month");

        Toolbar toolbar = (Toolbar) findViewById(R.id.appBarArrow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initializeElements();
        initializeActions();
    }


    public void initializeElements() {
        teacherToken = getSharedPreferences(PREFS, 0);
        token = teacherToken.getString("token", "brak tokenu");

        buttonHome = (ImageButton) findViewById(R.id.button_home);
        buttonRight = (ImageButton) findViewById(R.id.button_aright);
        buttonLeft = (ImageButton) findViewById(R.id.button_aleft);

        lessonMonth = (TextView) findViewById(R.id.lessons_month);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

//        TableRow tableRow = new TableRow(this);
//        View view = LayoutInflater.from(this).inflate(R.layout.table_element_payment, null, false);
//        nameLastname = (TextView) view.findViewById(R.id.name_lastname);
//        nrLessonsDone = (TextView) view.findViewById(R.id.nr_lessons_done);
//        sumToPay = (TextView) view.findViewById(R.id.sum_to_pay);
//        sumPayed = (TextView) view.findViewById(R.id.sum_payed);
//
//        nameLastname.setText("Student");
//        nrLessonsDone.setText("Lekcje");
//        sumToPay.setText("Do zapłaty");
//        sumPayed.setText("Zapłacono");
//
//        tableRow.addView(view);
//        tableLayout.addView(tableRow);

        getSaldos(this, month, year, groupId);

    }

    public void initializeActions() {

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentActivity.this, FirstScreenActivity.class));
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                tableLayout.removeAllViewsInLayout();
                month++;
                if(month>12)
                {
                    year++;
                    month = 1;
                }
                getSaldos(PaymentActivity.this, month, year, groupId);
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeAllViewsInLayout();
                month--;
                if(month<1)
                {
                    year--;
                    month = 12;
                }
                getSaldos(PaymentActivity.this, month, year, groupId);
//                Intent appInfo = new Intent(PaymentActivity.this, PaymentActivity.class);
//
//                Bundle bundle = new Bundle();
//                bundle.putInt("year", year);
//                bundle.putInt("month", month-1);
//                bundle.putLong("groupId", groupId);
//                appInfo.putExtras(bundle);
//
//                startActivity(appInfo);
            }
        });
    }

    private void getSaldos(Context context, int month, int year, long groupId) {
        // Ustawianie daty
        lessonMonth.setText(""+month+"."+year);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

        SaldoRetrofitService saldoRetrofitService = retrofit.create(SaldoRetrofitService.class);

        Call<List<Saldo>> callSaldo = saldoRetrofitService.getSaldos(token, month, year, groupId);

        System.out.println(month + " " + year + ", grupa; " + groupId);
        TableRow tableRow = new TableRow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.table_element_payment, null, false);
        nameLastname = (TextView) view.findViewById(R.id.name_lastname);
        nrLessonsDone = (TextView) view.findViewById(R.id.nr_lessons_done);
        sumToPay = (TextView) view.findViewById(R.id.sum_to_pay);
        sumPayed = (TextView) view.findViewById(R.id.sum_payed);

        nameLastname.setText("Student");
        nrLessonsDone.setText("Lekcje");
        sumToPay.setText("Do zapłaty");
        sumPayed.setText("Zapłacono");

        tableRow.addView(view);
        tableLayout.addView(tableRow);

        callSaldo.enqueue(new Callback<List<Saldo>>() {
            @Override
            public void onResponse(Call<List<Saldo>> call, Response<List<Saldo>> response) {
                List<Saldo> saldos = response.body();
                System.out.println(saldos);
                if (saldos != null) {
                    for (Saldo saldo : saldos) {
                        TableRow tableRow = new TableRow(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.table_element_payment, null, false);
                        nameLastname = (TextView) view.findViewById(R.id.name_lastname);
                        nrLessonsDone = (TextView) view.findViewById(R.id.nr_lessons_done);
                        sumToPay = (TextView) view.findViewById(R.id.sum_to_pay);
                        sumPayed = (TextView) view.findViewById(R.id.sum_payed);


                        System.out.println(saldo.getStudent().getFirstname());

                        nameLastname.setText(saldo.getStudent().getFirstname() + " " + saldo.getStudent().getLastname());
                        nrLessonsDone.setText(new Integer(saldo.getLessonsNumber()).toString());
                        sumToPay.setText(new Double(saldo.getToPay()).toString());
                        sumPayed.setText(new Double(saldo.getPaid()).toString());

                        tableRow.setOnClickListener(new TableRowClickListener(saldo, month, year));

                        tableRow.addView(view);
                        tableLayout.addView(tableRow);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Saldo>> call, Throwable t) {
                System.out.println("Nie udalo sie polaczyc");
            }
        });
    }


    private class TableRowClickListener implements View.OnClickListener {
        private Saldo saldo;
        private int month;
        private int year;

        public TableRowClickListener(Saldo saldo, int month, int year) {
            this.saldo = saldo;
            this.month = month;
            this.year = year;
        }

        @Override
        public void onClick(View view) {
            PaymentDialogFragment paymentDialogFragment = new PaymentDialogFragment();
            paymentDialogFragment.setMonth(month);
            paymentDialogFragment.setYear(year);
            paymentDialogFragment.setSaldo(saldo);
            paymentDialogFragment.setContext(PaymentActivity.this);
            paymentDialogFragment.show(getFragmentManager(), null);
        }
    }


}
