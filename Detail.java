package com.magsol.drugdictionary;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.magsol.facebook.AsyncFacebookRunner;
import com.magsol.facebook.AsyncFacebookRunner.RequestListener;
import com.magsol.facebook.DialogError;
import com.magsol.facebook.Facebook;
import com.magsol.facebook.Facebook.DialogListener;
import com.magsol.facebook.FacebookError;




import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Detail extends Activity {
TextView heading,h2,par;
LinearLayout linearLayout ;
String data;

private Facebook facebook;
private String messageToPost;
private static final String APP_ID = "237347636457796";
private static final String[] PERMISSIONS = new String[] { "publish_stream" };
private static final String TOKEN = "access_token";
private static final String EXPIRES = "expires_in";
private static final String KEY = "facebook-credentials";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.magsol.drugdictionary.R.layout.activity_detail);
		String file=getIntent().getStringExtra("value");
		heading=(TextView)findViewById(R.id.textView1);
		linearLayout = (LinearLayout) findViewById(com.magsol.drugdictionary.R.id.info);
		data="";
		//linearLayout.setOrientation(RelativeLayout.CENTER_VERTICAL);
		
		XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			    InputStream in_s = getApplicationContext().getAssets().open(file+".html");
		        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	           
//		        BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));
//		        StringBuilder sb = new StringBuilder();
//		        String line = null;
//
//		        while ((line = reader.readLine()) != null) {
//		            sb.append(line);
//		        }
//		        in_s.close();
//		       String s=sb.toString();	   
//		       
//		       parser.setInput(new StringReader(s.replaceAll("&", "and")));
//	            
//		       InputStream stream = new ByteArrayInputStream(s.getBytes("UTF-8"));
//	            
//	            
	            
	            
	            parser.setInput(in_s, null);
	            parseXML(parser);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
	private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
	{
		ArrayList<Drug> Drugs = null;
        int eventType = parser.getEventType();
        Drug currentProduct = null;
        int i=0;
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
        
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	Drugs = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("h2"))
                    {
                    	 // currentProduct = new Drug();
                        String s=parser.nextText();
                    	heading.setText(s);
                         data="    "+s+"\n\n";
                      
                    } else if (name.equals("h3")){
//                    	h2=(TextView)findViewById(R.id.textView22);
//                           h2.setText(parser.nextText());
                    //	data=data+parser.nextText()+"\n";
                    String s=parser.nextText();	
                    	TextView txt1 = new TextView(Detail.this);
            			linearLayout.setBackgroundColor(Color.TRANSPARENT);
            			txt1.setText(s);
            			txt1.setTextSize(20);
            			txt1.setTextColor(Color.BLACK);
            			txt1.setTypeface(null, Typeface.BOLD);
            			linearLayout.addView(txt1);
            			 data=data+s+"\n";
                         //Toast.makeText(getApplicationContext(), data+",", Toast.LENGTH_SHORT).show();

                        	//Log.i("info", name+currentProduct.name+currentProduct.id  );
                        
                    }
                    else if (name.equals("p")){
////                        lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//                        lp.addRule(RelativeLayout.BELOW, recent.getId());		
                    	 String s=parser.nextText();
                    	TextView txt1 = new TextView(Detail.this);
                   
                    			linearLayout.setBackgroundColor(Color.TRANSPARENT);
                    			txt1.setText(s);
                    			
                    			linearLayout.addView(txt1);
                    			data=data+s+"\n";
                    	i++;
                    	
//                    	par=(TextView)findViewById(R.id.textView3);
//                           par.setText(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                   
                    if (name.equalsIgnoreCase("pill") && currentProduct != null){
                    	Drugs.add(currentProduct);
                    } 
            }
            eventType = parser.next();
        }

       // printProducts(Drugs);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(com.magsol.drugdictionary.R.menu.detail, menu);
		
		View v = (View) menu.findItem(R.id.action_settings).getActionView();
		 
        /** Get the edit text from the action view */
        final Button but = ( Button ) v.findViewById(R.id.button1);
        but.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	
		AlertDialog.Builder alrt=new AlertDialog.Builder(Detail.this);
		alrt.setTitle("Share");
		//alrt.setMessage("Choose this audio as ringtone, alarm and other notifications");
		
		
		LinearLayout lila1= new LinearLayout(Detail.this);
	    lila1.setOrientation(1); //1 is for vertical orientation
		
		
		  final RadioButton[] rb = new RadioButton[2];
		    RadioGroup rg = new RadioGroup(Detail.this); //create the RadioGroup
		    rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
		    rb[0]  = new RadioButton(Detail.this);
	        rg.addView(rb[0]); //the RadioButtons are added to the radioGroup instead of the layout
	        rb[0].setText("Facebook");
	        rb[1]  = new RadioButton(Detail.this);
	        rg.addView(rb[1]); //the RadioButtons are added to the radioGroup instead of the layout
	        rb[1].setText("Others");
		 // alarm.setText("Alarm");
		  lila1.addView(rg);
		  alrt.setView(lila1);
		  
		  alrt.setPositiveButton("Share", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
			
			if(rb[0].isChecked())
			{
				facebookShare();
			}
				if(rb[1].isChecked())
				{
					//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
					Intent sendIntent = new Intent();
				      sendIntent.setAction(Intent.ACTION_SEND);
				      sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				      sendIntent.setType("text/plain");
				     ////////////////////////////     for image    ////////////////////////////
				      // sendIntent.setType("image/*");
				     // sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
				      /////////////////////////////////////////////////////////////////////////
				      sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
				      startActivity(sendIntent);
				}
				}
			});
		  alrt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				
					
				}
			});
			alrt.show();
		
	
	
	}
	    
        });
		
		
        return super.onCreateOptionsMenu(menu);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	



