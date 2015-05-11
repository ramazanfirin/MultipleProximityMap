package in.wptrafficanalyzer.multipleproximitymapv2;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {
	
	GoogleMap googleMap;
    LocationManager locationManager;
    PendingIntent pendingIntent;
    SharedPreferences sharedPreferences;
    int locationCount = 0;

    IntentFilter filter = new IntentFilter();
    
	ToneGenerator toneG= new ToneGenerator(AudioManager.STREAM_MUSIC, 100); ;
    
    BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private Handler mHandler;
	// Stops scanning after 10 seconds.
		private static final long SCAN_PERIOD = 1000000;
    
    private SensorManager sensorManager;
	 private Sensor sensor;
	 Marker otobusMarker;
	 
	//Handler mHandler;
	 
		double distance;
	 
	 Location targetLocation; 
	 
	 int count = 0;
	  Timer timer = new Timer();
	  
	  public void refresh(){
			int REQUEST_ENABLE_BT  =1234;
			final BluetoothManager bluetoothManager =(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			mBluetoothAdapter = bluetoothManager.getAdapter();
			
			// Ensures Bluetooth is available on the device and it is enabled. If not,
			// displays a dialog requesting user permission to enable Bluetooth.
			if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	  public void scan(View v){
			refresh();
			//bleList = new ArrayList<BLEConnectionPreference>();
			//deviceList = new ArrayList<HashMap<String,String>>();
			//mBluetoothAdapter.startLeScan(mLeScanCallback);
			
			scanLeDevice(true);
//			Handler mHandler = new Handler();
//			mHandler.post(run);
			
			 new Thread(new Task()).start();
	
		}
	  
	    private void scanLeDevice(final boolean enable) {
	    	final BluetoothManager bluetoothManager =(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
	    	mBluetoothAdapter = bluetoothManager.getAdapter();
	    	mHandler = new Handler();
	    	if (enable) {
	            // Stops scanning after a pre-defined scan period.
	            mHandler.postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                    mScanning = false;
	                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
	                    distance=0;
	                }
	            }, SCAN_PERIOD);

	            mScanning = true;
	            mBluetoothAdapter.startLeScan(mLeScanCallback);
	        } else {
	            mScanning = false;
	            mBluetoothAdapter.stopLeScan(mLeScanCallback);
	        }
	       
	    }
	    
	    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
	        @Override
	        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
	        		BLEConnectionPreference bleConnectionPreference = new BLEConnectionPreference(device, rssi, scanRecord);
	            	if (bleConnectionPreference.getMajor()==0){
	            		//back = bleConnectionPreference;
	            		//double estimateDistance  =kalmanFilterUtil.calculateFromBack(bleConnectionPreference.getDistance());
	            		//bleConnectionPreference.setEstimateDistance(estimateDistance);
	            	}else{
	            		//front  =  bleConnectionPreference;
	             		//double estimateDistance  =kalmanFilterUtil.calculateFromFront(bleConnectionPreference.getDistance());
	            		//bleConnectionPreference.setEstimateDistance(estimateDistance);
	            		distance = bleConnectionPreference.getDistance();
	            	}
	            	
	            	
	            	//addDeviceFOrUI(bleConnectionPreference);
	        		runOnUiThread(new Runnable() {
	    				@Override
	    				public void run() {
	    					//listAdapter.notifyDataSetChanged();
	    					//decision.setText(calculatePosition());
	    				}
	    			});
	      }
	    };
	    
    public void clearMap(){
    	clearAlerts();
    	Intent proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity");
		
		pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Removing the proximity alert					
		locationManager.removeProximityAlert(pendingIntent);
		
		// Removing the marker and circle from the Google Map
		googleMap.clear();
		
		// Opening the editor object to delete data from sharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        
        // Clearing the editor
        editor.clear();
		
        // Committing the changes
		editor.commit();
		
		Toast.makeText(getBaseContext(), "Proximity Alert is removed", Toast.LENGTH_LONG).show();
    }
    
    public void clearAlerts(){
    	try {
    		
    		 locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			Intent proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity");
			PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent);
			
			Intent proximityIntent10 = new Intent("in.wptrafficanalyzer.activity.proximity10meter");
			PendingIntent pendingIntent10 = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent10,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent10);
			
			Intent proximityIntent30 = new Intent("in.wptrafficanalyzer.activity.proximity30meter");
			PendingIntent pendingIntent30 = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent30,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent30);
			
			Intent proximityIntent50 = new Intent("in.wptrafficanalyzer.activity.proximity50meter");
			PendingIntent pendingIntent50 = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent50,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent50);
			
			Intent proximityIntent5 = new Intent("in.wptrafficanalyzer.activity.proximity5meter");
			PendingIntent pendingIntent5 = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent5,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
    @Override
    protected void onResume() {
      super.onResume();
     // Log.d(TAG, "MainActivity: onResume()");
    }
    
   /*
    private BroadcastReceiver receiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	Toast.makeText(context, "Geldi", Toast.LENGTH_SHORT).show();
	    	registerReceiverIntent(this);
	    }

	};
    
	public void registerReceiverIntent(BroadcastReceiver broadcastReceiver){
		filter.addAction("com.google.android.c2dm.intent.RECEIVE");
	    filter.addCategory("in.wptrafficanalyzer.multipleproximitymapv2");
		
		registerReceiver(broadcastReceiver, filter, "com.google.android.c2dm.permission.SEND", null);
	}
	
	*/
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"16. onDestroy()", Toast.LENGTH_SHORT).show();
        mHandler.removeCallbacks(mRunnable);
    }
	
    @Override
    protected void onPause() {
        super.onPause();
      }
    
	private SensorEventListener mySensorEventListener = new SensorEventListener() {

	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }

	    @Override
	    public void onSensorChanged(SensorEvent event) {
	      // angle between the magnetic north direction
	      // 0=North, 90=East, 180=South, 270=West
	      float azimuth = event.values[0];
	      int azimuthValue = new Float(azimuth).intValue();
	      
	      MyApplication mApplication = (MyApplication)getApplicationContext();
	      mApplication.setAzumith(azimuthValue);
	      
	      
	      //azumit.setText(String.valueOf(azimuth));
	    }
	  };
	  


	  
	  
	  
	  private Runnable mRunnable = new Runnable() {

		    @Override
		    public void run() {
		      Log.e("Handlers", "Calls");
		      /** Do something **/
		      checkLocation();
		      
		  }  
	  }; 
	  
	  public void checkLocation(){
		   if(targetLocation==null)
		    	  return;
		   
		   //Toast.makeText(getBaseContext()," metre ,count" +count ,Toast.LENGTH_SHORT).show();
		  // mHandler.postDelayed(mRunnable, 10000);
		      
		      count++;
		      Location location = googleMap.getMyLocation();
		      if(location!=null){
		    	   float distance  = location.distanceTo(targetLocation);
		    	 
		    	   int distanceValue = new Float(distance).intValue();
		    	  
		    	   Toast.makeText(getBaseContext(),distanceValue+" metre mesafedesiniz" ,Toast.LENGTH_SHORT).show();
		    	  
		    	   if(distanceValue<10){
		    		   mHandler.removeCallbacks(mRunnable);
		    		   Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);
		    	   	    startActivity(intent);	
		    	   }else
		    		   mHandler.postDelayed(mRunnable, 10000);
		      }else
		         mHandler.postDelayed(mRunnable, 10000);
		  
		
	   }
  
		  
		  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mHandler = new Handler();
		
		
		
