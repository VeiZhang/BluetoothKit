package com.excellence.bluetoothlibrary;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.excellence.bluetoothlibrary.callback.IScannerListener;
import com.excellence.bluetoothlibrary.entity.BluetoothKitDevice;
import com.excellence.bluetoothlibrary.exception.BluetoothScanError;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/9
 *     desc   :
 * </pre>
 */

public class BluetoothScanService extends Service implements Handler.Callback {

    private static final String TAG = BluetoothScanService.class.getSimpleName();

    private static final int MSG_STOP_SEARCH = 0;
    private static final int MSG_AUTO_STOP_SEARCH = 1;

    private Handler mHandler = null;
    private BluetoothRequest mBluetoothRequest = null;
    private IScannerListener mScannerListenerImp = null;
    private Map<String, BluetoothKitDevice> mBleKitDeviceList = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private boolean isBluetoothLeRunning = false;
    private boolean isBluetoothClassicRunning = false;

    @Override
    public boolean onUnbind(Intent intent) {
        unregisterReceiver(mBluetoothStateReceiver);
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        IntentFilter intentFilter = new IntentFilter();
        // 蓝牙开关状态
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        // 蓝牙搜索
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        // 蓝牙配对
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

        registerReceiver(mBluetoothStateReceiver, intentFilter);
        return new BluetoothBinder();
    }

    private BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            Log.d(TAG, "onReceive: " + intent.getAction());
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        if (mScannerListenerImp != null && isBluetoothLeRunning && isBluetoothClassicRunning) {
                            mScannerListenerImp.onScanFailed(new BluetoothScanError("Bluetooth close when scanning!"));
                        }
                    }
                    break;

                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
                    addBluetoothDevice(device, rssi, null);
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    isBluetoothClassicRunning = true;
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    if (isBluetoothClassicRunning) {
                        mHandler.sendEmptyMessage(MSG_AUTO_STOP_SEARCH);
                    }
                    isBluetoothClassicRunning = false;
                    break;

                default:
                    break;
            }
        }
    };

    protected void search(BluetoothRequest bluetoothRequest, IScannerListener scannerListenerImp) {
        mHandler = new Handler(Looper.getMainLooper(), this);
        mBluetoothRequest = bluetoothRequest;
        mScannerListenerImp = scannerListenerImp;
        mBleKitDeviceList = new LinkedHashMap<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        stopSearch();

        if (mBluetoothRequest.getTimeOut() > 0) {
            mHandler.sendEmptyMessageDelayed(MSG_STOP_SEARCH, mBluetoothRequest.getTimeOut());
        }

        switch (mBluetoothRequest.getSearchMethod()) {
            case BLUETOOTH_LE:
            default:
                startScan();
                break;

            case BLUETOOTH_CLASSIC:
                startDiscovery();
                break;

            case BLUETOOTH_BOTH:
                startScan();
                startDiscovery();
                break;
        }
    }

    protected void stopSearch() {
        mHandler.removeCallbacksAndMessages(null);
        stopScan();
        cancelDiscovery();
    }

    private void startDiscovery() {
        mBluetoothAdapter.startDiscovery();
    }

    private void cancelDiscovery() {
        mBluetoothAdapter.cancelDiscovery();
    }

    private void startScan() {
        isBluetoothLeRunning = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            UUID[] uuids = mBluetoothRequest.getServiceUuids().toArray(new UUID[mBluetoothRequest.getServiceUuids().size()]);
            if (uuids.length == 0) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mBluetoothAdapter.startLeScan(uuids, mLeScanCallback);
            }
        }
    }

    private void stopScan() {
        isBluetoothLeRunning = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_STOP_SEARCH:
                stopSearch();
                mScannerListenerImp.onScanFinished(new ArrayList<>(mBleKitDeviceList.values()));
                return true;

            case MSG_AUTO_STOP_SEARCH:
                mHandler.removeCallbacksAndMessages(null);
                stopScan();
                mScannerListenerImp.onScanFinished(new ArrayList<>(mBleKitDeviceList.values()));
                return true;

            default:
                break;
        }
        return false;
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            addBluetoothDevice(device, rssi, scanRecord);
        }
    };

    private void addBluetoothDevice(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        if (device == null || mBleKitDeviceList.containsKey(device.getAddress())) {
            return;
        }
        if (mBluetoothRequest.getDeviceNameList().size() > 0 && !mBluetoothRequest.getDeviceNameList().contains(device.getName())) {
            return;
        }
        if (mBluetoothRequest.getDeviceMacList().size() > 0 && !mBluetoothRequest.getDeviceMacList().contains(device.getAddress())) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                BluetoothKitDevice bluetoothKitDevice = new BluetoothKitDevice(device, rssi, scanRecord);
                mBleKitDeviceList.put(device.getAddress(), bluetoothKitDevice);
                mScannerListenerImp.onScanning(bluetoothKitDevice);
            }
        });
    }

    public class BluetoothBinder extends Binder {

        public BluetoothScanService getService() {
            return BluetoothScanService.this;
        }

    }

}
