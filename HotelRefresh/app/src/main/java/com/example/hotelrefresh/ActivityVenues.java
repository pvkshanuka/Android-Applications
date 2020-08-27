package com.example.hotelrefresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Database.SQLiteHelper;
import Models.Venue;

public class ActivityVenues extends AppCompatActivity {

    RecyclerView rview;
    ArrayList<Venue> venue_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);

        init();

        loadVenues();

    }


    private void init() {

        rview = findViewById(R.id.r_view);

    }

    private void loadVenues() {

        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM venue WHERE status = 1", null);

        venue_list = new ArrayList<>();

        while (cursor.moveToNext()) {
            venue_list.add(new Venue(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5), cursor.getInt(6)));

            System.out.println("_________________________");
            System.out.println(cursor.getInt(0));
            System.out.println(cursor.getString(1));
            System.out.println(cursor.getInt(2));
            System.out.println(cursor.getDouble(3));
            System.out.println(cursor.getDouble(4));
            System.out.println(cursor.getString(5));
            System.out.println(cursor.getInt(6));

        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rview.setLayoutManager(linearLayoutManager);

        VenueViewAdapter adapter = new VenueViewAdapter(venue_list, getApplicationContext());
        rview.setAdapter(adapter);

        cursor = database.rawQuery("SELECT * FROM customer WHERE status = 1", null);

        while (cursor.moveToNext()) {
            System.out.println("_________________________");
            System.out.println(cursor.getInt(0));
            System.out.println(cursor.getString(1));
            System.out.println(cursor.getString(2));
            System.out.println(cursor.getString(3));
            System.out.println(cursor.getInt(4));

        }

    }
}

class VenueViewAdapter extends RecyclerView.Adapter {

    ArrayList<Venue> venue_list;
    Context context;

    public VenueViewAdapter(ArrayList<Venue> venue_list, Context context) {
        this.venue_list = venue_list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.venue_view, parent, false);
        VenueViewHolder viewHolder = new VenueViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final Venue venue = venue_list.get(position);
        int num = position;
        VenueViewHolder viewHolder = (VenueViewHolder) holder;

        if (num > 7) {
            num = 1;
        } else {
            num = position;
        }

        viewHolder.tv_name.setText(venue.getName());
        final int id = context.getResources().getIdentifier("a" + (++num), "drawable", context.getPackageName());
        viewHolder.img_btn.setImageResource(id);

        viewHolder.img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Main5Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("venueid", venue.getId());
                intent.putExtra("imgid", id);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return venue_list.size();
    }
}

class VenueViewHolder extends RecyclerView.ViewHolder {

    public ImageButton img_btn;
    public TextView tv_name;

    public VenueViewHolder(@NonNull View itemView) {
        super(itemView);

        img_btn = itemView.findViewById(R.id.img_btn);
        tv_name = itemView.findViewById(R.id.tv_name);
    }
}
