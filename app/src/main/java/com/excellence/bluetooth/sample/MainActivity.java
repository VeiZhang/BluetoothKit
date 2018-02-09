package com.excellence.bluetooth.sample;

import static com.excellence.bluetoothlibrary.BluetoothRequest.SearchMethod.BLUETOOTH_LE;

import java.util.List;

import com.excellence.bluetooth.sample.databinding.ActivityMainBinding;
import com.excellence.bluetoothlibrary.BluetoothClient;
import com.excellence.bluetoothlibrary.BluetoothRequest;
import com.excellence.bluetoothlibrary.callback.IScannerListener;
import com.excellence.bluetoothlibrary.data.BluetoothKitDevice;
import com.excellence.bluetoothlibrary.exception.BluetoothError;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		activityMainBinding.setOnClick(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				BluetoothRequest request = new BluetoothRequest.Builder().setSearchMethod(BLUETOOTH_LE).build();
				BluetoothClient.getInstance(MainActivity.this).addBluetoothRequest(request).addListener(new IScannerListener()
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
				}).search();
			}
		});
	}
}
