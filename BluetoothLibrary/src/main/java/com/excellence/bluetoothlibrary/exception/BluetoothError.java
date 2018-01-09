package com.excellence.bluetoothlibrary.exception;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/9
 *     desc   : 蓝牙异常
 * </pre>
 */

public class BluetoothError extends Exception
{

	public BluetoothError()
	{
		super();
	}

	public BluetoothError(String message)
	{
		super(message);
	}

	public BluetoothError(String message, Throwable cause)
	{
		super(message, cause);
	}

	public BluetoothError(Throwable cause)
	{
		super(cause);
	}
}
