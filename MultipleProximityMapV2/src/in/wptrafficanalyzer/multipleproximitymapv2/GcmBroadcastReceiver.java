/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.wptrafficanalyzer.multipleproximitymapv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;


/**
 * Handling of GCM messages.
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {
    static final String TAG = "GCMDemo";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;
    
    MainActivity mainActivity=null;
    

    @Override
    public void onReceive(Context context, Intent intent) {
    	try {
    		
			Log.i("mobilEkip", "GCM Mesaj Geldi");
			GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
			ctx = context;
			
			String messageType = gcm.getMessageType(intent);
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			    sendNotification("Send error: " + intent.getExtras().toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			    sendNotification("Deleted messages on server: " + intent.getExtras().toString());
			} else {
			   
			sendNotification("mesaj geldi=");
//			Uri uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		            if (uri != null) {
//		              Ringtone rt=RingtoneManager.getRingtone(context,uri);
//		              if (rt != null) {
//		                rt.setStreamType(AudioManager.STREAM_NOTIFICATION);
//		                rt.play();
//		              }
//		            }
			
			String message =(String)intent.getExtras().get("programName");
			if("yolTariff".equals(message)){
			
				String stopName =(String)intent.getExtras().get("stopName");
				String distance = (String)intent.getExtras().get("distance");
				String lat = (String)intent.getExtras().get("lat");
				String lng = (String)intent.getExtras().get("lng");
				String speed = (String)intent.getExtras().get("speed");
				String bearing = (String)intent.getExtras().get("bearing");
				String toastMessage  = getMessage(distance, speed, stopName, lat, lng,bearing);
				
				
				MyApplication mApplication = (MyApplication)ctx.getApplicationContext();
				//if(mApplication.isArrivedToStop()){
					Toast.makeText(ctx, toastMessage, Toast.LENGTH_LONG).show();
				//}
				
				Location currentLocation = mApplication.getCurrentLocation();
				LatLng currentLat = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
				//mApplication.updateCurrentLocaitonMarker(point);
				
				LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
				mApplication.updateMarker(point,currentLat);
				
				
			}else if ("gcmTest".equals(message)){
				sendNotification("url geldi");
				
				//Intent newIntent = new Intent(Intent.ACTION_VIEW);
				
				String url =(String)intent.getExtras().get("message");//address
				generateNotification(ctx,url);
				
				
				
//				Intent intentNew = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//				intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			    ctx.startActivity(intentNew);
			 
				Intent startIntent = new Intent(context, NotificationView.class);
				startIntent.putExtra("url", url);
			    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        context.startActivity(startIntent);
				
			}
			
//			Intent mIntent = new Intent(context, MainActivity.class);
//			mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			Bundle bundle = new Bundle();
//			mIntent.putExtra("stopName", stopName);
//			mIntent.putExtra("distance", distance);
//			mIntent.putExtra("lat", lat);
//			mIntent.putExtra("lng", lng);
//			mIntent.putExtra("speed", speed);
//			context.startActivity(mIntent);
			
			
			
			setResultCode(Activity.RESULT_OK);
   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context.getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();	
		}
    }
    
    public String getMessage(String distance,String Speed,String stopName,String lat,String lng,String azimuthValue){
		String voice="";
		
		int distanceValue = new Float(distance).intValue();
		int speedValue = new Float(Speed).intValue();
		int donusAcisi=0;
		String direction="";
		
		if(distanceValue<30 && speedValue<1){
			//LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);  
			
			MyApplication mApplication = (MyApplication)ctx.getApplicationContext();
			Location currentLocation = mApplication.getCurrentLocation();
			//Location currentLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(currentLocation ==null){
				voice = "son konum bulunamadı";
			}else{
				mApplication.setBusArrived(true);
				Location busLocation = new Location("");//provider name is unecessary
				busLocation.setLatitude(Double.parseDouble(lat));//your coords of course
				busLocation.setLongitude(Double.parseDouble(lng));
				
				float dsitance  = currentLocation.distanceTo(busLocation);
				float bearing = currentLocation.bearingTo(busLocation);
				int bearingValue = new Float(bearing).intValue(); 
				
			    int azumith =   mApplication.getAzumith();
			    azumith  = new Float(azimuthValue).intValue(); 
				if(bearingValue<0)
					bearingValue = bearingValue+360;
				
				if(azumith<bearingValue){
					donusAcisi = bearingValue-azumith;
				    direction = "sag";
					if(bearingValue-azumith>180){
						donusAcisi = 360-donusAcisi;
						direction = "sol";
					}
				}else {
					donusAcisi = azumith-bearingValue;
				    direction = "sol";
					if(azumith-bearingValue>180){
						donusAcisi = 360-donusAcisi;
						direction = "sag";
					}
				}
				
				String watchValue="";
				if("sag".equals(direction)){
					if(donusAcisi<15)
						watchValue = "12";
					if(donusAcisi>15 && donusAcisi<60)
						watchValue = "1";
					if(donusAcisi>60)
						watchValue = "2";
				}
				
				if("sol".equals(direction)){
					if(donusAcisi<15)
						watchValue = "12";
					if(donusAcisi>15 && donusAcisi<60)
						watchValue = "11";
					if(donusAcisi>60)
						watchValue = "10";
				}
				
				
			    //voice = "Otobus geldi " + new Float(dsitance).intValue()+" metre mesafede. Açi değeri "+ new Float(bearing).intValue() + " azumith = "+ azumith + "donus:"+direction+" "+donusAcisi;
			    voice = "Otobus geldi. " +watchValue+" yonunde"+ new Float(dsitance).intValue()+" metre";
			}
		}else{
			voice = "otobus"+" "+distanceValue+" metre";
		}
		
		
		return voice;
	}
    
    
    
    
    
    
    // Put the GCM message into a notification and post it.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,new Intent(ctx, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
        //.setSmallIcon(R.drawable.ic_stat_gcm)
        .setContentTitle("GCM Notification")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    /**
     * Create a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
    
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        Notification notification = new Notification(icon, message, when);
        
        String title = "Reklam Deneme";
        
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
      //  notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse(
//                               "android.resource://" 
//                               + context.getPackageName() 
//                               + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      

    }
    
    public void alertDialog(Context context){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
 
			// set title
			alertDialogBuilder.setTitle("Your Title");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Click yes to exit!")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, close
//						// current activity
//						MainActivity.this.finish();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			}
		
   
}
