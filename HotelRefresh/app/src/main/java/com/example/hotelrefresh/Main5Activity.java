package com.example.hotelrefresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Database.SQLiteHelper;

public class Main5Activity extends AppCompatActivity {

    private Button btn_inq;
    private TextView tv_name, tv_details, tv_details2, tv_occ, tv_area, tv_height;
    private ImageView img_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        init();

        final int venueid = getIntent().getIntExtra("venueid", 0);
        int imgid = getIntent().getIntExtra("imgid", 0);

        if (venueid == 0) {
            System.out.println("Vinue ID == 0 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            Intent intent = new Intent(this, ActivityVenues.class);
            startActivity(intent);
        } else {

            SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
            SQLiteDatabase database = helper.getWritableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM venue WHERE ve_id= '" + venueid + "'", null);

            if (cursor.moveToNext()) {

                System.out.println(cursor.getString(0));
                System.out.println(cursor.getString(1));
                System.out.println(cursor.getString(2));
                System.out.println(cursor.getString(3));
                System.out.println(cursor.getString(4));
                System.out.println(cursor.getString(5));
                System.out.println(cursor.getString(6));
                System.out.println(cursor.getString(7));
                System.out.println(cursor.getString(8));


                tv_name.setText(cursor.getString(1));
                tv_details.setText(cursor.getString(6));
                tv_details2.setText(cursor.getString(7));
                tv_occ.setText(cursor.getInt(2) + "");
                tv_area.setText(cursor.getDouble(3) + "");
                tv_height.setText(cursor.getDouble(4) + "");

                img_view.setImageResource(imgid);

            } else {
                System.out.println("Cursor not move >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Intent intent = new Intent(this, ActivityVenues.class);
                startActivity(intent);
            }

        }

        btn_inq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main5Activity.this,ActivityPackageViewC.class);
                intent.putExtra("id",venueid);
                intent.putExtra("name",tv_name.getText().toString());
                startActivity(intent);
            }
        });

    }

    private void init() {

        btn_inq = findViewById(R.id.btn_inqu);

        tv_name = findViewById(R.id.txt_vname);
        tv_details = findViewById(R.id.txt_vdetails);
        tv_details2 = findViewById(R.id.txt_vdetails2);
        tv_occ = findViewById(R.id.txt_vocc);
        tv_area = findViewById(R.id.txt_varea);
        tv_height = findViewById(R.id.txt_vheight);

        img_view = findViewById(R.id.img_view_venue);

    }
}
