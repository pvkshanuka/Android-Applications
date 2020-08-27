package com.app.wooker;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Frag_Bluetooth extends Fragment {

    BluetoothAdapter mBluetoothAdapter;

    Button btn_bluetooth, btn_bluetoothdis, btn_discover;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;


    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Toasty.info(getActivity(), "Bluetooth OFF").show();
                        mBTDevices.removeAll(mBTDevices);
                        mDeviceListAdapter.notifyDataSetChanged();
//                        btn_bluetooth.setText(R.string.txt_enable_bluetooth);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toasty.info(getActivity(), "Bluetooth Turning OFF").show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toasty.info(getActivity(), "Bluetooth ON").show();
//                        btn_bluetooth.setText(R.string.txt_disable_bluetooth);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toasty.info(getActivity(), "Bluetooth Turning ON").show();
                        break;
                }
            }
        }
    };


    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Toasty.info(getActivity(), "Discoverability Enabled.!").show();
//                        btn_bluetooth.setText(R.string.txt_disable_bluetooth);

//                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
//                        Toasty.info(getActivity(),"Discoverability Disabled.! Able to receive connections.").show();
//                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
//                        Toasty.info(getActivity(),"Discoverability Disabled.! Not able to receive connections.").show();
//                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Toasty.info(getActivity(), "Connecting.!").show();
//                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Toasty.info(getActivity(), "Connected.!").show();
//                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };


    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
//            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!mBTDevices.contains(device)) {
                    mBTDevices.add(device);
                    mDeviceListAdapter.notifyDataSetChanged();
                }
//                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
            }
        }
    };


    public Frag_Bluetooth() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag__bluetooth, container, false);

        init(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enableDisableBT();

            }
        });

        btn_bluetoothdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enableDisableDis();

            }
        });

        btn_discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                discoverDevices();

            }
        });

    }


    private void init(View view) {

        btn_bluetooth = view.findViewById(R.id.btn_bluetooth);
        btn_bluetoothdis = view.findViewById(R.id.btn_bluetoothdis);
        btn_discover = view.findViewById(R.id.btn_discover);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lvNewDevices = view.findViewById(R.id.lvNewDevices);
        mBTDevices = new ArrayList<>();

        mDeviceListAdapter = new DeviceListAdapter(getActivity(), R.layout.device_adapter_view, mBTDevices);
        lvNewDevices.setAdapter(mDeviceListAdapter);

    }

    private void discoverDevices() {

        Toasty.info(getActivity(), "Looking for unpaired devices.!").show();
        mBTDevices.removeAll(mBTDevices);
        mDeviceListAdapter.notifyDataSetChanged();
//        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
//            Toasty.info(getActivity(), "Canceling discovery.!").show();

//            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if (!mBluetoothAdapter.isDiscovering()) {

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }

    }


    private void enableDisableDis() {

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiver2, intentFilter);

    }

    public void enableDisableBT() {
        if (mBluetoothAdapter == null) {
            Toasty.info(getActivity(), "Does not have Bluetooth capabilities.").show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
//            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (mBluetoothAdapter.isEnabled()) {
//            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }

    private void checkBTPermissions() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = getActivity().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += getActivity().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
//            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(mBroadcastReceiver1);
            getActivity().unregisterReceiver(mBroadcastReceiver2);
            getActivity().unregisterReceiver(mBroadcastReceiver3);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
