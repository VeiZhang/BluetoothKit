package com.excellence.bluetooth.sample;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.excellence.bluetooth.sample.databinding.ActivityMainBinding;
import com.excellence.bluetoothlibrary.BluetoothClient;
import com.excellence.bluetoothlibrary.BluetoothRequest;

import static com.excellence.bluetoothlibrary.BluetoothRequest.SearchMethod.BLUETOOTH_LE;

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
				BluetoothClient.getInstance(MainActivity.this).search(request, null);
			}
		});
	}
}
