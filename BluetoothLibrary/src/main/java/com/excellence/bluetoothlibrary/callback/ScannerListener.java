package com.excellence.bluetoothlibrary.callback;

import com.excellence.bluetoothlibrary.data.BluetoothKitDevice;
import com.excellence.bluetoothlibrary.exception.BluetoothError;

import java.util.List;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/8
 *     desc   :
 * </pre>
 */

public class ScannerListener implements IScannerListener
{

	@Override
	public void onScanStarted()
	{

	}

	@Override
	public void onScanning(BluetoothKitDevice device)
	{

	}

	@Override
	public void onScanFinished(List<BluetoothKitDevice> deviceList)
	{

	}

	@Override
	public void onScanFailed(BluetoothError e)
	{

	}
}
