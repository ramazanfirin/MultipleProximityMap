package in.wptrafficanalyzer.multipleproximitymapv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

public class NotificationView extends Activity {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		
	
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 
		// set title
		alertDialogBuilder.setTitle("Defacto indirim firsati");

		Cursor c = getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null); 
		c.moveToFirst();
		String name =(c.getString(c.getColumnIndex("display_name")));
		c.close();
		// set dialog message
		alertDialogBuilder
			.setMessage("Merhaba "+name+".Viaport defactoda size özel indirimler var")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
//					// if this button is clicked, close
//					// current activity
//					MainActivity.this.finish();
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
			
			
			Intent intent = getIntent();
			 
	        // 2. get message value from intent
	        String url = (String)intent.getExtras().get("url");
			
			Intent intentNew = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//intentNew.get
		    getApplicationContext().startActivity(intentNew);
	}
}