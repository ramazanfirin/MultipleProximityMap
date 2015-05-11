package in.wptrafficanalyzer.multipleproximitymapv2;

import java.util.Locale;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.InvalidApiKeyException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class SpeakActivity extends Activity {
	
	String TAG="speak";
    SpeechSynthesis synthesis;
    TextToSpeech ttobj;
    String voice="";
    String stopName;
    String distance;
    String lat;
    String lng;
    String Speed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speak);
		
		try {
			Bundle extras = getIntent().getExtras();
			
			 
			if (extras != null) {
			   stopName= extras.getString("stopName");
			   distance= extras.getString("distance");
			   lat= extras.getString("lat");
			   lng= extras.getString("lng");
			   Speed= extras.getString("speed");

	    			ttobj=new TextToSpeech(getApplicationContext(), 
	    			      new TextToSpeech.OnInitListener() {
	    			      @Override
	    			      public void onInit(int status) {
	    			         if(status != TextToSpeech.ERROR){
	    			             ttobj.setLanguage(Locale.UK);
	    			             String message  = getMessage(distance,Speed,stopName,lat,lng);
	    			             ttobj.speak(message, TextToSpeech.QUEUE_FLUSH, null); 
	    			            }				
	    			         }
	    			      });


			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();	
		}

	}	
	
	public String getMessage(String distance,String Speed,String stopName,String lat,String lng){
		String voice="";
		double distanceValue = Double.parseDouble(distance);
		double speedValue = Double.parseDouble(Speed);
		if(distanceValue<30 && speedValue<5){
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
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
			voice = stopName+" "+distance+" metre";
		}
		
		
		return voice;
	}
	
	@Override
	   public void onPause(){
	      if(ttobj !=null){
	         ttobj.stop();
	         ttobj.shutdown();
	      }
	      super.onPause();
	   }
	
	public void prepareTTSEngine() throws Exception{
		try {
			synthesis = SpeechSynthesis.getInstance(this);
			synthesis.setSpeechSynthesisEvent(new SpeechSynthesisEvent() {

				public void onPlaySuccessful() {
					Log.i(TAG, "onPlaySuccessful");
				}

				public void onPlayStopped() {
					Log.i(TAG, "onPlayStopped");
				}

				public void onPlayFailed(Exception e) {
					Log.e(TAG, "onPlayFailed");
					

					AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
					builder.setMessage("Error[TTSActivity]: " + e.toString())
					       .setCancelable(false)
					       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}

				public void onPlayStart() {
					Log.i(TAG, "onPlayStart");
				}

				@Override
				public void onPlayCanceled() {
					Log.i(TAG, "onPlayCanceled");
				}
				
				
			});


		} catch (InvalidApiKeyException e) {
			Log.e(TAG, "Invalid API key\n" + e.getStackTrace());
			Toast.makeText(getApplicationContext(), "ERROR: Invalid API key", Toast.LENGTH_LONG).show();
			throw e;
		}
	}
}
