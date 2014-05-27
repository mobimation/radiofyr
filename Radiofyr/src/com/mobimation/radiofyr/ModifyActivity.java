package com.mobimation.radiofyr;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobimation.radiofyr.R;
import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.Region;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconCharacteristics;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconCharacteristics.MyReadCallBack;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconConnection;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconConnection.MyWriteCallback;
/**
 * Activity to modify beacon parameters.
 * 
 * @author gunnarforsgren
 *
 */
public class ModifyActivity extends Activity {

	protected static final String TAG = "ModifyActivity";
	private EditText uuid;
	private EditText major;
	private EditText minor;
	private Beacon beacon;

	private AprilBeaconConnection conn;
	private TextView battery;
	private TextView txpower;
	private TextView advinterval;

	private AprilBeaconCharacteristics read;

	private static final int READBATTERY = 0;

	private static final int READTXPOWER = 1;

	private static final int READADVINTERVAL = 2;

	private BeaconManager beaconManager;
	private static final Region ALL_BEACONS_REGION = new Region("apr", null,
			null, null);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_modify2);
		super.onCreate(savedInstanceState);
		// Wire up buttons to handlers
		findViewById(R.id.buttonWriteUUID) .setOnClickListener(writeUUID);
		findViewById(R.id.buttonWriteMajor).setOnClickListener(writeMajor);
		findViewById(R.id.buttonWriteMinor).setOnClickListener(writeMinor);
		findViewById(R.id.buttonWriteTXPower).setOnClickListener(writeTXPower);
		findViewById(R.id.buttonWriteAdvInterval).setOnClickListener(writeAdvInterval);
		findViewById(R.id.buttonWriteFgScanPeriod).setOnClickListener(writeFgScanPeriod);
		findViewById(R.id.buttonWriteBgScanPeriod).setOnClickListener(writeBgScanPeriod);
		init();
	}

    private View.OnClickListener writeUUID = 
    	  new View.OnClickListener() {
    		@Override
        public void onClick(View v) {
    			
        	  EditText uuid1=(EditText)findViewById(R.id.uuidhi);
          EditText uuid2=(EditText)findViewById(R.id.uuidlo);
          String UUID=
        		  uuid1.getText().toString()+
        		  uuid2.getText().toString();
          Log.d("ModifyActivity", "writeUUID="+UUID);
          conn.writeUUID(UUID); 
        		  
  		  conn.connectGattToWrite(
  		    
  		   new AprilBeaconConnection.MyWriteCallback() {
			
  			@Override
			public void onWriteMajorSuccess(final int oldMajor, final int newMajor) {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity",
								"Wrote Major value, oldMajor=" + oldMajor + "newMajor=" + newMajor);
					}
				  }
			    );
		    }
  			
  			@Override
			public void onWriteMinorSuccess(final int oldMinor, final int newMinor) {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity",
								"Wrote Minor value, oldMinor=" + oldMinor + "newMinor=" + newMinor);
					}
				  }
			    );
		    }
			
  			@Override
			public void onWritePasswordSuccess() {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity", "Write Password Success");
					}
				  }
			    );
		    }
			
  			@Override
			public void onErrorOfPassword() {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity", "Password Error");
					}
				  }
			    );
		    }
  			
			@Override
			public void onErrorOfConnection() {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity", "Connection Error");
					}
				  }
			    );
		    }
			
			@Override
			public void onErrorOfDiscoveredServices() {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity", "Error requesting Discovered Services");
					}
				  }
			    );
		    }
  			
			@Override
			public void onWriteUUIDSuccess() {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity", "Success writing UUID");
					}
				  }
			    );
		    }
  			
			@Override
			public void onWritewriteAdvertisingIntervalSuccessSuccess() {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity", "Success writing Advertising Interval");
					}
				  }
			    );
		    } 			
			
			@Override
			public void onWritewriteTxPowerSuccessSuccess() {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity", "Success writing TX Power");
					}
				  }
			    );
		    } 			
			
  		   } 
  		  );  
    	    }
  	  };
    
    private View.OnClickListener writeMajor = 
    	  new View.OnClickListener() {
    	  @Override
        public void onClick(View v) {
            //Inform the user the button has been clicked
    		  Log.d("ModifyActivity","writeMajor()");
    		  
    		  
        	  EditText majorText=(EditText)findViewById(R.id.major);
          String majorv=
        		  majorText.getText().toString();
        		  
          Log.d("ModifyActivity", "writeMajor="+majorv);
          conn.writeMajor(new Integer(majorv).intValue()); 
        		  
  		  conn.connectGattToWrite(
  		    
  		   new AprilBeaconConnection.MyWriteCallback() {
			
  			@Override
			public void onWriteMajorSuccess(final int oldMajor, final int newMajor) {
			    ModifyActivity.this.runOnUiThread(
			      new Runnable() {
					@Override
					public void run() {
						Log.d("ModifyActivity",
								"Wrote Major value, oldMajor=" + oldMajor + "newMajor=" + newMajor);
					}
				  }
			    );
		    }
  		   }
		);
    		    
        }
      };
      
      private View.OnClickListener writeMinor = 
        	  new View.OnClickListener() {
        	  @Override
            public void onClick(View v) {
                //Inform the user the button has been clicked
        		  Log.d("ModifyActivity","writeMinor()");
        		  
        		  
            	  EditText minorText=(EditText)findViewById(R.id.minor);
              String minorv=
            		  minorText.getText().toString();
            		  
              Log.d("ModifyActivity", "writeMinor="+minorv);
              conn.writeMinor(new Integer(minorv).intValue()); 
            		  
      		  conn.connectGattToWrite(
      		    
      		   new AprilBeaconConnection.MyWriteCallback() {
    			
      			@Override
    			public void onWriteMinorSuccess(final int oldMinor, final int newMinor) {
    			    ModifyActivity.this.runOnUiThread(
    			      new Runnable() {
    					@Override
    					public void run() {
    						Log.d("ModifyActivity",
    								"Wrote Minor value, oldMinor=" + oldMinor + "newMinor=" + newMinor);
    					}
    				  }
    			    );
    		    }
      		   }
    		);
        		    
            }
          };
    private View.OnClickListener writeTXPower = 
      new View.OnClickListener() {
    	  @Override
        public void onClick(final View v) {
            //Inform the user the button has been clicked
    		  Log.d("ModifyActivity","writeTxPower()");
        }
      };
    private View.OnClickListener writeAdvInterval = 
     new View.OnClickListener() {
    	  @Override
        public void onClick(final View v) {
            //Inform the user the button has been clicked    
    		  Log.d("ModifyActivity","writeAdvInterval()");
        }
     };
    private View.OnClickListener writeFgScanPeriod =
    	 new View.OnClickListener() {
    	  @Override
        public void onClick(final View v) {
            //Inform the user the button has been clicked 
    		  Log.d("ModifyActivity","writeFgScanPeriod()");
        }
     };
    private View.OnClickListener writeBgScanPeriod = 
    	 new View.OnClickListener() {
    	  @Override
        public void onClick(final View v) {
    		  Log.d("ModifyActivity","writeBgScanPeriod()");
            //Inform the user the button has been clicked               
        }
     };
     
	public void getBattery(View v) {
		setBattery();
	}

	public void getTxPower(View v) {
		setTxPower();
	}

	public void getAdvinterval(View v) {
		setAdvinterval();
	}

	private void setAdvinterval() {
		read = new AprilBeaconCharacteristics(this, beacon);
		read.connectGattToRead(new MyReadCallBack() {
			@Override
			public void readyToGetAdvinterval() {
				final Integer mAdvinterval;
				try {
					mAdvinterval = read.getAdvinterval();
					ModifyActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							advinterval.setText(mAdvinterval + "00ms");
							read.close();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, READADVINTERVAL);
	}

	private void setTxPower() {
		read = new AprilBeaconCharacteristics(this, beacon);
		read.connectGattToRead(new MyReadCallBack() {
			@Override
			public void readyToGetTxPower() {
				final Integer txpowervalue;
				try {
					txpowervalue = read.getTxPower();
					ModifyActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							switch (txpowervalue) {
							case 0:
								txpower.setText("0dBm (Normal)");
								read.close();
								break;
							case 1:
								txpower.setText("4dBm (Strong)");
								read.close();
								break;
							case 2:
								txpower.setText("-6dBm (Weak)");
								read.close();
								break;
							case 3:
								txpower.setText("-23dBm (Weakest)");
								read.close();
								break;
							default:
								break;
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, READTXPOWER);
	}

	private void setBattery() {
		read = new AprilBeaconCharacteristics(this, beacon);
		read.connectGattToRead(new MyReadCallBack() {
			@Override
			public void readyToGetBattery() {
				final Integer battery2;
				try {
					battery2 = read.getBattery();
					ModifyActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							battery.setText(battery2 + "%");
							read.close();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, READBATTERY);
	}

	private void init() {
	/*	uuid = (EditText) findViewById(R.id.uuid);
		major = (EditText) findViewById(R.id.major);
		minor = (EditText) findViewById(R.id.minor);
		battery = (TextView) findViewById(R.id.battery);
		txpower = (TextView) findViewById(R.id.txpower);
		advinterval = (TextView) findViewById(R.id.advinterval); */
		Bundle bundle = getIntent().getExtras();
		beacon = bundle.getParcelable("beacon");
		conn = new AprilBeaconConnection(this, beacon);

		Log.i(TAG, "Mac address="+beacon.getMacAddress());
		Log.i(TAG, "Major number="+beacon.getMajor());
		Log.i(TAG, "Minor number="+beacon.getMinor());
		Log.i(TAG, "Mac address="+beacon.getMacAddress());
		String proximityUUID = beacon.getProximityUUID();
		/* uuid.setText(proximityUUID); */
	}

	public void sure(View v) {
		aprilWrite();
	}

	private void aprilWrite() {
		conn = new AprilBeaconConnection(this, beacon);

		if (!TextUtils.isEmpty(major.getText().toString())) {
			int newMajor = Integer.parseInt(major.getText().toString());
			conn.writeMajor(newMajor);
		}
		if (!TextUtils.isEmpty(minor.getText().toString())) {
			int newMinor = Integer.parseInt(minor.getText().toString());
			conn.writeMinor(newMinor);
		}
		if (!TextUtils.isEmpty(uuid.getText().toString())) {
			String newUuid = uuid.getText().toString();
			conn.writeUUID(newUuid);
		}

		conn.connectGattToWrite(new MyWriteCallback() {
			@Override
			public void onWriteMajorSuccess(final int oldMajor,
					final int newMajor) {
				ModifyActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								ModifyActivity.this,
								"oldMajor#" + oldMajor + "newMajor为" + newMajor,
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onWriteMinorSuccess(final int oldMionr,
					final int newMinor) {
				ModifyActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								ModifyActivity.this,
								"oldMinor#" + oldMionr + "newMinor为" + newMinor,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (conn != null && !conn.isConnected()) {
			conn = new AprilBeaconConnection(this, beacon);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		beacon = null;
		if (conn != null && conn.isConnected()) {
			conn.close();
		}
	}

	public void notify(View v) {
		beaconManager = new BeaconManager(this);
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startMonitoring(ALL_BEACONS_REGION);
				} catch (RemoteException e) {
				}
			}
		});

		beaconManager.setMonitoringListener(new MonitoringListener() {

			@Override
			public void onExitedRegion(Region arg0) {
				Toast.makeText(getApplicationContext(), "你离开beacon范围", 0)
						.show();
			}

			@Override
			public void onEnteredRegion(Region arg0, List<Beacon> arg1) {
				Toast.makeText(getApplicationContext(), "你进入beacon范围", 0)
						.show();
			}
		});

	}

	@Override
	protected void onStop() {
		try {
			if (beaconManager != null)
				beaconManager.disconnect();
//				beaconManager.stopRanging(ALL_BEACONS_REGION);
//				beaconManager.stopMonitoring(ALL_BEACONS_REGION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onStop();
	}
}
