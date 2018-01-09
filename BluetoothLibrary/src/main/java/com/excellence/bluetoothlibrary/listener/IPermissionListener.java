package com.excellence.bluetoothlibrary.listener;

import com.excellence.bluetoothlibrary.exception.BluetoothError;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/8
 *     desc   : 打开蓝牙、定位的回调
 * </pre>
 */

public interface IPermissionListener
{
	void onBluetoothEnabled();

	void onBluetoothPermissionGranted();

	void onBluetoothError(BluetoothError e);
}
