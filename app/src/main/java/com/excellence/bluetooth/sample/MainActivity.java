package com.excellence.bluetooth.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.excellence.bluetooth.sample.databinding.ActivityMainBinding;
import com.excellence.bluetoothlibrary.BluetoothClient;
import com.excellence.bluetoothlibrary.BluetoothRequest;
import com.excellence.bluetoothlibrary.callback.IScannerListener;
import com.excellence.bluetoothlibrary.entity.BluetoothKitDevice;
import com.excellence.bluetoothlibrary.exception.BluetoothError;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setOnClick(this);
    }

    @Override
    public void onClick(View v) {
        BluetoothRequest request = new BluetoothRequest.Builder()
                .setScanTimeOut(0)
                .isPermissionCheck(false)
                .build();

        BluetoothClient.getInstance(MainActivity.this)
                .addBluetoothRequest(request)
                .addListener(new IScannerListener() {
                    @Override
                    public void onScanStarted() {
                        Log.d(TAG, "onScanStarted");
                    }

                    @Override
                    public void onScanning(BluetoothKitDevice device) {
                        ParcelUuid[] parcelUuids = device.getBluetoothDevice().getUuids();
                        String uuid = "";
                        if (parcelUuids != null && parcelUuids.length > 0) {
                            uuid = parcelUuids[0].toString();
                        }
                        Log.i(TAG, "onScanning: " + device.getName() + " - " + device.getAddress() + " - " + uuid);
                    }

                    @Override
                    public void onScanFinished(List<BluetoothKitDevice> deviceList) {
                        Log.d(TAG, "onScanFinished: " + deviceList.size());
                    }

                    @Override
                    public void onScanFailed(BluetoothError e) {
                        e.printStackTrace();
                    }
                }).search();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BluetoothClient.getInstance(this).stopSearch();
    }

}
