package in.wptrafficanalyzer.multipleproximitymapv2;



import java.util.ArrayList;
import java.util.Locale;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.InvalidApiKeyException;
import org.ispeech.error.NoNetworkException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SelectionActivity extends Activity{
	SpeechSynthesis synthesis;
	public static final String TAG = "iSpeech SDK Sample";
	private static final int REQUEST_CODE = 1234;
	
	String selection="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selection);
		selection = "";
		String voice  = "Durağa geldiniz.Duraktan geçen otobusler:";
		String voice2  = "495 Kıranardı , 300 Talas, 540 esenyurt.";
		String voice3  = "Lütfen butona basarak seçim yapınız.";
		
		Toast.makeText(getBaseContext(),voice+voice2+voice3 ,Toast.LENGTH_LONG).show();
//		try {
//			prepareTTSEngine();
//			synthesis.setStreamType(AudioManager.STREAM_MUSIC); 
//			synthesis.setVoiceType("eurturkishmale");
//			synthesis.speak(voice+voice2+voice3);
//			//synthesis.speak(voice2);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		final Button accept = (Button) findViewById(R.id.accept);
		accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(selection.equals("")){
            		Toast.makeText(getBaseContext(),"Lütfen Seçim Yapınız." ,Toast.LENGTH_LONG).show();
            	}else{
            		Toast.makeText(getBaseContext(),"Otobüs gelirken haber verilecektir." ,Toast.LENGTH_LONG).show();
            		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            		MyApplication mApplication = (MyApplication)getApplicationContext();
            		mApplication.setArrivedToStop(true);
               	 startActivity(intent);
            	}
            	 	
            }
        });
        
        
        
		
	}	
	
	/**
     * Handle the action of the button being clicked
     */
    public void speakButtonClicked(View v)
    {
        startVoiceRecognitionActivity();
    }
    
    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
       // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale..toString());
        startActivityForResult(intent, REQUEST_CODE);
    }
 
    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {   boolean founded= false;
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
        	ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
             String asd="";
        	if("495".equals(result.get(0))){
        		selection = "Kıranardı";
        		founded = true;
        		
        	}else if("300".equals(result.get(0))){
        		selection = "Talas";
        		founded = true;
        	}else if("540".equals(result.get(0))){
        		selection = "esenyurt";
        		founded = true;
        	}else{
        		selection="bulunamadı";	
        		founded = false;
        	}
        	
        	String voice="";
        	if(founded==true){
        		voice  = " yapılan seçim:"+ selection+". Onaylıyorsanız kabul butonuna basınız.";
        	}else{
        		voice  = " anlaşılmadı. Tekrar deneyin";
        	}
        	
        	
        	Toast.makeText(getBaseContext(),voice ,Toast.LENGTH_LONG).show();
//        	try {
//				//synthesis.speak(voice);
//			} catch (BusyException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (NoNetworkException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
        super.onActivityResult(requestCode, resultCode, data);
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
					

					AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
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
