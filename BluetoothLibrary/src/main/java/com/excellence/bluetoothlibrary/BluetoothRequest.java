package com.excellence.bluetoothlibrary;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/8
 *     desc   : 蓝牙搜索
 * </pre>
 */

public class BluetoothRequest
{
	private List<String> mDeviceNameList = null;
	private List<String> mDeviceMacList = null;
	private List<UUID> mServiceUuids = null;
	private long mTimeOut = 0;
	private boolean isAutoConnect = false;
	private boolean isBluetoothLe = true;
	private boolean isBluetoothClassic = false;

	private BluetoothRequest(Builder builder)
	{
		mDeviceNameList = builder.mDeviceNameList;
		mDeviceMacList = builder.mDeviceMacList;
		mServiceUuids = builder.mServiceUuids;
		mTimeOut = builder.mTimeOut;
		isAutoConnect = builder.isAutoConnect;
		isBluetoothLe = builder.isBluetoothLe;
		isBluetoothClassic = builder.isBluetoothClassic;
	}

	public static class Builder
	{
		private static final long DEFAULT_TIME_OUT = 10 * 1000;

		private List<String> mDeviceNameList = null;
		private List<String> mDeviceMacList = null;
		private List<UUID> mServiceUuids = null;
		private long mTimeOut = DEFAULT_TIME_OUT;
		private boolean isAutoConnect = false;
		private boolean isBluetoothLe = true;
		private boolean isBluetoothClassic = false;

		public Builder()
		{
			mDeviceNameList = new ArrayList<>();
			mDeviceMacList = new ArrayList<>();
			mServiceUuids = new ArrayList<>();
		}

		/**
		 * 过滤蓝牙名称
		 *
		 * @param nameList
		 * @return
		 */
		public Builder addDeviceName(String... nameList)
		{
			if (nameList != null && nameList.length > 0)
			{
				for (String name : nameList)
				{
					if (!TextUtils.isEmpty(name))
						mDeviceNameList.add(name);
				}
			}
			return this;
		}

		/**
		 * 过滤蓝牙Mac地址
		 *
		 * @param macList
		 * @return
		 */
		public Builder addDeviceMac(String... macList)
		{
			if (macList != null && macList.length > 0)
			{
				for (String mac : macList)
				{
					if (!TextUtils.isEmpty(mac))
						mDeviceMacList.add(mac);
				}
			}
			return this;
		}

		/**
		 * 过滤UUID
		 *
		 * @param uuidList
		 * @return
		 */
		public Builder addServiceUuid(String... uuidList)
		{
			if (uuidList != null && uuidList.length > 0)
			{
				for (String uuid : uuidList)
				{
					if (!TextUtils.isEmpty(uuid))
						mServiceUuids.add(UUID.fromString(uuid));
				}
			}
			return this;
		}

		/**
		 * 设置搜索超时时间
		 *
		 * @param time unit: ms
		 * @return
		 */
		public Builder setScanTimeOut(long time)
		{
			if (time > 0)
			{
				mTimeOut = time;
			}
			return this;
		}

		/**
		 * 是否自动连接
		 *
		 * @param isAutoConnect
		 * @return
		 */
		public Builder isAutoConnect(boolean isAutoConnect)
		{
			this.isAutoConnect = isAutoConnect;
			return this;
		}

		/**
		 * 搜索方式: BLE低功耗蓝牙 - 默认true
		 *
		 * @param isBluetoothLe
		 * @return
		 */
		public Builder isBluetoothLe(boolean isBluetoothLe)
		{
			this.isBluetoothLe = isBluetoothLe;
			return this;
		}

		/**
		 * 搜索方式: 经典蓝牙 - 默认false
		 *
		 * @param isBluetoothClassic
		 * @return
		 */
		public Builder isBluetoothClassic(boolean isBluetoothClassic)
		{
			this.isBluetoothClassic = isBluetoothClassic;
			return this;
		}

		public BluetoothRequest build()
		{
			return new BluetoothRequest(this);
		}
	}
}
