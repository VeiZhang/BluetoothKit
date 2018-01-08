package com.excellence.bluetoothlibrary;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;

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
	/**
	 * 是否支持经典蓝牙
	 *
	 * @return
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
	@SuppressLint("MissingPermission")
	public static boolean isBluetoothEnabled()
	{
		return isSupportBluetooth() && BluetoothAdapter.getDefaultAdapter().isEnabled();
	}
}
