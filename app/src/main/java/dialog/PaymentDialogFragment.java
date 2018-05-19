package dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import activities.PaymentActivity;
import com.example.dell.organizerkorepetytora.R;

import rest.SaldoRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sends.Ack;
import sends.Saldo;
import utils.Adress;

/**
 * Created by micha on 10.05.2018.
 */

public class PaymentDialogFragment extends DialogFragment {
    private Saldo saldo;
    private int year;
    private int month;
    private DialogFragment dialogFragment;
    private Context context;

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
        this.saldo = saldo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public PaymentDialogFragment()
    {
        dialogFragment = this;
//        Calendar calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_dialog, container, false);
        TextView name = (TextView) view.findViewById(R.id.name_lastname_pay);
        TextView toPay = (TextView) view.findViewById(R.id.to_pay);
        EditText paid = (EditText) view.findViewById(R.id.payed);
        Button acceptButton = (Button) view.findViewById(R.id.accept_payment);

        name.setText(saldo.getStudent().getFirstname() + " " + saldo.getStudent().getLastname());
        toPay.setText(new Double(saldo.getToPay()).toString());
        paid.setText(new Double(saldo.getPaid()).toString());

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double paidAmount = null;
                try{
                    paidAmount = Double.parseDouble(paid.getText().toString());
                } catch(NumberFormatException er)
                {
                    Toast.makeText(getActivity(), "Proszę wpisać kwotę", Toast.LENGTH_SHORT).show();
                }
                if(paidAmount != null)
                {
                    System.out.println(paidAmount);
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(Adress.getAdress()).addConverterFactory(GsonConverterFactory.create()).build();

                    SaldoRetrofitService saldoRetrofitService = retrofit.create(SaldoRetrofitService.class);

                    saldo.setPaid(paidAmount);
                    Call<Ack> callSaldo = saldoRetrofitService.updateSaldo(saldo);

                    callSaldo.enqueue(new Callback<Ack>() {
                        @Override
                        public void onResponse(Call<Ack> call, Response<Ack> response) {
                            Ack ack = response.body();
                            // TODO w zaleznosci od powodzenia dodania rozne pop-upy lub inne info
                            if(ack != null && ack.isConfirm())
                            {

                            }
                            else
                            {

                            }
                            Intent appInfo = new Intent(context, PaymentActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putInt("year", getYear());
                            bundle.putInt("month", getMonth());
                            bundle.putLong("groupId", saldo.getGroupId());
                            appInfo.putExtras(bundle);

                            startActivity(appInfo);
                        }

                        @Override
                        public void onFailure(Call<Ack> call, Throwable t) {

                            Intent appInfo = new Intent(context, PaymentActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putInt("year", getYear());
                            bundle.putInt("month", getMonth());
                            bundle.putLong("groupId", saldo.getGroupId());
                            appInfo.putExtras(bundle);

                            startActivity(appInfo);
                        }
                    });

                    dialogFragment.getDialog().setCanceledOnTouchOutside(false);
                }

            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Niestandardowy dialog");
        return dialog;
    }
}
