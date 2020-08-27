package com.app.wooker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

//public class DialogMap extends AppCompatDialogFragment {
public class DialogMap extends DialogFragment implements OnMapReadyCallback {

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    GoogleMap map;
    SupportMapFragment mapFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Marker marker;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;


    public static LatLng location;

    LatLng latLng, cuLatLang;

    MarkerOptions options;

    Button btn_close;
    TextView btn_ok;
    Button btn_getclo;
    Button btn_getmlo;
    TextView tv_msg;
    ConstraintLayout msg_view;
    ConstraintLayout btns_view;
    ConstraintLayout search_loc_cons;
    ConstraintLayout btn_cur_loc;
    ConstraintLayout btn_mapview;
    AutoCompleteTextView et_search_loc;

    BitmapDescriptor marker_icon;

    PlacesClient placesClient;

    AutocompleteSupportFragment autocompleteSupportFragment;

    Boolean isOnlyView = false;


    public DialogMap() {

    }

    @SuppressLint("ValidFragment")
    public DialogMap(Boolean isOnlyView) {
        this.isOnlyView = isOnlyView;
    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE >>>>>>>>>>>>>>>>>>>>>>>>>>>>> on create dialog");

        View view = inflater.inflate(R.layout.dialog_map_frag, container, false);

        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mymap);
        mapFragment.getMapAsync(this);

        btn_close = view.findViewById(R.id.btn_close);
        btn_ok = view.findViewById(R.id.btn_ok);
        btn_getmlo = view.findViewById(R.id.btn_getmlo);
        btn_getclo = view.findViewById(R.id.btn_getclo);
        tv_msg = view.findViewById(R.id.txtv_msg);
        tv_msg.setText("Press & hold on map to add marker.");
        btns_view = view.findViewById(R.id.map_btns_view);
        msg_view = view.findViewById(R.id.map_msg_view);
        search_loc_cons = view.findViewById(R.id.search_loc_cons);
        btn_cur_loc = view.findViewById(R.id.btn_current_loc);
        btn_mapview = view.findViewById(R.id.btn_mapview);

        marker_icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_marker_s);

//        et_search_loc = view.findViewById(R.id.pt_search_loc);


        if (location != null) {
            btns_view.setVisibility(View.VISIBLE);
//            search_loc_cons.setVisibility(View.VISIBLE);
            msg_view.setVisibility(View.INVISIBLE);
        } else {
            btns_view.setVisibility(View.INVISIBLE);
//            search_loc_cons.setVisibility(View.INVISIBLE);
            msg_view.setVisibility(View.VISIBLE);
        }

        if (isOnlyView) {
            btns_view.setVisibility(View.INVISIBLE);
            search_loc_cons.setVisibility(View.INVISIBLE);
            msg_view.setVisibility(View.INVISIBLE);
//        btn_cur_loc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sho
//            }
//        });
        } else {

            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogMap.this.dismiss();
                }
            });

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg_view.setVisibility(View.INVISIBLE);
                    btns_view.setVisibility(View.VISIBLE);
                    search_loc_cons.setVisibility(View.VISIBLE);
                }
            });

            btn_getmlo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (location == null) {
                        btns_view.setVisibility(View.INVISIBLE);
                        search_loc_cons.setVisibility(View.INVISIBLE);
                        tv_msg.setText("Please add marker to get marker location");
                        msg_view.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "Please add marker to get marker location(Press & hold on your location to add marker", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogMap.this.dismiss();
                        Toasty.info(getActivity(), "Location selected!", Toast.LENGTH_SHORT, true).show();
                    }
                }
            });

            btn_getclo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cuLatLang == null) {
                        btns_view.setVisibility(View.INVISIBLE);
                        search_loc_cons.setVisibility(View.INVISIBLE);
                        tv_msg.setText("Please enable location service");
                        msg_view.setVisibility(View.VISIBLE);
//                    DialogMap.this.dismiss();
//                    Snackbar.make(getActivity().findViewById(R.id.main_coor_lay), "Location service is desable, Please enable to get current location", Snackbar.LENGTH_LONG).setAction("ENABLE",
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                    startActivity(intent);
//                                }
//                            })
//                            .setActionTextColor((ContextCompat.getColor(getActivity().getApplicationContext(), R.color.col_yellow))).show();
                    } else {
                        location = cuLatLang;
                        DialogMap.this.dismiss();
                        Toasty.info(getActivity(), "Location selected!", Toast.LENGTH_SHORT, true).show();
                    }

                }
            });

        }
