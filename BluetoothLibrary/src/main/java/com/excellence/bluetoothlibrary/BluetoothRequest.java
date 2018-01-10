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
	private SearchMethod mSearchMethod = SearchMethod.BLUETOOTH_LE;

	private BluetoothRequest(Builder builder)
	{
		mDeviceNameList = builder.mDeviceNameList;
		mDeviceMacList = builder.mDeviceMacList;
		mServiceUuids = builder.mServiceUuids;
		mTimeOut = builder.mTimeOut;
		isAutoConnect = builder.isAutoConnect;
		mSearchMethod = builder.mSearchMethod;
	}

	public List<String> getDeviceNameList()
	{
		return mDeviceNameList;
	}

	public List<String> getDeviceMacList()
	{
		return mDeviceMacList;
	}

	public List<UUID> getServiceUuids()
	{
		return mServiceUuids;
	}

	public long getTimeOut()
	{
		return mTimeOut;
	}

	public boolean isAutoConnect()
	{
		return isAutoConnect;
	}

	public SearchMethod getSearchMethod()
	{
		return mSearchMethod;
	}

	public static class Builder
	{
		private static final long DEFAULT_TIME_OUT = 10 * 1000;

		private List<String> mDeviceNameList = null;
		private List<String> mDeviceMacList = null;
		private List<UUID> mServiceUuids = null;
		private long mTimeOut = DEFAULT_TIME_OUT;
		private boolean isAutoConnect = false;
		private SearchMethod mSearchMethod = SearchMethod.BLUETOOTH_LE;

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
		 * 设置搜索超时时间，默认10s，<0表示不限制超时时间
		 *
		 * @param time unit: ms
		 * @return
		 */
		public Builder setScanTimeOut(long time)
		{
			mTimeOut = time;
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
		 * 蓝牙搜索方式
		 * @see SearchMethod#BLUETOOTH_LE		低功耗蓝牙 - 默认
		 * @see SearchMethod#BLUETOOTH_CLASSIC	经典蓝牙
		 * @see SearchMethod#BLUETOOTH_BOTH		两种方式
		 *
		 * @param searchMethod
		 * @return
		 */
		public Builder setSearchMethod(SearchMethod searchMethod)
		{
			mSearchMethod = searchMethod;
			return this;
		}

		public BluetoothRequest build()
		{
			return new BluetoothRequest(this);
		}
	}

	public enum SearchMethod
	{
		BLUETOOTH_LE, BLUETOOTH_CLASSIC, BLUETOOTH_BOTH
	}
}
