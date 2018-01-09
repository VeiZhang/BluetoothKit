package com.excellence.bluetoothlibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.excellence.bluetoothlibrary.exception.BluetoothError;
import com.excellence.bluetoothlibrary.listener.IScannerListener;
import com.excellence.bluetoothlibrary.listener.IPermissionListener;

import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isBluetoothEnabled;
import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isLocationEnabled;
import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isSupportBluetooth;

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
	}

	public void search(BluetoothRequest request, IScannerListener listener)
	{
		mScannerListener = listener;
		mPermissionListener = new PermissionListener();
		/**
		 * 不支持蓝牙设备
		 */
		if (!isSupportBluetooth())
		{
			mPermissionListener.onBluetoothError(new BluetoothError("Your device don't support bluetooth!"));
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
			mScannerListener.onScanStarted();
		}

		@Override
		public void onBluetoothError(BluetoothError e)
		{
			mScannerListener.onScanFailed(e);
		}
	}
}
