package com.example.hotelrefresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import Classes.Validation;
import Database.SQLiteHelper;
import Models.Package;

public class ActivityPackageViewA extends AppCompatActivity {

    Spinner spinner;
    RecyclerView recyclerView;
    ArrayList<String> venues_list;
    ArrayList<Package> packages_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_view);

        init();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        final PackageViewAdapter2 adapter = new PackageViewAdapter2(packages_list, ActivityPackageViewA.this, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (Validation.isInt(spinner.getSelectedItem().toString().split("-")[0])) {

                    int id = Integer.parseInt(spinner.getSelectedItem().toString().split("-")[0]);

                    SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
                    SQLiteDatabase database = helper.getWritableDatabase();
                    Cursor cursor = database.rawQuery("SELECT * FROM package WHERE ve_id='" + id + "' AND status = 1", null);

                    packages_list.removeAll(packages_list);
                    while (cursor.moveToNext()) {
                        packages_list.add(new Package(cursor.getInt(1), cursor.getInt(2), cursor.getDouble(3), cursor.getDouble(4),cursor.getInt(0)));

                        System.out.println("_________________________");
                        System.out.println(cursor.getString(1));
                        System.out.println(cursor.getInt(2));
                        System.out.println(cursor.getDouble(3));
                        System.out.println(cursor.getDouble(4));
                        System.out.println(cursor.getDouble(5));

                    }


                } else {
                    packages_list.removeAll(packages_list);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void init() {

        spinner = findViewById(R.id.sp_venuea);
        recyclerView = findViewById(R.id.rv_pviewa);

        venues_list = new ArrayList<>();
        venues_list.add("Select Venue");

        packages_list = new ArrayList<>();

        SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM venue WHERE status = 1", null);

        while (cursor.moveToNext()) {
            venues_list.add(cursor.getString(0) + "- " + cursor.getString(1));
            System.out.println("________________________________");
            System.out.println(cursor.getString(0));
            System.out.println(cursor.getString(1));
            System.out.println("________________________________");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityPackageViewA.this, R.layout.my_simple_list_item1, venues_list);
        adapter.setDropDownViewResource(R.layout.my_spinner);

        spinner.setAdapter(adapter);

    }
}

class PackageViewAdapter2 extends RecyclerView.Adapter {

    ArrayList<Package> package_list;
    Context context;
    FragmentManager fragmentManager;

    public PackageViewAdapter2(ArrayList<Package> package_list, Context context, FragmentManager fragmentManager) {
        this.package_list = package_list;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.package_view_c, parent, false);
        PackageViewHolder viewHolder = new PackageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final Package pack = package_list.get(position);

        PackageViewHolder viewHolder = (PackageViewHolder) holder;

        viewHolder.tv_hours.setText(pack.getHours() + "");
        viewHolder.tv_price.setText(pack.getPrice() + "");
        viewHolder.tv_offer.setText(pack.getOffer() + "%");

        viewHolder.btn_addpackage.setText("Edit Package");

        viewHolder.btn_addpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,ActivityPackageEdit.class);
                intent.putExtra("pid",pack.getPakid());
                intent.putExtra("hours",pack.getHours());
                intent.putExtra("price",pack.getPrice());
                intent.putExtra("offer",pack.getOffer());

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return package_list.size();
    }
}
