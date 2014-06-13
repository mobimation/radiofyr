package com.mobimation.radiofyr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mobimation.radiofyr.R;
import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.RangingListener;
import com.aprilbrother.aprilbrothersdk.Region;


/**
 * 
 * Radiofyr list beacons
 * 
 * @author Gunnar Forsgren, Mobimation AB, Sweden
 * 
 */
public class BeaconList extends Activity {
	private int count=0;
	private static final int REQUEST_ENABLE_BT = 1234;
	private static final String TAG = "MainActivity";
	// private static final Region ALL_BEACONS_REGION = new Region("apr",
	// "B9407F30-F5F8-466E-AFF9-25556B57FE6D",
	// null, null);
	private static final Region ALL_BEACONS_REGION = new Region("apr", null,
			null, null);
	private BeaconAdapter2 adapter;
	private BeaconManager beaconManager;
	private ArrayList<Beacon> myBeacons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		init();
		Log.e("onCreate","Init done");
	}

	/**
	 * Init beacon list operation
	 */
	private void init() {
		myBeacons = new ArrayList<Beacon>();
		ListView lv = (ListView) findViewById(R.id.lv);
		adapter = new BeaconAdapter2(this.getApplicationContext(),myBeacons);
		lv.setAdapter(adapter);
		beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(new RangingListener() {

			@Override
			public void onBeaconsDiscovered(Region region,
					final List<Beacon> beacons) {

				     myBeacons.addAll(beacons);
				     Log.e(TAG, "Radiofyr hittade "+count++ +" radiofyrar");

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
/*						getActionBar().setSubtitle(
								"Found beacons: " + beacons.size()); */
						adapter.replaceWith(beacons);
					}
				});
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(BeaconList.this,
						ModifyActivity.class);
				Beacon beacon = myBeacons.get(arg2);
				Bundle bundle = new Bundle();
				bundle.putParcelable("beacon", beacon);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});
	}

	/**
	 * Connect to scanning service
	 */
	private void connectToService() {
		getActionBar().setSubtitle("Söker...");
		adapter.replaceWith(Collections.<Beacon> emptyList());
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_BEACONS_REGION);
				} catch (RemoteException e) {
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				connectToService();
			} else {
				Toast.makeText(this, "Bluetooth inte aktiverat", Toast.LENGTH_LONG)
						.show();
				getActionBar().setSubtitle("Bluetooth inte aktiverat");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy",
					Toast.LENGTH_LONG).show();
			return;
		}

		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}
	}

	@Override
	protected void onDestroy() {
		beaconManager.disconnect();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		try {
			myBeacons.clear();
			beaconManager.stopRanging(ALL_BEACONS_REGION);
		} catch (RemoteException e) {
			Log.d(TAG, "Error while stopping ranging", e);
		}
		super.onStop();
	}
}
