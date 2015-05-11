package in.wptrafficanalyzer.multipleproximitymapv2;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.maps.GoogleMap;

import android.content.Context;
import android.os.AsyncTask;

public class GetStationCoordinationTask extends AsyncTask<String, Void, String> {
	private Context context;
	private GoogleMap map;
    private MainActivity mainActivity;
	
	
	public GetStationCoordinationTask(Context _context,MainActivity _mainActivity) {
		super();
		context = _context;
		mainActivity = _mainActivity;		
		// TODO Auto-generated constructor stub
	}



	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		
		 String url = "http://www.kaliteyazilim.com:8080/yoltariffserver/rest/hello/getStationInformation";
		 //url = "http://192.168.43.56:8080/yoltariffserver/rest/hello/getStationInformation";
		 String returnString=""; 
         HttpClient httpclient = new DefaultHttpClient();
         HttpGet request = new HttpGet(url);
         ResponseHandler<String> handler = new BasicResponseHandler();
         //HttpResponse httpResponse = httpclient.execute(request);
         try {
        	 returnString = httpclient.execute(request, handler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        
		
		return returnString;
	}
	
	@Override
    protected void onPostExecute(String msg) {
        if(msg==null || msg.equals(""))
        	return;
		String[] values  = msg.split(" ");
		String lat = values[0];
		String lng = values[1];
		
		mainActivity.addTargetLocation(Double.parseDouble(lat),Double.parseDouble(lng));
		
		
    }
}
