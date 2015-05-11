package in.wptrafficanalyzer.multipleproximitymapv2;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class Util {

	public static void clearAlerts(Context context){
    	try {
    		
    		LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
			Intent proximityIntent = new Intent("in.wptrafficanalyzer.activity.proximity");
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, proximityIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent);
			
			Intent proximityIntent10 = new Intent("in.wptrafficanalyzer.activity.proximity10meter");
			PendingIntent pendingIntent10 = PendingIntent.getActivity(context, 0, proximityIntent10,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent10);
			
			Intent proximityIntent30 = new Intent("in.wptrafficanalyzer.activity.proximity30meter");
			PendingIntent pendingIntent30 = PendingIntent.getActivity(context, 0, proximityIntent30,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent30);
			
			Intent proximityIntent50 = new Intent("in.wptrafficanalyzer.activity.proximity50meter");
			PendingIntent pendingIntent50 = PendingIntent.getActivity(context, 0, proximityIntent50,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent50);
			
			Intent proximityIntent5 = new Intent("in.wptrafficanalyzer.activity.proximity5meter");
			PendingIntent pendingIntent5 = PendingIntent.getActivity(context, 0, proximityIntent5,Intent.FLAG_ACTIVITY_NEW_TASK);
			locationManager.removeProximityAlert(pendingIntent5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
}
