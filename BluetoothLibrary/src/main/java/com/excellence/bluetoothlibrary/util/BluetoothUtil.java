package com.excellence.bluetoothlibrary.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/8
 *     desc   :
 * </pre>
 */

public class BluetoothUtil
{
	public static final String TAG = BluetoothUtil.class.getSimpleName();

	/**
	 * 是否支持经典蓝牙
	 *
	 * @return {@code true}:支持<br>{@code false}:不支持
	 */
	public static boolean isSupportBluetooth()
	{
		return BluetoothAdapter.getDefaultAdapter() != null;
	}

	/**
	 * 是否支持BLE低功耗蓝牙
	 *
	 * @return
	 */
	public static boolean isSupportBle()
	{
		return isSupportBluetooth() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
	}

	/**
	 * 是否打开了蓝牙
	 *
	 * @return
	 */
	public static boolean isBluetoothEnabled()
	{
		return isSupportBluetooth() && BluetoothAdapter.getDefaultAdapter().isEnabled();
	}

	/**
	 * 定位是否打开，针对TargetSDKVersion>=23
	 *
	 * @param context
	 * @return
	 */
	public static boolean isLocationEnabled(Context context)
	{
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager == null || locationManager.getAllProviders() == null || locationManager.getAllProviders().size() == 0)
		{
			/**
			 * 不支持定位，则默认为打开状态
			 */
			Log.w(TAG, "isGpsOEnabled: location manager is null or location providers is empty");
			return true;
		}

		if (!locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) && !locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
		{
			/**
			 * 没有gps或网络模块，则默认为打开状态
			 */
			Log.w(TAG, "isGpsOEnabled: NO gps or network devices");
			return true;
		}

		boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		Log.e(TAG, "isGpsEnabled: Gps enabled : " + gpsEnabled + " - Network enabled : " + networkEnabled);
		return gpsEnabled || networkEnabled;
	}
}
