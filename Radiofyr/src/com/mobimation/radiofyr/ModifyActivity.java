package com.mobimation.radiofyr;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import com.aprilbrother.aprilbrothersdk.BeaconManager.ErrorListener;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.BeaconManager.ServiceReadyCallback;
import com.aprilbrother.aprilbrothersdk.Region;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconCharacteristics;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconCharacteristics.MyReadCallBack;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconConnection;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconConnection.MyWriteCallback;
import com.aprilbrother.aprilbrothersdk.connection.AprilBeaconConnection.WriteCallback;
/**
 * Activity to modify beacon parameters.
 * 
 * @author gunnarforsgren
 *
 */
public class ModifyActivity extends Activity {

	protected static final String TAG = "ModifyActivity";
	private ModifyActivity act;
	private Context context;
	private EditText uuid;
	private EditText major;
	private EditText minor;
	private Beacon beacon;

	private AprilBeaconConnection conn;
	private TextView battery;
	private TextView txpower;
	private TextView advinterval;
	
	private String oldPassword;

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
		context=this.getApplicationContext();
		// Wire up buttons to handlers
		findViewById(R.id.buttonWriteUUID) .setOnClickListener(writeUUID);
		findViewById(R.id.buttonWriteMajor).setOnClickListener(writeMajor);
		findViewById(R.id.buttonWriteMinor).setOnClickListener(writeMinor);
		findViewById(R.id.buttonWriteTXPower).setOnClickListener(writeTXPower);
		findViewById(R.id.buttonWriteAdvInterval).setOnClickListener(writeAdvInterval);
		findViewById(R.id.buttonFgWriteScanPeriod).setOnClickListener(writeFgScanWait);
		findViewById(R.id.buttonBgWriteScanPeriod).setOnClickListener(writeBgScanWait);
		findViewById(R.id.buttonWritePassword).setOnClickListener(writePassword);
		act=this;
		init();
	}
	/**
	 * Handle write UUID
	 */
    private View.OnClickListener writeUUID = 
    	  new View.OnClickListener() {
    		@Override
        public void onClick(View v) {
    			
        	  EditText uuid1=(EditText)findViewById(R.id.uuidhi);
          EditText uuid2=(EditText)findViewById(R.id.uuidlo);
          String UUID=
        		  uuid1.getText().toString()+"-"+uuid2.getText().toString();
          if (UUID.contains("-")) {
           // UUID string expanded into ********-****-****-****-************ format
           String formattedUUID=
        		  UUID.substring(0, 7)+ "-"+
        		  UUID.substring(8,11)+ "-"+
        		  UUID.substring(12,15)+"-"+
        		  UUID.substring(16,19)+"-"+
        		  UUID.substring(20,31);
        		
            Log.d("ModifyActivity", "writeUUID="+formattedUUID);
//          showEnterDialog();  // Prompt for current password
            if (conn==null)
        	      Log.d("ModifyActivity", "*****WARNING: AprilBeaconConnection NULL value *****");
            else {  
        	      conn = new AprilBeaconConnection(act, beacon);
              conn.writeUUID(formattedUUID); 
        	      writeToBeacon(conn);
            }
          }
          else
        	    alert("UUID string shall be a string of 32 hex characters");
        }
      };
      
  	/**
  	 * Handle write Major number
  	 */
      private View.OnClickListener writeMajor = 
      	  new View.OnClickListener() {
      		@Override
          public void onClick(View v) {
          	  EditText major=(EditText)findViewById(R.id.major);
            int majorNo=Integer.valueOf(major.getText().toString());
            Log.d("ModifyActivity", "writeMajor="+majorNo);
            conn = new AprilBeaconConnection(act, beacon);
            
            AprilBeaconConnection.MyWriteCallback mwc=new MyWriteCallback() {
            	 @Override
            	 public void onErrorOfConnection() {
            		 Log.d("ModifyActivity", "Yikes. error of connection !!");
            	 }
            	 public void onWriteMajorSuccess(int oldMajor, int newMajor) {
            		 Log.d("ModifyActivity", "Major written ok, "+oldMajor+" replaced with "+newMajor);
            	 }
            };
            conn.writeMajor(majorNo); 
            conn.connectGattToWrite(mwc, "AprilBrother");
 //         	writeToBeacon(conn);	    
          }
        };

    	/**
     * Handle write Minor number
     */
     private View.OnClickListener writeMinor = 
       new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         EditText minor=(EditText)findViewById(R.id.minor);
         int minorNo=Integer.valueOf(minor.getText().toString());
         Log.d("ModifyActivity", "writeMinor="+minorNo);
         conn.close();
         conn = new AprilBeaconConnection(act, beacon);
         conn.writeMinor(minorNo); 
         writeToBeacon(conn);	    
       }
     };
      
    /**
     * Handle write TX Power
     */
    private View.OnClickListener writeTXPower = 
      new View.OnClickListener() {
    	  @Override
        public void onClick(final View v) {
    		  EditText txPower=(EditText)findViewById(R.id.txpower);
    		  int txPowerNo=Integer.valueOf(txPower.getText().toString());
    		  Log.d("ModifyActivity","writeTxPower()");
    		  conn.writeTxPower(txPowerNo);
    		  writeToBeacon(conn);
        }
      };
      
    /**
     * Handle write Advertising Interval
     */
    private View.OnClickListener writeAdvInterval = 
     new View.OnClickListener() {
    	  @Override
        public void onClick(final View v) {
    		  EditText advInterval=(EditText)findViewById(R.id.advinterval);
    		  int advIntervalNo=Integer.valueOf(advInterval.getText().toString());
    		  Log.d("ModifyActivity","writeAdvInterval()");
    		  conn.writeAdvertisingInterval(advIntervalNo);
    		  writeToBeacon(conn);
        }
     };
     
    /**
     * Handle write Scan Periods
     * The scan period is the duration for a beacon scan 
     */ 
    private View.OnClickListener writeFgScanWait =
    	 new View.OnClickListener() {
    	  @Override
        public void onClick(final View v) {
    		  EditText fgScanPeriod=(EditText)findViewById(R.id.fgScanPeriod);
    		  EditText fgScanWait=(EditText)findViewById(R.id.fgWaitPeriod);
    		  
    		  long fgScanPeriodMs=Long.valueOf(fgScanPeriod.getText().toString());
    		  long fgScanWaitMs=Long.valueOf(fgScanWait.getText().toString());
    		  Log.d("ModifyActivity","Foreground Scan/Wait (ms)= "+fgScanPeriodMs+"/"+fgScanWaitMs);
    		  beaconManager.setForegroundScanPeriod(fgScanPeriodMs,fgScanWaitMs);
    		  // TODO close connection and reconnect with new values in effect
        }
     };
     
     /**
      * Handle write Wait Periods
      * The wait period is the pause time between beacon scans
      */ 
     private View.OnClickListener writeBgScanWait =
    		 new View.OnClickListener() {
     	  @Override
         public void onClick(final View v) {
     		  EditText bgScanPeriod=(EditText)findViewById(R.id.bgScanPeriod);
     		  EditText bgScanWait=(EditText)findViewById(R.id.bgWaitPeriod);
     		  long bgScanPeriodMs=Long.valueOf(bgScanPeriod.getText().toString());
     		  long bgScanWaitMs=Long.valueOf(bgScanWait.getText().toString());
     		  Log.d("ModifyActivity","Background Scan/Wait (ms)= "+bgScanPeriodMs+"/"+bgScanWaitMs);
     		  beaconManager.setBackgroundScanPeriod(bgScanPeriodMs,bgScanWaitMs);
    		  // TODO close connection and reconnect with new values in effect
         }
      };
      
      /**
       * Handle change password
       * The password used for authorizing setting changes
       */ 
      private View.OnClickListener writePassword =
     		 new View.OnClickListener() {
      	  @Override
          public void onClick(final View v) {
      		EditText password=(EditText)findViewById(R.id.password);
      		oldPassword=password.getText().toString();
      		Log.d("ModifyActivity", "New password set:"+oldPassword);
          }
       };
 
     /**
      * Write outstanding parameters to the beacon
      * and handle callbacks.
      * @param c Connection reference to already connected beacon
      */
