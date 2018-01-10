package com.excellence.bluetoothlibrary;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.excellence.bluetoothlibrary.data.BluetoothKitDevice;
import com.excellence.bluetoothlibrary.callback.IScannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/9
 *     desc   :
 * </pre>
 */

public class BluetoothScanService extends Service implements Handler.Callback
{
	public static final String TAG = BluetoothScanService.class.getSimpleName();

	private static final int MSG_STOP_SEARCH = 0;

	private Handler mHandler = null;
	private BluetoothRequest mBluetoothRequest = null;
	private IScannerListener mScannerListenerImp = null;
	private List<BluetoothKitDevice> mBleKitDeviceList = null;
	private BluetoothAdapter mBluetoothAdapter = null;

	@Override
	public boolean onUnbind(Intent intent)
	{
		unregisterReceiver(mBluetoothStateReceiver);
		return super.onUnbind(intent);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent)
	{
		IntentFilter intentFilter = new IntentFilter();
		// 蓝牙开关状态
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(mBluetoothStateReceiver, intentFilter);
		return new BluetoothBinder();
	}

	private BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{

		}
	};

	protected void search(BluetoothRequest bluetoothRequest, IScannerListener scannerListenerImp)
	{
		mHandler = new Handler(Looper.getMainLooper(), this);
		mBluetoothRequest = bluetoothRequest;
		mScannerListenerImp = scannerListenerImp;
		mBleKitDeviceList = new ArrayList<>();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		switch (mBluetoothRequest.getSearchMethod())
		{
		case BLUETOOTH_LE:
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

	protected void stopSearch()
	{
		stopScan();
	}

	@SuppressLint("MissingPermission")
	private void startDiscovery()
	{
		mBluetoothAdapter.startDiscovery();
	}

	@SuppressLint({ "MissingPermission", "NewApi" })
	private void startScan()
	{
		mBluetoothAdapter.startLeScan(mBluetoothRequest.getServiceUuids().toArray(new UUID[mBluetoothRequest.getServiceUuids().size()]), mLeScanCallback);
		mHandler.removeCallbacksAndMessages(null);
		mHandler.sendEmptyMessageDelayed(MSG_STOP_SEARCH, mBluetoothRequest.getTimeOut());
	}

	@SuppressLint({ "MissingPermission", "NewApi" })
	private void stopScan()
	{
		mHandler.removeCallbacksAndMessages(null);
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
		case MSG_STOP_SEARCH:
			stopScan();
			mScannerListenerImp.onScanFinished(mBleKitDeviceList);
			return true;
		}
		return false;
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback()
	{
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
		{
			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					BluetoothKitDevice bluetoothKitDevice = new BluetoothKitDevice(device, rssi, scanRecord);
					mBleKitDeviceList.add(bluetoothKitDevice);
					mScannerListenerImp.onScanning(bluetoothKitDevice);
				}
			});
		}
	};

	public class BluetoothBinder extends Binder
	{

		public BluetoothScanService getService()
		{
			return BluetoothScanService.this;
		}

	}

}
