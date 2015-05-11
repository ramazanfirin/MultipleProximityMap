package in.wptrafficanalyzer.multipleproximitymapv2;

import android.app.Application;
import android.content.res.Configuration;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyApplication extends Application{
	private static MyApplication singleton;
	private GoogleMap map;
	int azumith;
	
	public Marker otobusMarker;
	public Marker currentLocationMarker; 	
	
	public boolean busArrived = false;
	public boolean arrivedToStop = false;
	
	public boolean isArrivedToStop() {
		return arrivedToStop;
	}



	public void setArrivedToStop(boolean arrivedToStop) {
		this.arrivedToStop = arrivedToStop;
	}



	public boolean isBusArrived() {
		return busArrived;
	}



	public void setBusArrived(boolean busArrived) {
		this.busArrived = busArrived;
	}



	public void updateMarker(LatLng point,LatLng current){
		if(otobusMarker!=null)
			otobusMarker.remove();
		
		// Creating an instance of MarkerOptions
				MarkerOptions markerOptions = new MarkerOptions();					
				
				// Setting latitude and longitude for the marker
				markerOptions.position(point);
				
				otobusMarker=map.addMarker(markerOptions);
				
				if(currentLocationMarker!=null)
					currentLocationMarker.remove();
				
				// Creating an instance of MarkerOptions
						MarkerOptions markerOptions2 = new MarkerOptions();					
						
						// Setting latitude and longitude for the marker
						markerOptions2.position(current);
						
						currentLocationMarker=map.addMarker(markerOptions2);			
	}
	
	
	
	public Marker getOtobusMarker() {
		return otobusMarker;
	}

	public void setOtobusMarker(Marker otobusMarker) {
		this.otobusMarker = otobusMarker;
	}

	public int getAzumith() {
		return azumith;
	}

	public void setAzumith(int azumith) {
		this.azumith = azumith;
	}

	public MyApplication getInstance(){
		return singleton;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;

	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public GoogleMap getMap() {
		return map;
	}

	public void setMap(GoogleMap map) {
		this.map = map;
	}
	
	public Location getCurrentLocation(){
		if(map!=null)
			return map.getMyLocation();
		else
		return 	null;
	}
}