//////////////////////Call on button Click ////////////////////////

private void facebookShare1() {
Boolean io=false;
facebook = new Facebook(APP_ID);
restoreCredentials(facebook);
messageToPost = "This is a sample app for facebook authentication!.";
if (!facebook.isSessionValid()) {
loginAndPostToWall();

} else {

io=	postImageToWall();
//io= postToWall(messageToPost);
}
if(io==true){
final AlertDialog alertDialog = new AlertDialog.Builder(
 Detail.this).create();
alertDialog.setCancelable(false);
// Setting Dialog Title
alertDialog.setTitle("Facebook Share");

// Setting Dialog Message
alertDialog.setMessage("Success");
// Setting OK Button	
alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
alertDialog.dismiss();
}
});
alertDialog.show();
}
}






////////////////////////////////////////////FACEBOOK ////////////////////////////////////////////////


@SuppressWarnings("deprecation")
private void facebookShare() {
Boolean io=false;
facebook = new Facebook(APP_ID);
restoreCredentials(facebook);
messageToPost = "This is a sample app for facebook authentication!.";
if (!facebook.isSessionValid()) {
loginAndPostToWall();

} else {

io=	postToWall(data);
//io= postToWall(messageToPost);
}
if(io==true){
final AlertDialog alertDialog = new AlertDialog.Builder(
 Detail.this).create();
alertDialog.setCancelable(false);
// Setting Dialog Title
alertDialog.setTitle("Facebook Share");

// Setting Dialog Message
alertDialog.setMessage("Success");
// Setting OK Button	
alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
alertDialog.dismiss();
}
});
alertDialog.show();
}
}

public boolean saveCredentials(Facebook facebook) {
Editor editor = getApplicationContext().getSharedPreferences(KEY,
Context.MODE_PRIVATE).edit();
editor.putString(TOKEN, facebook.getAccessToken());
editor.putLong(EXPIRES, facebook.getAccessExpires());
return editor.commit();
}

public boolean restoreCredentials(Facebook facebook) {
SharedPreferences sharedPreferences = getApplicationContext()
.getSharedPreferences(KEY, Context.MODE_PRIVATE);
facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
return facebook.isSessionValid();
}

public Boolean loginAndPostToWall() {
facebook.authorize(Detail.this, PERMISSIONS,
Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
return true;
}

private Boolean postImageToWall() {

View vi = findViewById(R.id.scrollView1);
vi.setDrawingCacheEnabled(true);
Bitmap btmp = vi.getDrawingCache();
ByteArrayOutputStream stream = new ByteArrayOutputStream();

btmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
byte[] bitMapData = stream.toByteArray();
Bundle params1 = new Bundle();
params1.putString("method", "photos.upload");
params1.putByteArray("picture", bitMapData);
params1.putString("caption", "Help In Learning Game");
AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
mAsyncRunner.request(null, params1, "POST", new SampleUploadListener(),null);
return true;

}

public boolean postToWall(String message) {
Bundle parameters = new Bundle();
parameters.putString("message", message);
parameters.putString("description", "topic share");
AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
mAsyncRunner.request(null, parameters, "POST",new SampleUploadListener(), null);
return true;

}

public class SampleUploadListener extends BaseRequestListener {

public void onComplete(final String response, final Object state) {
try {
Log.d("Facebook-Example", "Response: " + response.toString());

} catch (FacebookError e) {
Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
}
}
}

public abstract class BaseRequestListener implements RequestListener {

public void onFacebookError(FacebookError e, final Object state) {
Log.e("Facebook", e.getMessage());
e.printStackTrace();
}

public void onFileNotFoundException(FileNotFoundException e,
final Object state) {
Log.e("Facebook", e.getMessage());
e.printStackTrace();
}

public void onIOException(IOException e, final Object state) {
Log.e("Facebook", e.getMessage());
e.printStackTrace();
}

public void onMalformedURLException(MalformedURLException e,
final Object state) {
Log.e("Facebook", e.getMessage());
e.printStackTrace();
}

}



class LoginDialogListener implements DialogListener {
public void onComplete(Bundle values) {
saveCredentials(facebook);
// postImageToWall(values.getString(Facebook.TOKEN));
Log.i("ACCESSTOKEN", values.getString(Facebook.TOKEN));
if (messageToPost != null) {
postToWall(messageToPost);
}
}

public void onFacebookError(FacebookError error) {
showToast("Authentication with Facebook failed!");
}

public void onError(DialogError error) {
showToast("Authentication with Facebook failed!");
}

public void onCancel() {
showToast("Authentication with Facebook cancelled!");
}
}

private void showToast(String message) {
Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
.show();
}


	
	
	
	
	

}