//		
//		registerReceiverIntent(receiver);
//		
		
		final Button button = (Button) findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	 Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);
            	 startActivity(intent);	
            }
        });
        
        final Button clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	clearMap();
            }
        });
        
        final Button startWalking = (Button) findViewById(R.id.startWalking);
        startWalking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	MyApplication mApplication = (MyApplication)getApplicationContext();
            	if(mApplication.isBusArrived()==true){
            		mScanning  =true;
            		refresh();
            		new Thread(new Task()).start();
            		scanLeDevice(true);
            	}else{
            		mScanning  =true;
            		refresh();
            		new Thread(new Task()).start();
            		scanLeDevice(true);
            		Toast.makeText(getApplicationContext(), "Otobus Henuz ulasmadi", Toast.LENGTH_LONG).show();
            	}	
            }
        });
       
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    if (sensor != null) {
	    	sensorManager.registerListener(mySensorEventListener, sensor,
	            SensorManager.SENSOR_DELAY_NORMAL);
	        Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");
	      }
        
        new GCMRegisterTask(getApplicationContext(),"160087228600").execute("");
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available        	

        	clearAlerts();
        	
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();
            
            MyApplication mApplication = (MyApplication)getApplicationContext();
            mApplication.setMap(googleMap);

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);       
            
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            
            //googleMap.
            
            // Getting LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            
            
            // Opening the sharedPreferences object
    		sharedPreferences = getSharedPreferences("location", 0);
    		
    		// Getting number of locations already stored
            locationCount = sharedPreferences.getInt("locationCount", 0);    		
    		
    		// Getting stored zoom level if exists else return 0
    		String zoom = sharedPreferences.getString("zoom", "0");    		
    		
    		// If locations are already saved
            if(locationCount!=0){

                    String lat = "";
                    String lng = "";

                    // Iterating through all the locations stored
                    for(int i=0;i<locationCount;i++){

                            // Getting the latitude of the i-th location
                            lat = sharedPreferences.getString("lat"+i,"0");

                            // Getting the longitude of the i-th location
                            lng = sharedPreferences.getString("lng"+i,"0");

                            // Drawing marker on the map
                            drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                            
                            // Drawing circle on the map
        					drawCircle(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)),"50");
                    }

                    // Moving CameraPosition to last clicked position
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));

                    // Setting the zoom level in the map on last position  is clicked
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(Float.parseFloat(zoom)));                                                   
            }   		
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble("41.021304"), Double.parseDouble("29.227850"))));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble("41.026879"), Double.parseDouble("29.122960"))));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15)); 
            
           // 41.020475, 29.228670 eve yakin
            // 41.021304 , 29.227850 kose 
            String lat = "41.020475";
            String lng = "29.228670";
            
            LatLng ev= new LatLng(Double.parseDouble("41.021304"), Double.parseDouble("29.227850"));
