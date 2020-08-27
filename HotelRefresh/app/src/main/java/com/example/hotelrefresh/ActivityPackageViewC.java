package com.example.hotelrefresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Database.SQLiteHelper;
import Models.Package;
import Models.Venue;

public class ActivityPackageViewC extends AppCompatActivity {

    TextView tv_vname;
    RecyclerView rv_packages;

    ArrayList<Package> packages_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_view_c);

        init();



        int id = getIntent().getIntExtra("id", 0);

        tv_vname.setText(getIntent().getStringExtra("name"));

        loadPackages(id);

    }

    private void init() {

        tv_vname = findViewById(R.id.tv_vename);

        rv_packages = findViewById(R.id.rv_pviewc);

    }

    private void loadPackages(int id) {

        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM package WHERE status = 1 AND ve_id = '" + id + "'", null);

        packages_list = new ArrayList<>();

        while (cursor.moveToNext()) {
            packages_list.add(new Package(cursor.getInt(1), cursor.getInt(2), cursor.getDouble(3), cursor.getDouble(4),cursor.getInt(0)));

            System.out.println("_________________________");
            System.out.println(cursor.getString(1));
            System.out.println(cursor.getInt(2));
            System.out.println(cursor.getDouble(3));
            System.out.println(cursor.getDouble(4));

        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_packages.setLayoutManager(linearLayoutManager);

        PackageViewAdapter adapter = new PackageViewAdapter(packages_list, ActivityPackageViewC.this,getSupportFragmentManager());
        rv_packages.setAdapter(adapter);

    }
}

class PackageViewAdapter extends RecyclerView.Adapter {

    ArrayList<Package> package_list;
    Context context;
    FragmentManager fragmentManager;

    public PackageViewAdapter(ArrayList<Package> package_list, Context context,FragmentManager fragmentManager) {
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

        viewHolder.btn_addpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.customer != null) {

                    DialogSelectPackage dialogSelectPackage = new DialogSelectPackage(pack.getId());

                    dialogSelectPackage.show(fragmentManager,"");

                } else {
                    Toast.makeText(context, "Please Load Customer First", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return package_list.size();
    }
}

class PackageViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_hours, tv_price, tv_offer;
    public Button btn_addpackage;

    public PackageViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_hours = itemView.findViewById(R.id.tv_pvchours);
        tv_price = itemView.findViewById(R.id.tv_pvcprice);
        tv_offer = itemView.findViewById(R.id.tv_pvcoffer);

        btn_addpackage = itemView.findViewById(R.id.btn_pvcselectpackage);

    }
}
