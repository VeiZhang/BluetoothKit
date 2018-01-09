package com.excellence.bluetoothlibrary.listener;

import com.excellence.bluetoothlibrary.exception.BluetoothError;

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
	void onScanning();

	/**
	 * 扫描结束
	 */
	void onScanFinished();

	/**
	 * 搜索失败异常
	 *
	 * @param e
	 */
	void onScanFailed(BluetoothError e);

}
