package com.excellence.bluetoothlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.excellence.bluetoothlibrary.exception.BluetoothEnabledError;
import com.excellence.bluetoothlibrary.exception.BluetoothPermissionError;
import com.excellence.bluetoothlibrary.listener.IPermissionListener;

import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isBluetoothEnabled;
import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isLocationEnabled;
import static com.excellence.bluetoothlibrary.util.BluetoothUtil.isSupportBluetooth;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/8
 *     desc   : 蓝牙权限申请：开启蓝牙、打开定位
 *              {@link BluetoothAdapter#isEnabled()}
 *              {@link android.provider.Settings#ACTION_LOCATION_SOURCE_SETTINGS}
 * </pre>
 */

public class PermissionActivity extends Activity
{
	public static final String TAG = PermissionActivity.class.getSimpleName();

	private static final int BLUETOOTH_ENABLED_REQUEST_CODE = 1024;
	private static final int LOCATION_ENABLED_REQUEST_CODE = 2048;

	private static IPermissionListener mPermissionListener = null;

	protected static void setPermissionListener(IPermissionListener permissionListener)
	{
		mPermissionListener = permissionListener;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (mPermissionListener == null || !isSupportBluetooth())
		{
			finish();
			return;
		}

		if (!isBluetoothEnabled())
		{
			Log.w(TAG, "onCreate: bluetooth is not enabled");
			Intent enabledBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enabledBluetoothIntent, BLUETOOTH_ENABLED_REQUEST_CODE);
		}
		else if (!isLocationEnabled(this))
		{
			Log.w(TAG, "onCreate: location is not enabled");
			final Intent enabledLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			if (enabledLocationIntent.resolveActivity(getPackageManager()) == null)
			{
				Log.w(TAG, "onCreate: location setting is not exists");
				mPermissionListener.onBluetoothPermissionGranted();
				mPermissionListener = null;
				finish();
				return;
			}

			new AlertDialog.Builder(this).setCancelable(false).setTitle(R.string.setting_prompt).setMessage(R.string.open_location)
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							mPermissionListener.onBluetoothError(new BluetoothPermissionError("Cancel, location permission denied!"));
							mPermissionListener = null;
							finish();
						}
					}).setPositiveButton(R.string.open, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							startActivityForResult(enabledLocationIntent, LOCATION_ENABLED_REQUEST_CODE);
						}
					}).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (mPermissionListener != null)
		{
			switch (requestCode)
			{
			case BLUETOOTH_ENABLED_REQUEST_CODE:
				if (isBluetoothEnabled())
				{
					mPermissionListener.onBluetoothEnabled();
				}
				else
				{
					mPermissionListener.onBluetoothError(new BluetoothEnabledError("Bluetooth open failed!"));
				}
				break;

			case LOCATION_ENABLED_REQUEST_CODE:
				if (isLocationEnabled(this))
				{
					mPermissionListener.onBluetoothPermissionGranted();
				}
				else
				{
					mPermissionListener.onBluetoothError(new BluetoothPermissionError("Setting, location permission denied!"));
				}
				break;
			}
			mPermissionListener = null;
		}
		finish();
	}

}