private void writeToBeacon(AprilBeaconConnection c) {
	EditText pw=(EditText)findViewById(R.id.password);
	oldPassword=pw.getText().toString();
	c.connectGattToWrite(
	  		   new MyWriteCallback() {
	  			@Override
				public void onWriteMajorSuccess(final int oldMajor, final int newMajor) {
					Log.d("ModifyActivity",
									"Wrote Major value, oldMajor=" + oldMajor + "newMajor=" + newMajor);
			    }
	  			
	  			@Override
				public void onWriteMinorSuccess(final int oldMinor, final int newMinor) {
							Log.d("ModifyActivity",
									"Wrote Minor value, oldMinor=" + oldMinor + "newMinor=" + newMinor);
			    }
				
	  			@Override
				public void onWriteNewPasswordSuccess(final String oldPassword, final String newPassword) {
	  				// super.onWriteNewPasswordSuccess(oldPassword, newPassword);
					Log.d("ModifyActivity", "Write Password Success");
			    }
				
	  			@Override
				public void onErrorOfPassword() {
					Log.d("ModifyActivity", "Password Error");
			    }
	  			
				@Override
				public void onErrorOfConnection() {
					Log.d("ModifyActivity", "Connection Error");
			    }
				
				@Override
				public void onErrorOfDiscoveredServices() {
					Log.d("ModifyActivity", "Error requesting Discovered Services");
			    }
	  			
				@Override
				public void onWriteUUIDSuccess() {
					Log.d("ModifyActivity", "Success writing UUID");
			    }
	  			
				@Override
				public void onWriteAdvertisingIntervalSuccess() {
					Log.d("ModifyActivity", "Success writing Advertising Interval");
			    } 			
/*				
				@Override
				public void onWriteTxPowerSuccess() {
				    ModifyActivity.this.runOnUiThread(
				      new Runnable() {
						@Override
						public void run() {
							Log.d("ModifyActivity", "Success writing TX Power");
						}
					  }
				    );
			    }
*/				
				@Override
				public void onWriteTxPowerSuccess() {
					Log.d("ModifyActivity", "Success writing TX Power");
			    }

				@Override
				public void onWritePasswordSuccess() {
					Log.d("ModifyActivity", "Success writing TX Power");
				} 			
				
	  		   },oldPassword 
	  		  );  
}
     
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
	/**
	 * Prompt for current password
	 */
	private EditText et_pwd;
	private Button bt_ok;
	private Button bt_cancel;
	private AlertDialog dialog;
	private void showEnterDialog() {

		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_enter_password, null);
		et_pwd = (EditText) view.findViewById(R.id.et_pwd);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				oldPassword = et_pwd.getText().toString().trim();
				Log.d("ModifyActivity", "Set password="+oldPassword);
				aprilWrite();
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
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
		}, oldPassword);
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
			public void onExitedRegion(Region r) {
				Toast.makeText(getApplicationContext(), 
				"Exited " +r.getIdentifier()+"(Region "+r.getMajor()+":"+r.getMinor()+")",0)
						.show();
			}

			@Override
			public void onEnteredRegion(Region r, List<Beacon> beacons) {
				Toast.makeText(getApplicationContext(),
				"Entered " +r.getIdentifier()+"(Region "+r.getMajor()+":"+r.getMinor()+") "+beacons.size()+" beacons",0)
						.show();
			}
		});
		beaconManager.setErrorListener(new ErrorListener() {
			@Override
			public void onError(java.lang.Integer paramInteger) {
				Log.d("ModifyActivity", "AprilBrother BeaconManager - onError() says:"+paramInteger);
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
	private void alert(String msg) {
		// Pop up a dialog with a message
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		// set the title of the Alert Dialog
		alertDialogBuilder.setTitle("Message");		
		// set dialog message		
		alertDialogBuilder
		.setMessage(msg)
	    .setCancelable(true)
		.setPositiveButton("Ok",	
		  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
			  dialog.cancel();			
			}
		  }
		);
		AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
}
