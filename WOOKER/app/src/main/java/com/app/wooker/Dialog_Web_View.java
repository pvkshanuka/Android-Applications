package com.app.wooker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Dialog_Web_View extends AppCompatDialogFragment {

    TextView tv_test;
    WebView web_view;

    ProgressBar progressBar;

    RequestQueue queue;

    String uid;
    String web_url;
    String worker_type;
    String date;

    public Dialog_Web_View() {
    }

    @SuppressLint("ValidFragment")
    public Dialog_Web_View(String uid, String worker_type, String date, String web_url) {
        this.uid = uid;
        this.worker_type = worker_type;
        this.date = date;
        this.web_url = web_url;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_web_view, null);

        builder.setView(view)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog_Web_View.this.dismiss();
                        ClientMain.fragmentController.setupDialog(new Dialog_Worker_Details(uid, worker_type, date));
                    }
                });

        init(view);

        doProcess();

        return builder.create();
    }


    private void init(View view) {
//        tv_test = view.findViewById(R.id.tv_web_view);
        web_view = view.findViewById(R.id.web_view);
        progressBar = view.findViewById(R.id.progressBar_wv);
    }

    private void doProcess() {

        queue = Volley.newRequestQueue(getActivity());

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, web_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        web_view.getSettings().setJavaScriptEnabled(true);
//                        web_view.loadData(response, "text/html; charset=utf-8", "UTF-8");
//                        web_view.loadDataWithBaseURL("", response, "text/html", "UTF-8", "");
//                        web_view.loadDataWithBaseURL(null, response, "text/html", "utf-8", null);
                        web_view.loadData(response, "text/html", "UTF-8");
                        web_view.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            tv_test.setText(Html.fromHtml(response, Html.FROM_HTML_MODE_COMPACT));
//                        }else{
//                            tv_test.setText(Html.fromHtml(response));
//                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getActivity(), error.getMessage()).show();
            }
        });

        stringRequest.setTag("TAG");

        queue.add(stringRequest);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        queue.cancelAll("TAG");
    }

}
