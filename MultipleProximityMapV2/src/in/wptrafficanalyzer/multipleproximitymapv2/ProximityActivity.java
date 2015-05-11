package in.wptrafficanalyzer.multipleproximitymapv2;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.InvalidApiKeyException;
import org.ispeech.error.NoNetworkException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;



public class ProximityActivity extends Activity {
	
	String notificationTitle;
	String notificationContent;
	String tickerMessage;
	
	public static final String TAG = "iSpeech SDK Sample";
	SpeechSynthesis synthesis;
	Context _context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("proje", "100 meter");
		try {

			
			LocationManager   locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			
			boolean proximity_entering = getIntent().getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, true);
			
			double lat = getIntent().getDoubleExtra("lat", 0);
			
			double lng = getIntent().getDoubleExtra("lng", 0);
			
			String name = getIntent().getStringExtra("name");
			
			String distance = getIntent().getStringExtra("distance");
			
			String strLocation = Double.toString(lat)+","+Double.toString(lng);
			
			if(proximity_entering){
				Toast.makeText(getBaseContext(),name+" "+  distance+" metre"  ,Toast.LENGTH_SHORT).show();
				notificationTitle = "Proximity - Entry";
//			notificationContent = "Entered the region:" + strLocation;
//			tickerMessage = "Entered the region:" + strLocation;
				notificationContent = name+" "+  distance+" metre yaklasildi" ;
				tickerMessage = name+" "+  distance+" metre yaklasildi";
				
				//speak(name+" "+  distance+" metre yaklasildi");
				
				
			}else{
				//Toast.makeText(getBaseContext(),name+" "+  distance+" metre uzaklasildi"  ,Toast.LENGTH_LONG).show();
				notificationTitle = "Proximity - Exit";
//			notificationContent = "Exited the region:" + strLocation;
//			tickerMessage = "Exited the region:" + strLocation;
				notificationContent = name+" "+  distance+" metre uzaklasildi";
				tickerMessage = name+" " +distance+" metre uzaklasildi";
			}
			
			
			
			Intent notificationIntent = new Intent(getApplicationContext(),NotificationView.class);
			
			/** Adding content to the notificationIntent, which will be displayed on 
			 * viewing the notification
			 */
			notificationIntent.putExtra("content", notificationContent );
			
			/** This is needed to make this intent different from its previous intents */
			notificationIntent.setData(Uri.parse("tel:/"+ (int)System.currentTimeMillis()));
			
			/** Creating different tasks for each notification. See the flag Intent.FLAG_ACTIVITY_NEW_TASK */
			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			
			/** Getting the System service NotificationManager */
			NotificationManager nManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
			
			/** Configuring notification builder to create a notification */
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
								.setWhen(System.currentTimeMillis())
								.setContentText(notificationContent)
								.setContentTitle(notificationTitle)
								.setSmallIcon(R.drawable.ic_launcher)
								.setAutoCancel(true)
								.setTicker(tickerMessage)
								.setContentIntent(pendingIntent)
								.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			
			
			/** Creating a notification from the notification builder */
			Notification notification = notificationBuilder.build();
			
			/** Sending the notification to system. 
			 * The first argument ensures that each notification is having a unique id 
			 * If two notifications share same notification id, then the last notification replaces the first notification 
			 * */
			
			//gecici iptal
	     	//nManager.notify((int)System.currentTimeMillis(), notification);
			navigate();
			
			/** Finishes the execution of this activity */
			finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
//			builder.setMessage("Error[TTSActivity]: " + e.toString())
//			       .setCancelable(false)
//			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			           }
//			       });
//			AlertDialog alert = builder.create();
//			alert.show();
			Toast.makeText(getBaseContext(),"hata="+e.getMessage() ,Toast.LENGTH_LONG).show();
		}
		
	
		
	}

	public void navigate(){
		
		
	}
	
}
