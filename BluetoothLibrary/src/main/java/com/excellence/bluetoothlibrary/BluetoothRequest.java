package com.excellence.bluetoothlibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.text.TextUtils;

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
	private boolean isPermissionCheck = true;
	private SearchMethod mSearchMethod = SearchMethod.BLUETOOTH_LE;

	private BluetoothRequest(Builder builder)
	{
		mDeviceNameList = builder.mDeviceNameList;
		mDeviceMacList = builder.mDeviceMacList;
		mServiceUuids = builder.mServiceUuids;
		mTimeOut = builder.mTimeOut;
		isPermissionCheck = builder.isPermissionCheck;
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

	public boolean isPermissionCheck()
	{
		return isPermissionCheck;
	}

	public SearchMethod getSearchMethod()
	{
		return mSearchMethod;
	}

	public static class Builder
	{
		private static final long DEFAULT_TIME_OUT = 20 * 1000;

		private List<String> mDeviceNameList = null;
		private List<String> mDeviceMacList = null;
		private List<UUID> mServiceUuids = null;
		private long mTimeOut = DEFAULT_TIME_OUT;
		private boolean isPermissionCheck = true;
		private SearchMethod mSearchMethod = SearchMethod.BLUETOOTH_BOTH;

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
					{
						mDeviceNameList.add(name);
					}
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
					{
						mDeviceMacList.add(mac);
					}
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
					{
						mServiceUuids.add(UUID.fromString(uuid));
					}
				}
			}
			return this;
		}

		/**
		 * 设置搜索超时时间，默认20s，<=0表示不限制超时时间
		 *
		 * {@link SearchMethod#BLUETOOTH_CLASSIC} 经典蓝牙自动停止搜索，同时，低功耗蓝牙也停止搜索
		 * {@link SearchMethod#BLUETOOTH_LE} 低功耗蓝牙设置搜索的超时时间
		 * 此设置项是对两个同时停止
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
		 * 是否自动检测权限
		 * <p>
		 *     默认，启用，；但是有些设备，如Android机顶盒有些使用打开蓝牙的方式打不开，一直卡住，此时则关闭自动检测
		 *
		 *     如果关闭了权限检测后，不能搜索出蓝牙，很可能是需要权限，最终办法是设置 targetSdkVersion < 23
		 *     Need ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission to get scan results
		 * </p>
		 *
		 * @param permissionCheck
		 */
		public Builder isPermissionCheck(boolean permissionCheck)
		{
			isPermissionCheck = permissionCheck;
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
