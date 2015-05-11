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



public class ProximityActivity10meter extends ProximityActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("proje", "10 meter");
	}	

	public void navigate(){
		
		Util.clearAlerts(getApplicationContext());
		
		//Toast.makeText(getBaseContext(),"Geldi" ,Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);
   	    startActivity(intent);	
	}
}
