package com.app.wooker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.app.wooker.AsyncTask.UpdatePwAsyncTask;
import com.app.wooker.AsyncTask.UpdateUserAsyncTask;
import com.app.wooker.AsyncTask.UploadAndSaveUserImgAsyncTask;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import CustomClasses.RunOnUIThread;
import CustomClasses.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Frag_Client_Profile_Settings extends Fragment {

    Button btn_updatelocation;
    ConstraintLayout cons_pw_area;
    Dialog_Lodaing dialogLodaing;
    EditText et_fname, et_lname, et_cno;
    Button btn_change_name, btn_change_cno, btn_changepw, btn_set_location, btn_add_cat, btn_remove_cat;
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
        View view = inflater.inflate(R.layout.client_profile_settings, container, false);
        init(view);
        return view;
    }

    private void init(final View view) {
        db = FirebaseFirestore.getInstance();
        cons_pw_area = view.findViewById(R.id.cons_w_change_pwc);
        et_cno = view.findViewById(R.id.et_cnoc);
        et_fname = view.findViewById(R.id.et_fnamec);
        et_lname = view.findViewById(R.id.et_lnamec);
        btn_change_cno = view.findViewById(R.id.btn_change_cnoc);
        btn_change_name = view.findViewById(R.id.btn_change_namec);
        btn_changepw = view.findViewById(R.id.btn_changepwc);

        btn_choose_pic = view.findViewById(R.id.btn_choose_picc);
        btn_uoload_pic = view.findViewById(R.id.btn_upload_picc);
        circleImageView = view.findViewById(R.id.circleImageViewpsc);
        progressBar = view.findViewById(R.id.progressBarimgupc);

        mStorageRef = FirebaseStorage.getInstance().getReference("userimages");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RunOnUIThread.runOnUiThread(new Runnable() {
                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);

                    CircleImageView circleImageView = navigationView.getHeaderView(0).findViewById(R.id.circleImageViewheaderc);

                    CircleImageView circleImageView3 = view.findViewById(R.id.circleImageViewpsc);

                    @Override
                    public void run() {
                        circleImageView3.setImageDrawable(circleImageView.getDrawable());

                    }
                });

            }
        }).start();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("facebook.com") || FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(1).getProviderId().equals("google.com")) {
            cons_pw_area.setVisibility(View.INVISIBLE);
        }

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
                        new UploadAndSaveUserImgAsyncTask(getActivity(), "client", circleImageView, progressBar, mImageUri).execute();
                        mImageUri = null;
                    } else {
                        Toasty.error(getActivity(), "Please select image to upload.!", Toasty.LENGTH_LONG).show();
                    }
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

                        dialogLodaing = new Dialog_Lodaing("Processing..!", "Details Updateing.!");
                        ClientMain.fragmentController.setupDialog(dialogLodaing);
                        MainActivity.hideKeyboard(getActivity());
                        ClientMain.mUser.setCno(et_cno.getText().toString());
                        new UpdateUserAsyncTask(
                                getActivity()
                                , "client"
                                , dialogLodaing
                                , ClientMain.mUser)
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
                    dialogLodaing = new Dialog_Lodaing("Processing..!", "Details Updateing.!");
                    ClientMain.fragmentController.setupDialog(dialogLodaing);
                    MainActivity.hideKeyboard(getActivity());
                    ClientMain.mUser.setFname(et_fname.getText().toString());
                    ClientMain.mUser.setLname(et_lname.getText().toString());

                    new UpdateUserAsyncTask(
                            getActivity()
                            , "client"
                            , dialogLodaing
                            , ClientMain.mUser)
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
                                    dialogLodaing = new Dialog_Lodaing("","Sending password reset email..!");
                                    ClientMain.fragmentController.setupDialog(dialogLodaing);
                                    new UpdatePwAsyncTask(getActivity(),ClientMain.firebaseUser.getEmail(),dialogLodaing).execute();
                                }
                            }).create().show();
                }
            });
    }

    private boolean checkPermissionF() {
        if (checkLocationPermissionsAreGranted()) {
            return true;
        } else {
            deniedPermissionsAccess = new String[deniedList.size()];
            deniedPermissionsAccess = deniedList.toArray(deniedPermissionsAccess);
            Toasty.warning(getActivity(), "Need permissions to continue").show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), deniedPermissionsAccess[0])) {
                Snackbar.make(getActivity().findViewById(R.id.client_coor_lay), "App needs permissions to continue.!", Snackbar.LENGTH_INDEFINITE)
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
                Snackbar.make(getActivity().findViewById(R.id.client_coor_lay), "App needs permissions to continue", Snackbar.LENGTH_LONG).setAction("ENABLE",
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

    private void choosePhoto() {
        CropImage.activity()
                .setAspectRatio(6, 6)
                .setMinCropResultSize(100, 100)
                .setMaxCropResultSize(1800, 1800)
                .start(getContext(), this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == MainActivity.RESULT_OK) {
                mImageUri = result.getUri();
                Picasso.with(getActivity())
                        .load(mImageUri)
                        .into(circleImageView);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toasty.error(getActivity(), result.getError().getMessage()).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
