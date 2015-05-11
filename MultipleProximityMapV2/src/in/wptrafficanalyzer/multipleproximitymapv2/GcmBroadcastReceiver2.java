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

import java.util.Locale;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;


/**
 * Handling of GCM messages.
 */
public class GcmBroadcastReceiver2 extends BroadcastReceiver {
    static final String TAG = "GCMDemo";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;
    
    
    

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
			String stopName =(String)intent.getExtras().get("stopName");
			String distance = (String)intent.getExtras().get("distance");
			String lat = (String)intent.getExtras().get("lat");
			String lng = (String)intent.getExtras().get("lng");
			String speed = (String)intent.getExtras().get("speed");
			String toastMessage  = getMessage(distance, speed, stopName, lat, lng);
			Toast.makeText(ctx, toastMessage, Toast.LENGTH_SHORT).show();
		
//			Intent mIntent = new Intent(context, SpeakActivity.class);
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
    
    public String getMessage(String distance,String Speed,String stopName,String lat,String lng){
		String voice="";
		
		int distanceValue = new Float(distance).intValue();
		int speedValue = new Float(Speed).intValue();
		
		if(distanceValue<30 && speedValue<5){
			LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);  
			Location currentLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(currentLocation ==null){
				voice = "son konum bulunamadı";
			}else{
				Location busLocation = new Location("");//provider name is unecessary
				busLocation.setLatitude(Double.parseDouble(lat));//your coords of course
				busLocation.setLongitude(Double.parseDouble(lng));
				
				float dsitance  = currentLocation.distanceTo(busLocation);
				float bearing = currentLocation.bearingTo(busLocation);
				voice = "Araç geldi " + new Float(dsitance).intValue()+" metre mesafede. Açi değeri "+ new Float(bearing).intValue();
			}
		}else{
			voice = stopName+" "+distanceValue+" metre";
		}
		
		
		return voice;
	}
    
    
    
    
    
    
    // Put the GCM message into a notification and post it.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, MainActivity.class), 0);

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
}
