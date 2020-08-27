package com.example.app36googlemap2;

import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GoogleMap map;
    Marker marker;
    Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.flayout1, mapFragment, "map");
        fragmentTransaction.commit();

        OnMapReadyCallback listener = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                map = googleMap;

//                Set Camera to position
                LatLng location = new LatLng(6.897221, 79.860329);
                CameraPosition.Builder cameraBuilder = new CameraPosition.Builder();
                cameraBuilder.target(location);
                cameraBuilder.zoom(17);
                CameraPosition cameraPosition = cameraBuilder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                map.animateCamera(cameraUpdate);

//                Add Marker to possition
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                markerOptions.title("You");
                BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.googleg_disabled_color_18);
                markerOptions.icon(descriptor);
                final Marker marker = map.addMarker(markerOptions);
                marker.showInfoWindow();

//                Change map style
                MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.style);
                map.setMapStyle(mapStyleOptions);

//                Set marker onclick listener
                GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(MainActivity.this, "Marker Clicked", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                };

                map.setOnMarkerClickListener(markerClickListener);

//                Meka warning ekak witharai
                map.setMyLocationEnabled(true);

//                Marker eka move karana eka
                LocationRequest request = LocationRequest.create();
                request.setInterval(2000);
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationCallback locationCallback = new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {

                        lastLocation = locationResult.getLastLocation();
                        MarkerOptions markerOptions1 = new MarkerOptions();
                        LatLng location2 = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        markerOptions1.position(location2);
                        markerOptions1.title("ME");
                        BitmapDescriptor descriptor2 = BitmapDescriptorFactory.fromResource(R.drawable.googleg_disabled_color_18);
                        markerOptions1.icon(descriptor2);
//                        markerOptions1.flat(true);

                        if (MainActivity.this.marker != null) {
                            MainActivity.this.marker.remove();
                        }

                        MainActivity.this.marker = map.addMarker(markerOptions1);

                    }
                };

                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                client.requestLocationUpdates(request, locationCallback, Looper.myLooper());

                UiSettings uiSettings = map.getUiSettings();
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setCompassEnabled(true);
                uiSettings.setCompassEnabled(true);

                GoogleMap.OnMapLongClickListener listener3 = new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(final LatLng latLng) {

                        MarkerOptions options3 = new MarkerOptions();
                        options3.position(latLng);
                        options3.title("Marker");
                        options3.draggable(true);
                        options3.icon(BitmapDescriptorFactory.fromResource(R.drawable.friendly));
                        Marker marker1 = map.addMarker(options3);
                        marker1.showInfoWindow();

                        try {

                            Geocoder geocoder = new Geocoder(MainActivity.this);
                            List<Address> list = geocoder.getFromLocation(6.896929, 79.860319, 5);

                            for (Address address : list) {
                                System.out.println(address.getLocality());
                            }

                            DialogInterface.OnClickListener l1 = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            };

//                    builder.se

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


//                        Navigation
//                        PolylineOptions options4 = new PolylineOptions();
//                        options4.add(latLng);
//
//                        LatLng a1 = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
//                        options4.add(a1);
//                        options4.color(getColor(R.color.green));
//                        options4.width(15);
//                        map.addPolyline(options4);

                        final String startLocation = lastLocation.getLatitude() + "," + lastLocation.getLongitude();
                        final String endLocation = latLng.latitude + "," + latLng.longitude;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + startLocation + "\n" +
                                            "&destination=" + endLocation + "&mode=driving\n" +
                                            "&key=AIzaSyBjI_ElJ7OP20Y6HX_iVgULDmLs88UShy8");

                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                                    InputStream inputStream = connection.getInputStream();

                                    int data;

                                    String response = "";

                                    while ((data = inputStream.read()) != -1) {

                                        response += (char) data;

                                    }

                                    System.out.println(response);

                                    String r1 = response.split("overview_polyline")[1];
                                    String r2 = r1.split("points\" : \"")[1];
                                    final String r3 = r2.split("\"")[0];

                                    System.out.println("ROUTE - "+r3);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<LatLng> route = PolyUtil.decode(r3);
                                            PolylineOptions polylineOptions = new PolylineOptions();
                                            polylineOptions.addAll(route);
                                            polylineOptions.color(getColor(R.color.green));
                                            polylineOptions.width(15);
                                            map.addPolyline(polylineOptions);
                                        }
                                    });

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();


                    }
                };

                map.setOnMapLongClickListener(listener3);


            }
        };


        mapFragment.getMapAsync(listener);

    }
}
