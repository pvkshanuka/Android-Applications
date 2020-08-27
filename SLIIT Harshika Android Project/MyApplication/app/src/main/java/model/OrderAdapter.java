package model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.testing.myapplication.AdapterCallback;
import com.testing.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import connectionsqlite.DBConnection;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> implements View.OnClickListener {
    private ArrayList<Order> olist;
    private SimpleDateFormat sdf;
    private Activity context;
    private CountdownTimer timer;
    private DBConnection connection;
    private SQLiteDatabase database;

    //dialog variables
    private TextView orderid;
    private TextView daddress;
    private TextView paddress;
    private TextView pdate;
    private TextView progresstext;
    private ProgressBar progressBar;
    private Button acceptbtn;
    private Button declinebtn;
    private Button cancelbtn;
    private boolean iscancel;
    private Order temporder;
    private Dialog dialog;
    private AdapterCallback callback;


    public OrderAdapter(ArrayList<Order> o, Activity parentcontext) {
        olist = new ArrayList<>();
        this.olist = o;
        sdf = new SimpleDateFormat("yyy/MM/dd");
        iscancel = false;
        this.context = parentcontext;
        try {
            callback = (AdapterCallback) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlayout, parent, false);
        inflate.setOnClickListener(this);
        // context = parent.getContext();
        connection = new DBConnection(context);
        database = connection.getWritableDatabase();
        return new OrderViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = olist.get(position);
        holder.setData(order.getDaddress(), sdf.format(order.getPdate()), position);


    }

    @Override
    public int getItemCount() {
        return olist.size();
    }

    @Override
    public void onClick(View v) {
        View viewById = v.findViewById(R.id.or_layout);
        int tag = (int) viewById.getTag();
        Order order = olist.get(tag);
        temporder = order;
        // System.out.println(order.getDaddress());

        dialog = new Dialog(context, R.style.FullScreenDialogStyle);
        dialog.setContentView(R.layout.dialoglayout);
        orderid = dialog.findViewById(R.id.dialog_orderid);
        daddress = dialog.findViewById(R.id.dialog_daddress);
        paddress = dialog.findViewById(R.id.dialog_paddress);
        pdate = dialog.findViewById(R.id.dialog_pdate);
        acceptbtn = dialog.findViewById(R.id.dialog_accept);
        progresstext = dialog.findViewById(R.id.dialog_progresstext);
        progressBar = dialog.findViewById(R.id.dialog_progressBar);
        declinebtn = dialog.findViewById(R.id.dialog_decline);
        cancelbtn = dialog.findViewById(R.id.dialog_cancel);
        progressBar.setVisibility(View.GONE);
        progresstext.setVisibility(View.GONE);
        cancelbtn.setVisibility(View.GONE);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == declinebtn) {
                    iscancel = true;
                    acceptbtn.setVisibility(View.GONE);
                    declinebtn.setVisibility(View.GONE);

                    progressBar.setVisibility(View.VISIBLE);
                    progresstext.setVisibility(View.VISIBLE);
                    cancelbtn.setVisibility(View.VISIBLE);
                    timer = new CountdownTimer(10000, 100);
                    timer.start();
                } else if (v == acceptbtn) {
                   // System.out.println("accepted");
                    acceptOrder();
                } else if (v == cancelbtn) {
                    //System.out.println("Canceled");
                    // declineOrder();
                    iscancel = false;
                   // dialog.dismiss();
                    timer.cancel();
                    progressBar.setProgress(0);
                    progresstext.setText("0%");
                    cancelbtn.setVisibility(View.GONE);
                    progresstext.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    acceptbtn.setVisibility(View.VISIBLE);
                    declinebtn.setVisibility(View.VISIBLE);

                }
            }
        };
        declinebtn.setOnClickListener(listener);
        cancelbtn.setOnClickListener(listener);
        acceptbtn.setOnClickListener(listener);
        orderid.setText(order.getOrderid() + "");
        daddress.setText(order.getDaddress());
        paddress.setText(order.getPaddress());
        pdate.setText(sdf.format(order.getPdate()));

        dialog.show();
    }

    public class CountdownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished / 1000);
            int progressupdate = (int) (millisUntilFinished / 100);
            progressBar.setProgress(10 - progress);
            progresstext.setText(100 - progressupdate + "%");


        }

        @Override
        public void onFinish() {
            if (iscancel) {
                declineOrder();
            } else {
                cancelbtn.setVisibility(View.GONE);
                progresstext.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                acceptbtn.setVisibility(View.VISIBLE);
                declinebtn.setVisibility(View.VISIBLE);
            }
        }
    }

    public void declineOrder() {
        acceptbtn.setVisibility(View.VISIBLE);
        declinebtn.setVisibility(View.VISIBLE);
        cancelbtn.setVisibility(View.GONE);
        progresstext.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        if (iscancel) {
            System.out.println("declined");
            String query1 = "UPDATE orders SET status=2 WHERE or_id = '" + temporder.getOrderid() + "'";
            database.execSQL(query1);
            olist.remove(temporder);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Success");
            builder.setMessage("Order Declined !").
                    setCancelable(false);

            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface adialog, int which) {
                    dialog.dismiss();
                    adialog.dismiss();
                    try {
                        callback.onMethodCallback();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).setIcon(R.drawable.ic_done_black_24dp);
            builder.create().show();

        } else {
            System.out.println("Canceled");
            iscancel = true;
        }
    }

    private void acceptOrder(){
        System.out.println("declined");
        String query1 = "UPDATE orders SET status=3 WHERE or_id = '" + temporder.getOrderid() + "'";
        database.execSQL(query1);
        olist.remove(temporder);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Success");
        builder.setMessage("Order Accepted !").
                setCancelable(false);

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface adialog, int which) {
                dialog.dismiss();
                adialog.dismiss();
                try {
                    callback.onMethodCallback();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).setIcon(R.drawable.ic_done_black_24dp);
        builder.create().show();


    }


}