//        setupAutoCompleteFragment();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        DialogMap.this.
//        ((ConstraintLayout) view.findViewById(R.id.map_cons_lay)).setMinWidth((displayMetrics.widthPixels-20));
        getDialog().getWindow().setLayout((displayMetrics.widthPixels - 20), (displayMetrics.heightPixels - 20));

        return view;
    }

//    private void setupAutoCompleteFragment() {
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//
//
////            @Override
////            public void onPlaceSelected(Place place) {
////            }
//
//            @Override
//            public void onPlaceSelected(Place place) {
//                moveCamera(place.getLatLng(),14,place.getName().toString(),true,marker_icon);
//
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.e("Error", status.getStatusMessage());
//            }
//        });
//    }

    private void init() {

            String apiKey = "AIzaSyBKQOZTYc3sfzUajPaYfb6Bpdv9GmbHcmE";

            if (!Places.isInitialized()) {
                Places.initialize(getActivity(), apiKey);
            }

            placesClient = Places.createClient(getActivity());


            autocompleteSupportFragment = (AutocompleteSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));

            autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    moveCamera(place.getLatLng(), 17, place.getName().toString(), true, marker_icon);
                }

                @Override
                public void onError(@NonNull Status status) {
                    Toasty.error(getActivity(), "ERROR");
                }
            });


        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setCompassEnabled(isOnlyView);
        map.getUiSettings().setIndoorLevelPickerEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(isOnlyView);


        btn_cur_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCurrentLocation(true);
            }
        });

        btn_mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("MAP TYPE - " + map.getMapType() + " <<<<<<<<<<");
                if (map.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (map.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (map.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else if (map.getMapType() == GoogleMap.MAP_TYPE_TERRAIN) {
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else {
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                System.out.println("MAP TYPE - " + map.getMapType() + " >>>>>>>>>>");
            }
        });

        hideSoftKeyboard();
    }

    private void geoLocate() {


        String search_string = et_search_loc.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addressList = new ArrayList<>();
        try {

            addressList = geocoder.getFromLocationName(search_string, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList.size() > 0) {
            System.out.println(addressList.size() + "-Results   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            Address address = addressList.get(0);
            System.out.println(address.toString());
//            latLng = new LatLng(address.getLatitude(),address.getLongitude());
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 14));
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 14f, address.getAddressLine(0), true, marker_icon);


        } else {
            System.out.println("No Results   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            Toasty.info(getActivity(), "No Results.!").show();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE >>>>>>>>>>>>>>>>>>>>>>>>>>>>> on map ready");
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);

        if (location != null) {
//            kalin set karapu location ekak thiyanawa


            options = new MarkerOptions();
            options.position(location);
            if (isOnlyView) {
                options.title("Job Location");
            } else {
                options.title("Your Place");
            }
            options.icon(marker_icon);
            options.draggable(false);
            DialogMap.this.marker = map.addMarker(options);
            marker.showInfoWindow();

//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18.5f));
            moveCamera(location, 18.5f, "", false, null);

        }
        showCurrentLocation(false);

        if (!isOnlyView) {
            GoogleMap.OnMapLongClickListener longClickListener = new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {

                    location = latLng;
                    MarkerOptions options = new MarkerOptions();
                    options.position(latLng);
                    options.title("Your Place");
                    options.draggable(true);
                    options.icon(marker_icon);
                    if (DialogMap.this.marker != null) {
                        DialogMap.this.marker.remove();
                    }
                    DialogMap.this.marker = map.addMarker(options);
                    DialogMap.this.marker.showInfoWindow();

                }
            };

            map.setOnMapLongClickListener(longClickListener);

        }
        init();
    }

    private void showCurrentLocation(final boolean mustZoom) {

        if (isLocationServiceEnabled()) {
//                Location service enable
            Toast.makeText(getActivity(), "Location service enable", Toast.LENGTH_SHORT).show();
//                    current location ekata marker ekak dala zoom karanawa.
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
            lastLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        System.out.println("Sucess");
                        if (task.getResult() != null) {
                            System.out.println("result not null");
                            currentLocation = (Location) task.getResult();
                            cuLatLang = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            if (location == null || mustZoom) {
//                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(cuLatLang, 18.5f));
                                moveCamera(cuLatLang, 18.5f, "", false, null);
                            }
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            map.setMyLocationEnabled(true);
                        } else {
                            System.out.println("result null");
                            if (location == null) {
                                latLng = new LatLng(7.873687, 80.648844);
//                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7.3f));
                                moveCamera(latLng, 7.3f, "", false, null);
                            }
                        }
                    } else {
                        System.out.println("Sucess na");
                        if (location == null) {
                            latLng = new LatLng(7.873687, 80.648844);
//                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7.3f));
                            moveCamera(latLng, 7.3f, "", false, null);
                        }
                    }
                }
            });


