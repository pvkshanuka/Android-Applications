package com.app.wooker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.wooker.AsyncTask.DialogMapLoadAsyncTask;
import com.app.wooker.AsyncTask.SignUpAsyncTask;
import com.app.wooker.AsyncTask.UpdatePwAsyncTask;
import com.app.wooker.AsyncTask.UpdateUserAsyncTask;
import com.app.wooker.AsyncTask.UploadAndSaveUserImgAsyncTask;
import com.app.wooker.DBClasses.User;
import com.app.wooker.DBClasses.WorkCats;
import com.app.wooker.DBClasses.WorkerTypes;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CustomClasses.RunOnUIThread;
import CustomClasses.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Frag_Worker_Profile_Settings extends Fragment {

    Button btn_updatelocation;

    ConstraintLayout cons_pw_area;

    EditText et_fname, et_lname, et_cno,et_web_link;
    Button btn_change_name, btn_change_cno, btn_changepw, btn_set_location, btn_add_cat, btn_remove_cat,btn_add_website;
    Spinner spinner_cats;

    Button btn_choose_pic, btn_uoload_pic;
    CircleImageView circleImageView;
    ProgressBar progressBar;

    File file_img;

    Uri mImageUri;

    Geocoder geocoder;
    List<Address> addresses;

    FirebaseFirestore db;

    StorageReference mStorageRef;

    public static StorageTask mUploadTask;


    String permissions[] = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CAMERA};
    String[] deniedPermissionsAccess;
    List<String> deniedList;
    boolean allPermissionGranted = true;

    Dialog_Lodaing dialog_lodaing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worker_profile_settings, container, false);

//        view.findViewById(R.id.scrollView3main).setVisibility(View.VISIBLE);

        init(view);
