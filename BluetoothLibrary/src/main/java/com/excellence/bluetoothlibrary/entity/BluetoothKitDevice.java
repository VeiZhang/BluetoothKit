package com.excellence.bluetoothlibrary.entity;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/10
 *     desc   : 搜索结果的蓝牙设备
 * </pre>
 */

public class BluetoothKitDevice implements Parcelable
{

	private BluetoothDevice bluetoothDevice;
	private int rssi;
	private byte[] scanRecord;

	public BluetoothKitDevice(BluetoothDevice bluetoothDevice)
	{
		this(bluetoothDevice, 0, null);
	}

	public BluetoothKitDevice(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord)
	{
		this.bluetoothDevice = bluetoothDevice;
		this.rssi = rssi;
		this.scanRecord = scanRecord;
	}

	public String getName()
	{
		return bluetoothDevice == null ? "" : bluetoothDevice.getName();
	}

	public String getAddress()
	{
		return bluetoothDevice == null ? "" : bluetoothDevice.getAddress();
	}

	public BluetoothDevice getBluetoothDevice()
	{
		return bluetoothDevice;
	}

	public int getRssi()
	{
		return rssi;
	}

	public byte[] getScanRecord()
	{
		return scanRecord;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeParcelable(this.bluetoothDevice, flags);
		dest.writeInt(this.rssi);
		dest.writeByteArray(this.scanRecord);
	}

	protected BluetoothKitDevice(Parcel in)
	{
		this.bluetoothDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
		this.rssi = in.readInt();
		this.scanRecord = in.createByteArray();
	}

	public static final Parcelable.Creator<BluetoothKitDevice> CREATOR = new Parcelable.Creator<BluetoothKitDevice>()
	{
		@Override
		public BluetoothKitDevice createFromParcel(Parcel source)
		{
			return new BluetoothKitDevice(source);
		}

		@Override
		public BluetoothKitDevice[] newArray(int size)
		{
			return new BluetoothKitDevice[size];
		}
	};
}