//            addProximityPoint(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), "ev", "100");
//            addProximityPoint(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), "ev", "50");
//            addProximityPoint(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), "ev", "30");
//            addProximityPoint(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), "ev", "10");
//            addProximityPoint(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), "ev", "5");
//            
            //locationManager.//
         /*
            locationManager.addTestProvider(LocationManager.NETWORK_PROVIDER, false, false, false, false, false, 
            	      true, true, 0, 5);
            locationManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, true);
            
            	    Location mockLocation = new Location(LocationManager.NETWORK_PROVIDER);
            	    mockLocation.setLatitude(Double.parseDouble("41.021304"));
            	    mockLocation.setLongitude(Double.parseDouble("29.227850"));
            	    mockLocation.setAltitude(0); 
            	    mockLocation.setTime(System.currentTimeMillis()); 
            	    mockLocation.setAccuracy(0);
            	    mockLocation.setBearing(0);
            	    mockLocation.setSpeed(0);
            	    mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            	    locationManager.setTestProviderLocation(LocationManager.NETWORK_PROVIDER, mockLocation);
            
            */
            googleMap.setOnMapClickListener(new OnMapClickListener() {
				
				@Override
				public void onMapClick(LatLng point) {				 
//					addProximityPoint(point,"manuel","100");
//					addProximityPoint(point,"manuel","50");
//					addProximityPoint(point,"manuel","30");
//					addProximityPoint(point,"manuel","10");
//					addProximityPoint(point,"manuel","5");
//			     
					
					
 					
 					
 					
				}
            }); 
           
            googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {				
				@Override
				public void onMapLongClick(LatLng point) {
					targetLocation = null;
					mHandler.removeCallbacks(mRunnable);
					Toast.makeText(getBaseContext(), "Proximity Alert removed", Toast.LENGTH_SHORT).show();
					timer.cancel();
				}
			});           
            
            
            new GetStationCoordinationTask(getApplicationContext(), this).execute("");;
		}		
	}
	
	public void addTargetLocation(double latitude,double longitude){
		targetLocation = new Location("");
		targetLocation.setLatitude(latitude);
		targetLocation.setLongitude(longitude);
		mHandler.postDelayed(mRunnable, 1000);
		//Toast.makeText(getBaseContext(), "Proximity Alert is added", Toast.LENGTH_SHORT).show();
		 drawMarker(new LatLng(latitude, longitude));
         
         // Drawing circle on the map
			drawCircle(new LatLng(latitude,longitude),"50");
	
			 googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
	            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble("41.026879"), Double.parseDouble("29.122960"))));
	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15)); 
			
	}
	
	private void addProximityPoint(LatLng point,String name,String distance){
		// Incrementing location count
		locationCount++;
							
		// Drawing marker on the map
		drawMarker(point);
		
		// Drawing circle on the map
		drawCircle(point,distance);					
		
        // This intent will call the activity ProximityActivity
        Intent proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity");
        
        if(distance.equals("100"))
        	proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity");
        if(distance.equals("50"))
        	proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity50meter");
        if(distance.equals("30"))
        	proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity30meter");
        if(distance.equals("10"))
        	proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity10meter");
        if(distance.equals("5"))
        	proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity5meter");
        
        
        // Passing latitude to the PendingActivity
        proximityIntent.putExtra("lat",point.latitude);
        
        
        // Passing longitude to the PendingActivity
        proximityIntent.putExtra("lng", point.longitude);
        
        proximityIntent.putExtra("name", name);
        
        proximityIntent.putExtra("distance", distance);
        
        
		
        // Creating a pending intent which will be invoked by LocationManager when the specified region is
        // entered or exited
        pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
        
        
        // Setting proximity alert 
        // The pending intent will be invoked when the device enters or exits the region 20 meters
        // away from the marked point
        // The -1 indicates that, the monitor will not be expired
        locationManager.addProximityAlert(point.latitude, point.longitude, Float.parseFloat(distance), -1, pendingIntent);	
      
        /** Opening the editor object to write data to sharedPreferences */
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Storing the latitude for the i-th location
        editor.putString("lat"+ Integer.toString((locationCount-1)), Double.toString(point.latitude));

        // Storing the longitude for the i-th location
        editor.putString("lng"+ Integer.toString((locationCount-1)), Double.toString(point.longitude));

        // Storing the count of locations or marker count
        editor.putInt("locationCount", locationCount);
        
        /** Storing the zoom level to the shared preferences */
        editor.putString("zoom", Float.toString(googleMap.getCameraPosition().zoom));		        

        /** Saving the values stored in the shared preferences */
        editor.commit();		        
        
        //Toast.makeText(getBaseContext(), "Proximity Alert is added", Toast.LENGTH_SHORT).show();	
        
        googleMap.getMyLocation();
	}
	
	
	private void drawCircle(LatLng point,String radius){
		
		// Instantiating CircleOptions to draw a circle around the marker
		CircleOptions circleOptions = new CircleOptions();
		
		// Specifying the center of the circle
		circleOptions.center(point);
		
		// Radius of the circle
		circleOptions.radius(Double.valueOf(radius));
		
		// Border color of the circle
		circleOptions.strokeColor(Color.BLACK);
		
		// Fill color of the circle
		circleOptions.fillColor(0x30ff0000);
		
		// Border width of the circle
		circleOptions.strokeWidth(2);
		
		// Adding the circle to the GoogleMap
		googleMap.addCircle(circleOptions);
		
	}	
	
	private void drawMarker(LatLng point){
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();					
		
		// Setting latitude and longitude for the marker
		markerOptions.position(point);
		
		// Adding InfoWindow title
		markerOptions.title("Location Coordinates");		
		
		// Adding InfoWindow contents
		markerOptions.snippet(Double.toString(point.latitude) + "," + Double.toString(point.longitude));		
		
		// Adding marker on the Google Map
		googleMap.addMarker(markerOptions);
		//googleMap.
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	class Task implements Runnable {
		@Override
		public void run() {
			
			
			while(mScanning){
				if(distance !=0){
					
					
				double duration = (distance/30)*1000;
				toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200); //200 is duration in ms
				try {
					Thread.sleep(new Double(duration).intValue());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				toneG.stopTone();
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			}
			
			
		}

	}

}
