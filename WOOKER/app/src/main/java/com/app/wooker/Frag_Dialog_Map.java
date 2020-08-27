package com.app.wooker;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.wooker.DBClasses.User;
import com.app.wooker.DBClasses.WorkerTypes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import CustomClasses.RunOnUIThread;
import es.dmoral.toasty.Toasty;

public class Frag_Dialog_Map extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    Spinner spin_online, spin_location;

    Marker marker_job;

    FirebaseFirestore fb;
    WorkerTypes workerType;
    User user;

    Map<String, Double> live_location;
    BitmapDescriptor marker_icon;
    ArrayList<String> uid_list;
    Timer ti = new Timer();
    List<Address> addresses = null;
    String city;
    String worker_type;
    String date;
    Button btn_list_view;

    public Frag_Dialog_Map() {
    }

    @SuppressLint("ValidFragment")
    public Frag_Dialog_Map(String worker_type, String date) {
        this.worker_type = worker_type;
        this.date = date;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_map, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(view);


        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mymap, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        return view;
    }

    private void init(View view) {

        btn_list_view = view.findViewById(R.id.btn_view_list);
        spin_online = view.findViewById(R.id.spinner_online);
        spin_location = view.findViewById(R.id.spinner_location);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_mymap);
        fb = FirebaseFirestore.getInstance();
        uid_list = new ArrayList<>();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (DialogMap.location != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(DialogMap.location, 12));
        }

        ti.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("RUN");
                loadMarkers();
            }
        }, 0, 10000);


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (marker.getSnippet() != null) {
                    Toasty.info(getActivity(), marker.getSnippet()).show();
                    ClientMain.fragmentController.setupDialog(new Dialog_Worker_Details(marker.getSnippet(),worker_type,date));
                }

                return true;
            }
        });

        try {
            Geocoder geocoder = new Geocoder(getActivity());
            addresses = geocoder.getFromLocation(DialogMap.location.latitude, DialogMap.location.longitude, 1);

            if (addresses.size() != 0) {
                System.out.println(addresses.get(0).toString());

                if (addresses.get(0).getLocality() != null) {
                    city = addresses.get(0).getLocality();
                } else if (addresses.get(0).getSubAdminArea() != null) {
                    city = addresses.get(0).getSubAdminArea();
                } else if (addresses.get(0).getFeatureName() != null) {
                    city = addresses.get(0).getFeatureName();
                }

                System.out.println("MARKER CITY- " + city);
                System.out.println(addresses.get(0).getLocality());
                System.out.println(addresses.get(0).getSubAdminArea());
                System.out.println(addresses.get(0).getFeatureName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMarkers() {

        if (DialogMap.location != null) {
            if (worker_type.equals("")) {
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fb.collection("worker_types")
                                .whereEqualTo("type", worker_type)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        if (!queryDocumentSnapshots.isEmpty()) {

                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                workerType = documentSnapshot.toObject(WorkerTypes.class);
                                                System.out.println(workerType.getWorkerid());

                                                if (!(uid_list.contains(workerType.getWorkerid()))) {
                                                    uid_list.add(workerType.getWorkerid());
                                                    System.out.println("ADDED UID=" + workerType.getWorkerid() + " " + uid_list.size());
                                                }

                                            }
                                        } else {
                                            Toasty.info(getActivity(), "No workers.!").show();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(getActivity(), e.getMessage()).show();
                            }
                        })
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            map.clear();

                                            RunOnUIThread.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (DialogMap.location != null) {
                                                        marker_job = map.addMarker(new MarkerOptions().position(DialogMap.location).title("Job Location"));
                                                        marker_job.showInfoWindow();
                                                        marker_job.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_marker_s));
                                                    }
                                                }
                                            });

                                            for (String uid : uid_list) {
                                                fb.collection("users").document(uid)
                                                        .get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                if (documentSnapshot.exists()) {
                                                                    user = documentSnapshot.toObject(User.class);

                                                                    if (user.isOnline()) {
                                                                        if (user.getLive_location() != null) {
                                                                            live_location = user.getLive_location();
                                                                        } else {
                                                                            live_location = user.getLocation();
                                                                        }
                                                                    } else {
                                                                        live_location = user.getLocation();
                                                                    }

                                                                    if (spin_online.getSelectedItemPosition() == 0) {
                                                                        if (user.isOnline()) {
                                                                            if (spin_location.getSelectedItemPosition() == 0) {

                                                                                try {


                                                                                    if (city.equals(user.getLive_loc_city())) {
                                                                                        map.addMarker(new MarkerOptions().position(new LatLng(live_location.get("latitude"), live_location.get("longitude")))
                                                                                                .title(user.getFname())
                                                                                                .icon(MainActivity.getBitmapDescriptorFromVector(getActivity(), R.drawable.ic_worker_y)))
                                                                                                .setSnippet(user.getUid());
                                                                                    }

                                                                                } catch (Exception e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            } else {

                                                                                map.addMarker(new MarkerOptions().position(new LatLng(live_location.get("latitude"), live_location.get("longitude")))
                                                                                        .title(user.getFname())
                                                                                        .icon(MainActivity.getBitmapDescriptorFromVector(getActivity(), R.drawable.ic_worker_y)))
                                                                                        .setSnippet(user.getUid());
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if (spin_location.getSelectedItemPosition() == 0) {
                                                                            try {
                                                                                if (user.isOnline()) {
                                                                                    if (city.equals(user.getLive_loc_city())) {

                                                                                        map.addMarker(new MarkerOptions().position(new LatLng(live_location.get("latitude"), live_location.get("longitude")))
                                                                                                .title(user.getFname())
                                                                                                .icon(MainActivity.getBitmapDescriptorFromVector(getActivity(), R.drawable.ic_worker_y)))
                                                                                                .setSnippet(user.getUid());
                                                                                    }
                                                                                } else if (city.equals(user.getCity())) {
                                                                                    map.addMarker(new MarkerOptions().position(new LatLng(live_location.get("latitude"), live_location.get("longitude")))
                                                                                            .title(user.getFname())
                                                                                            .icon(MainActivity.getBitmapDescriptorFromVector(getActivity(), R.drawable.ic_work_house)))
                                                                                            .setSnippet(user.getUid());
                                                                                }

                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        } else {

                                                                            if (user.isOnline()) {

                                                                                map.addMarker(new MarkerOptions().position(new LatLng(live_location.get("latitude"), live_location.get("longitude")))
                                                                                        .title(user.getFname())
                                                                                        .icon(MainActivity.getBitmapDescriptorFromVector(getActivity(), R.drawable.ic_worker_y)))
                                                                                        .setSnippet(user.getUid());
                                                                            } else {
                                                                                map.addMarker(new MarkerOptions().position(new LatLng(live_location.get("latitude"), live_location.get("longitude")))
                                                                                        .title(user.getFname())
                                                                                        .icon(MainActivity.getBitmapDescriptorFromVector(getActivity(), R.drawable.ic_work_house)))
                                                                                        .setSnippet(user.getUid());
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toasty.error(getActivity(), e.getMessage()).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });

                    }
                }).start();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> online_list = new ArrayList<>();
        online_list.add("Online Workers");
        online_list.add("All Workers");

        ArrayList<String> location_list = new ArrayList<>();
        location_list.add("Around Location");
        location_list.add("Anywhere");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_list_item1, online_list);
        adapter.setDropDownViewResource(R.layout.my_spinner);
        spin_online.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_list_item1, location_list);
        adapter2.setDropDownViewResource(R.layout.my_spinner);

        spin_location.setAdapter(adapter2);

        btn_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_List_View dialog_list_view = new Dialog_List_View(worker_type,date);
                ClientMain.fragmentController.setupDialog(dialog_list_view);

            }
        });

        spin_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadMarkers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_online.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadMarkers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public GoogleMap getMap() {
        return map;
    }

    @Override
    public void onPause() {
        super.onPause();
        ti.cancel();
        ClientMain.fragmentController.setupClientFragment(new Frag_Client_Home(), false);
    }
}