//Toasty.info(getActivity(),"onCreateView").show();
        return view;
    }

    private void init(final View view) {

        dialog_lodaing = new Dialog_Lodaing("Loading..!","Loading Settings.!");
        WorkerMain.fragmentController.setupDialog(dialog_lodaing);

        db = FirebaseFirestore.getInstance();
        btn_updatelocation = view.findViewById(R.id.btn_update_location);


        cons_pw_area = view.findViewById(R.id.cons_w_change_pw);
        et_cno = view.findViewById(R.id.et_cno);
        et_fname = view.findViewById(R.id.et_fname);
        et_lname = view.findViewById(R.id.et_lname);


        btn_change_cno = view.findViewById(R.id.btn_change_cno);
        btn_change_name = view.findViewById(R.id.btn_change_name);
        btn_changepw = view.findViewById(R.id.btn_changepw);
        btn_set_location = view.findViewById(R.id.btn_set_location);
        spinner_cats = view.findViewById(R.id.spinner_cats);
        btn_add_cat = view.findViewById(R.id.btn_add_cat);
        btn_remove_cat = view.findViewById(R.id.btn_remove_cat);
        btn_add_website = view.findViewById(R.id.btn_set_web_link);
        et_web_link = view.findViewById(R.id.et_web_link);


        btn_choose_pic = view.findViewById(R.id.btn_choose_pic);
        btn_uoload_pic = view.findViewById(R.id.btn_upload_pic);
        circleImageView = view.findViewById(R.id.circleImageViewpsw);
        progressBar = view.findViewById(R.id.progressBarimgup);

        mStorageRef = FirebaseStorage.getInstance().getReference("userimages");

        if (!(WorkerMain.mUser.getLocation().isEmpty())) {
            Map<String, Double> location = WorkerMain.mUser.getLocation();
            DialogMap.location = new LatLng(location.get("latitude"), location.get("longitude"));
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                        dialog_lodaing.dismiss();
                RunOnUIThread.runOnUiThread(new Runnable() {
                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);

                    CircleImageView circleImageView = navigationView.getHeaderView(0).findViewById(R.id.circleImageViewheaderw);

                    CircleImageView circleImageView2 = view.findViewById(R.id.circleImageViewpsw);

                    @Override
                    public void run() {
                        view.findViewById(R.id.scrollView3main).setVisibility(View.VISIBLE);
                        circleImageView2.setImageDrawable(circleImageView.getDrawable());

                    }
                });

            }
        }).start();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadCats();


        btn_add_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(et_web_link.getText().toString())) {
                    Toasty.error(getActivity(), "Website link is empty..!").show();
                } else {
                    if (et_web_link.getText().toString().length() >5) {

                        Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Processing..!", "Details Updating.!");
                        WorkerMain.fragmentController.setupDialog(dialogLodaing);

                        MainActivity.hideKeyboard(getActivity());

                        WorkerMain.mUser.setWeburl(et_web_link.getText().toString());
                        new UpdateUserAsyncTask(
                                getActivity()
                                , "worker"
                                , dialogLodaing
                                , WorkerMain.mUser)
                                .execute();
                        et_cno.setText("");

                    } else {
                        Toasty.error(getActivity(), "Invalid website link..!").show();
                    }
                }

            }
        });

        btn_choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermissionF()) {
                    choosePhoto();
                }

            }
        });


        btn_uoload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toasty.warning(getActivity(), "Upload in progress.!", Toasty.LENGTH_LONG).show();
                } else {
                    if (mImageUri != null) {

                        new UploadAndSaveUserImgAsyncTask(getActivity(), "worker", circleImageView, progressBar, mImageUri).execute();
                        mImageUri = null;
                    } else {
                        Toasty.error(getActivity(), "Please select image to upload.!", Toasty.LENGTH_LONG).show();
                    }
                }

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("facebook.com") || FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("google.com")) {
                    RunOnUIThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cons_pw_area.setVisibility(View.INVISIBLE);

                        }
                    });
                }

            }
        }).start();


        btn_set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogMapLoadAsyncTask(getActivity(), "worker").execute();
            }
        });

        btn_updatelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DialogMap.location != null) {

                    Map<String, Double> location = new HashMap<>();
                    location.put("latitude", DialogMap.location.latitude);
                    location.put("longitude", DialogMap.location.longitude);

                    if (!(WorkerMain.mUser.getLocation().equals(location))) {
                        try {
                            Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Processing..!", "Details Updating.!");
                            WorkerMain.fragmentController.setupDialog(dialogLodaing);

                            MainActivity.hideKeyboard(getActivity());


                            geocoder = new Geocoder(getActivity());
                            addresses = geocoder.getFromLocation(DialogMap.location.latitude, DialogMap.location.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

//                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                            String city = addresses.get(0).getLocality();
//                            String state = addresses.get(0).getAdminArea();
//                            String country = addresses.get(0).getCountryName();
//                            String postalCode = addresses.get(0).getPostalCode();
//                            String knownName = addresses.get(0).getFeatureName();
//
//                            System.out.println(address + " / " + city + " / " + state + " / " + country + " / " + postalCode + " / " + knownName);


                            WorkerMain.mUser.setLocation(location);

                            WorkerMain.mUser.setCity(addresses.get(0).getLocality());

                            new UpdateUserAsyncTask(
                                    getActivity()
                                    , "worker"
                                    , dialogLodaing
                                    , WorkerMain.mUser)
                                    .execute();
                            System.out.println(WorkerMain.mUser.getCity() + "                      <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                            et_cno.setText("");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toasty.error(getActivity(), "Please set location to update.!").show();
                    }
                } else {
                    Toasty.error(getActivity(), "Please set location to update.!").show();
                }

            }
        });


        btn_change_cno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_cno.getText().toString())) {
                    Toasty.error(getActivity(), "Contact number is empty..!").show();
                } else {
                    if (Validations.conValidation(et_cno.getText().toString())) {

                        Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Processing..!", "Details Updating.!");
                        WorkerMain.fragmentController.setupDialog(dialogLodaing);

                        MainActivity.hideKeyboard(getActivity());

                        WorkerMain.mUser.setCno(et_cno.getText().toString());
                        new UpdateUserAsyncTask(
                                getActivity()
                                , "worker"
                                , dialogLodaing
                                , WorkerMain.mUser)
                                .execute();
                        et_cno.setText("");

                    } else {
                        Toasty.error(getActivity(), "Invalid Contact number..!").show();
                    }
                }
            }
        });

        btn_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_fname.getText().toString()) || TextUtils.isEmpty(et_lname.getText().toString())) {
                    Toasty.error(getActivity(), "Some details are empty..!").show();
                } else {

                    Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("Processing..!", "Details Updateing.!");
                    WorkerMain.fragmentController.setupDialog(dialogLodaing);

                    MainActivity.hideKeyboard(getActivity());

                    WorkerMain.mUser.setFname(et_fname.getText().toString());
                    WorkerMain.mUser.setLname(et_lname.getText().toString());

                    new UpdateUserAsyncTask(
                            getActivity()
                            , "worker"
                            , dialogLodaing
                            , WorkerMain.mUser)
                            .execute();

                    et_fname.setText("");
                    et_lname.setText("");

                }
            }
        });

        btn_changepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Change Password")
                        .setMessage("Password reset link will send to your email, You will signed out after sending the link.!")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Dialog_Lodaing dialogLodaing = new Dialog_Lodaing("", "Sending password reset email..!");
                                WorkerMain.fragmentController.setupDialog(dialogLodaing);

                                new UpdatePwAsyncTask(getActivity(), WorkerMain.firebaseUser.getEmail(), dialogLodaing).execute();
                            }
                        }).create().show();

            }
        });

        btn_add_cat.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                final Dialog_Lodaing dialog_lodaing = new Dialog_Lodaing("Category Adding.!", "Processing..!");
                WorkerMain.fragmentController.setupDialog(dialog_lodaing);


                new Thread(new Runnable() {
                    boolean b = false;

                    @Override
                    public void run() {

                        if (spinner_cats.getSelectedItemPosition() == 0) {

                            Toasty.error(getActivity(), "Please select category to add.!", Toasty.LENGTH_LONG).show();

                            dialog_lodaing.dismiss();
                        } else {

                            db.collection("work_cats")
                                    .whereEqualTo("type", spinner_cats.getSelectedItem().toString()).
                                    get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    final String type = queryDocumentSnapshots.getDocuments().get(0).getId();

                                    if (queryDocumentSnapshots.isEmpty()) {
                                        Toasty.error(getActivity(), "Invalid Category").show();
                                        dialog_lodaing.dismiss();
                                    } else {

                                        db.collection("worker_types")
                                                .whereEqualTo("type", type)
                                                .whereEqualTo("workerid", WorkerMain.firebaseUser.getUid())
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if (queryDocumentSnapshots.isEmpty()) {
                                                            db.collection("worker_types").document().set(new WorkerTypes(type, WorkerMain.firebaseUser.getUid()))
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toasty.success(getActivity(), "Category added.!").show();
                                                                            spinner_cats.setSelection(0);

                                                                            dialog_lodaing.dismiss();


                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            dialog_lodaing.dismiss();

                                                                            Toasty.error(getActivity(), e.getMessage(), Toasty.LENGTH_LONG).show();
                                                                        }
                                                                    });
                                                        } else {
                                                            dialog_lodaing.dismiss();

                                                            Toasty.error(getActivity(), "Category already added", Toasty.LENGTH_LONG).show();
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        dialog_lodaing.dismiss();
                                                        Toasty.error(getActivity(), e.getMessage(), Toasty.LENGTH_LONG).show();
                                                    }
                                                });


                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog_lodaing.dismiss();
                                            Toasty.error(getActivity(), e.getMessage()).show();

                                        }
                                    });

                        }


                    }
                }).start();

            }
        });


        btn_remove_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog_Lodaing dialog_lodaing = new Dialog_Lodaing("Category Deleting.!", "Processing..!");
                WorkerMain.fragmentController.setupDialog(dialog_lodaing);


                new Thread(new Runnable() {
                    boolean b = false;

                    @Override
                    public void run() {

                        if (spinner_cats.getSelectedItemPosition() == 0) {

                            Toasty.error(getActivity(), "Please select category to delete.!", Toasty.LENGTH_LONG).show();

                            dialog_lodaing.dismiss();
                        } else {

                            db.collection("work_cats")
                                    .whereEqualTo("type", spinner_cats.getSelectedItem().toString()).
                                    get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    final String typeid = queryDocumentSnapshots.getDocuments().get(0).getId();

                                    if (queryDocumentSnapshots.isEmpty()) {
                                        Toasty.error(getActivity(), "Invalid Category").show();
                                        dialog_lodaing.dismiss();
                                    } else {

                                        db.collection("worker_types")
                                                .whereEqualTo("type", typeid)
                                                .whereEqualTo("workerid", WorkerMain.firebaseUser.getUid())
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if (queryDocumentSnapshots.isEmpty()) {
                                                            dialog_lodaing.dismiss();

                                                            Toasty.error(getActivity(), "You don't have that category to delete.!", Toasty.LENGTH_LONG).show();

                                                        } else {


                                                            db.collection("worker_types").document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete()
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toasty.success(getActivity(), "Category deleted.!").show();
                                                                            spinner_cats.setSelection(0);

                                                                            dialog_lodaing.dismiss();


                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            dialog_lodaing.dismiss();

                                                                            Toasty.error(getActivity(), e.getMessage(), Toasty.LENGTH_LONG).show();
                                                                        }
                                                                    });


                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        dialog_lodaing.dismiss();
                                                        Toasty.error(getActivity(), e.getMessage(), Toasty.LENGTH_LONG).show();
                                                    }
                                                });

                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog_lodaing.dismiss();
                                            Toasty.error(getActivity(), e.getMessage()).show();

                                        }
                                    });

                        }
                    }
                }).start();

            }
        });


    }

    private void choosePhoto() {
        CropImage.activity()
                .setAspectRatio(6, 6)
                .setMinCropResultSize(100, 100)
                .setMaxCropResultSize(1800, 1800)
                .start(getContext(), this);
    }

    private boolean checkPermissionF() {


        if (checkLocationPermissionsAreGranted()) {

            return true;

        } else {
            deniedPermissionsAccess = new String[deniedList.size()];
            deniedPermissionsAccess = deniedList.toArray(deniedPermissionsAccess);

            Toasty.warning(getActivity(), "Need permissions to continue").show();
//                    .makeText(activity, "Need permissions to continue", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), deniedPermissionsAccess[0])) {


                Snackbar.make(getActivity().findViewById(R.id.worker_coor_lay), "App needs permissions to continue.!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(getActivity(), deniedPermissionsAccess, MainActivity.STORAGE_PERMITION_CODE);
                                    }
                                })
                        .setActionTextColor((ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary))).show();


            } else {
                ActivityCompat.requestPermissions(getActivity(), deniedPermissionsAccess, MainActivity.STORAGE_AND_CAMERS_PERMITION_CODE);
            }
            return false;
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == MainActivity.RESULT_OK) {
//            Toast.makeText(getActivity(), "AWAAAAAAAAAAAAAAAAAAAAAAAAAAAaaa", Toast.LENGTH_SHORT).show();

//                try {
//                    InputStream fileInputStream = getActivity().getContentResolver().openInputStream(data.getData());
//                    int filesize = (fileInputStream.available() / 1024);
//                    System.out.println(filesize+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//                    if (filesize > 5200) {
//                        Toasty.error(getActivity(), "Selected image is too large size : " + filesize + "KB", Toasty.LENGTH_LONG).show();
//                        Toasty.info(getActivity(), "Please select image less than 5120KB", Toasty.LENGTH_LONG).show();
//                    } else {

//                        Toasty.info(getActivity(), "Image size : " + filesize + "KB", Toasty.LENGTH_LONG).show();

                mImageUri = result.getUri();
                Picasso.with(getActivity())
                        .load(mImageUri)
                        .into(circleImageView);

//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toasty.error(getActivity(), result.getError().getMessage()).show();
            }

        }

    }

    private void cropImage() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MainActivity.STORAGE_AND_CAMERS_PERMITION_CODE) {

            allPermissionGranted = true;

            for (int i = 0; i < grantResults.length; i++) {

                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allPermissionGranted = false;
                }
            }

            if (allPermissionGranted) {
                choosePhoto();
            } else {


                Snackbar.make(getActivity().findViewById(R.id.worker_coor_lay), "App needs permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(getActivity(), deniedPermissionsAccess, MainActivity.LOCATION_PERMITION_CODE);
                            }
                        })
                        .setActionTextColor((ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary))).show();

            }

        }
    }

    private void loadCats() {

        new Thread(new Runnable() {

            @Override
            public void run() {


                final List<String> cats = new ArrayList<>();
                cats.add("Select Worker Type");

                db.collection("work_cats").orderBy("type").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                    cats.add(documentSnapshot.toObject(WorkCats.class).getType());
                                }


                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                RunOnUIThread.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_list_item1, cats);
                        adapter.setDropDownViewResource(R.layout.my_spinner);
//
                        spinner_cats.setAdapter(adapter);

                    }
                });


            }
        }).start();


    }

    private boolean checkLocationPermissionsAreGranted() {
        deniedList = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(permission);
            }
        }
        if (deniedList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
////        super.onSaveInstanceState(outState);
//Toasty.info(getActivity(),"onSaveInstanceState").show();
//        outState.putString("fname",et_fname.getText().toString());
//
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
////        super.onViewStateRestored(savedInstanceState);
//        Toasty.info(getActivity(),"onViewStateRestored").show();
//
//        if (savedInstanceState != null){
//            Toasty.info(getActivity(),"IF").show();
//
//            et_fname.setText(savedInstanceState.getString("fname"));
//
//        }else {
//            Toasty.info(getActivity(),"ELSE").show();
//
//        }
//
//    }
}