//                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                    return;
//                }
//                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location culocation) {
//                        if (culocation != null) {
//                            cuLatLang = new LatLng(culocation.getLatitude(), culocation.getLongitude());
//                            Toast.makeText(getActivity(), latLng.toString(), Toast.LENGTH_LONG).show();
//                            options = new MarkerOptions();
//                            options.position(latLng);
//                            options.title("You");
//                            options.draggable(false);
//                            BitmapDescriptor descriptor2 = BitmapDescriptorFactory.fromResource(R.drawable.googleg_standard_color_18);
//                            options.icon(descriptor2);
//                            Marker marker = map.addMarker(options);
//                            marker.showInfoWindow();
//
////                            Marker marker = map.addMarker(new MarkerOptions().position(location).title("My Home"));
//                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cuLatLang, 18.5f));
//
//                        }else{
//                            latLng = new LatLng(7.873687, 80.648844);
//                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7.3f));
//                        }
//                    }
//                });

        } else {
//                load map sl
            if (location == null) {
                latLng = new LatLng(7.873687, 80.648844);
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7.3f));
                moveCamera(latLng, 7.3f, "", false, null);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
            if (map != null) {
                map.clear();
            }
            try {
                DialogMap.this.dismiss();
                Fragment fragment = (getFragmentManager().findFragmentById(R.id.mymap));

                if (getActivity() != null) {
                    System.out.println("isOnlyView");
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(autocompleteSupportFragment);
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();

                    MainActivity.dismissAllDialogs(fragmentManager);
                }
            } catch (IllegalStateException e) {
                System.out.println("IllegalStateException");
            }
        System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE >>>>>>>>>>>>>>>>>>>>>>>>>>>>> on destroy view");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onPause() {
        super.onPause();
        System.out.println(" EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE >>>>>>>>>>>>>>>>>>>>>>>>>>>>> onPause view");
//    onDestroyView();

    }

    public boolean isLocationServiceEnabled() {
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;

        if (locationManager == null)
            locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            //do nothing...
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            //do nothing...
        }

        return gps_enabled || network_enabled;

    }

    private void moveCamera(LatLng latLng, float zoom, String title, boolean addMarker, BitmapDescriptor icon) {

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (addMarker) {
            location = latLng;
            options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            if (icon != null) {
                options.icon(icon);
            }
            map.clear();
            DialogMap.this.marker = map.addMarker(options);
            DialogMap.this.marker.showInfoWindow();
            hideSoftKeyboard();
        }

    }

    private void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mMap != null) {
//            mMap.clear();
//        }
//    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
