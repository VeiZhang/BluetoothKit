package com.excellence.bluetoothlibrary.callback;

import com.excellence.bluetoothlibrary.data.BluetoothKitDevice;
import com.excellence.bluetoothlibrary.exception.BluetoothError;

import java.util.List;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/8
 *     desc   : 蓝牙搜索回调
 * </pre>
 */

public interface IScannerListener
{
	/**
	 * 开始搜索
	 */
	void onScanStarted();

	/**
	 * 搜索中
	 */
	void onScanning(BluetoothKitDevice device);

	/**
	 * 扫描结束
	 */
	void onScanFinished(List<BluetoothKitDevice> deviceList);

	/**
	 * 搜索失败异常
	 *
	 * @param e
	 */
	void onScanFailed(BluetoothError e);

}
