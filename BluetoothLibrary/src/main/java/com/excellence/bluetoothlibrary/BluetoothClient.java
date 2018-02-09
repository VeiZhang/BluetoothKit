package com.excellence.bluetoothlibrary;

import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isBluetoothEnabled;
import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isLocationEnabled;
import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isSupportBle;
import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isSupportBluetooth;

import java.util.List;

import com.excellence.bluetoothlibrary.callback.IPermissionListener;
import com.excellence.bluetoothlibrary.callback.IScannerListener;
import com.excellence.bluetoothlibrary.data.BluetoothKitDevice;
import com.excellence.bluetoothlibrary.exception.BluetoothError;
import com.excellence.bluetoothlibrary.exception.BluetoothSupportError;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/12/29
 *     desc   : 蓝牙工具类
 *     			权限 {@link android.Manifest.permission#BLUETOOTH}
 *     				{@link android.Manifest.permission#BLUETOOTH_ADMIN}
 *     				{@link android.Manifest.permission#BLUETOOTH_PRIVILEGED}
 *					{@link android.Manifest.permission#ACCESS_FINE_LOCATION}
 *					{@link android.Manifest.permission#ACCESS_COARSE_LOCATION}
 * </pre>
 */

public class BluetoothClient
{
	public static final String TAG = BluetoothClient.class.getSimpleName();

	private static BluetoothClient mInstance = null;

	private Context mContext = null;
	private BluetoothScanService mBluetoothScanService = null;
	private boolean isServiceBind = false;
	private BluetoothRequest mBluetoothRequest = null;
	private IScannerListener mScannerListenerImp = null;
	private IScannerListener mScannerListener = null;
	private IPermissionListener mPermissionListener = null;

	public static BluetoothClient getInstance(Context context)
	{
		if (mInstance == null)
			mInstance = new BluetoothClient(context.getApplicationContext());
		return mInstance;
	}

	public BluetoothClient(Context context)
	{
		mContext = context;
		mScannerListenerImp = new ScannerListenerImp();
		mPermissionListener = new PermissionListener();
	}

	public BluetoothClient addBluetoothRequest(BluetoothRequest bluetoothRequest)
	{
		mBluetoothRequest = bluetoothRequest;
		return this;
	}

	public BluetoothClient addListener(IScannerListener listener)
	{
		mScannerListener = listener;
		return this;
	}

	public void search()
	{
		if (mBluetoothRequest == null)
		{
			mBluetoothRequest = new BluetoothRequest.Builder().build();
		}

		/**
		 * 不支持蓝牙设备
		 */
		if (!isSupportBluetooth())
		{
			mPermissionListener.onBluetoothError(new BluetoothSupportError("Your device don't support bluetooth!"));
			return;
		}

		/**
		 * 只对BLE方式，不支持低功耗设备
		 */
		if (mBluetoothRequest.getSearchMethod() == BluetoothRequest.SearchMethod.BLUETOOTH_LE && !isSupportBle())
		{
			mPermissionListener.onBluetoothError(new BluetoothSupportError("Your device don't support bluetooth le!"));
			return;
		}

		/**
		 * 打开蓝牙
		 */
		if (!isBluetoothEnabled())
		{
			startPermissionActivity();
		}
		else
		{
			mPermissionListener.onBluetoothEnabled();
		}
	}

	private void startPermissionActivity()
	{
		PermissionActivity.setPermissionListener(mPermissionListener);
		Intent intent = new Intent(mContext, PermissionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	public void stopSearch()
	{
		if (mBluetoothScanService == null)
		{
			Log.e(TAG, "stopSearch: please search first");
			return;
		}
		mBluetoothScanService.stopSearch();
	}

	private class PermissionListener implements IPermissionListener
	{
		@Override
		public void onBluetoothEnabled()
		{
			int targetSdk = mContext.getApplicationInfo().targetSdkVersion;
			if (targetSdk >= Build.VERSION_CODES.M && !isLocationEnabled(mContext))
			{
				/**
				 * 打开GPS，或者网络定位
				 */
				startPermissionActivity();
			}
			else
			{
				mPermissionListener.onBluetoothPermissionGranted();
			}
		}

		@Override
		public void onBluetoothPermissionGranted()
		{
			mScannerListenerImp.onScanStarted();
		}

		@Override
		public void onBluetoothError(BluetoothError e)
		{
			mScannerListenerImp.onScanFailed(e);
		}
	}

	private class ScannerListenerImp implements IScannerListener
	{
		@Override
		public void onScanStarted()
		{
			if (mScannerListener != null)
				mScannerListener.onScanStarted();

			if (isServiceBind)
				mContext.unbindService(mServiceConnection);
			isServiceBind = mContext.bindService(new Intent(mContext, BluetoothScanService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
		}

		@Override
		public void onScanning(BluetoothKitDevice device)
		{
			if (mScannerListener != null)
				mScannerListener.onScanning(device);
		}

		@Override
		public void onScanFinished(List<BluetoothKitDevice> deviceList)
		{
			if (mScannerListener != null)
				mScannerListener.onScanFinished(deviceList);
		}

		@Override
		public void onScanFailed(BluetoothError e)
		{
			if (mScannerListener != null)
				mScannerListener.onScanFailed(e);
		}
	}

	private ServiceConnection mServiceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			mBluetoothScanService = ((BluetoothScanService.BluetoothBinder) service).getService();
			mBluetoothScanService.search(mBluetoothRequest, mScannerListenerImp);
		}

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			mBluetoothScanService = null;
		}
	};
}
