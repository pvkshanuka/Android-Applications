package com.testing.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import connectionsqlite.DBConnection;
import model.Order;
import model.OrderAdapter;

public class ViewOrdersActivity extends AppCompatActivity implements AdapterCallback{

    private SimpleDateFormat sdf;
    private DBConnection connection;
    private SQLiteDatabase database;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        userid = getIntent().getIntExtra("userid", 0);
        connection = new DBConnection(this);
        database = connection.getWritableDatabase();
        sdf = new SimpleDateFormat("yyy/MM/dd");
        loadOrderRecycler();
    }


    private void loadOrderRecycler() {
        RecyclerView recyclerView = findViewById(R.id.orderrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Order> olist = new ArrayList<>();
        String query1 = "SELECT * FROM orders WHERE dri_id = '" + userid + "' AND status = 1";
        Cursor cursor = database.rawQuery(query1, null);

        while (cursor.moveToNext()) {
            Order o = new Order(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    new Date(cursor.getLong(4)),
                    new Date(cursor.getLong(5)),
                    cursor.getInt(7)
            );
            olist.add(o);
        }

        OrderAdapter adapter = new OrderAdapter(olist,ViewOrdersActivity.this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onMethodCallback() {
        loadOrderRecycler();
    }
}
