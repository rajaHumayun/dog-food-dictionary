package com.magsol.drugdictionary;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
class Drug
{

	public String name;
	public String id;
	

}
public class MainActivity extends Activity {
	ArrayList<String> drugList, drugListid,searchlist;
	ListView mainListView ;
	String search="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 drugList = new ArrayList<String>(); 
		 drugListid = new ArrayList<String>(); 
		 searchlist = new ArrayList<String>();
		 mainListView = (ListView) findViewById( R.id.listView1 );  
		XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			    InputStream in_s = getApplicationContext().getAssets().open("pills.xml");
		        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	            parser.setInput(in_s, null);

	            parseXML(parser);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		showitems();
		
		mainListView.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
			      //Object listItem = list.getItemAtPosition(position);
			   
			   String value=drugListid.get(position);
			   if(!search.equals(""))
			   {
				   value=searchlist.get(position);
				   int i=drugList.indexOf(value);
				   value=drugListid.get(i);
				   
			   }
			   
			   
			   Log.i("info",value);
			   
			   Intent i= new Intent(getApplicationContext(), Detail.class);
			   i.putExtra("value", value);
			   startActivity(i);
			   
			   
			   
			   } 
			});
		
	}
void showitems(){
	
	ListAdapter  listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,drugList);
	 if(!search.equals(""))
	 {
		 listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,searchlist);
		 
	 }
			mainListView.setAdapter( listAdapter ); 
	
}
	private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
	{
		ArrayList<Drug> Drugs = null;
        int eventType = parser.getEventType();
        Drug currentProduct = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	Drugs = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("pills")){
                        currentProduct = new Drug();
                    } else if (currentProduct != null){
                        if (name.equals( "pill")){
                            currentProduct.name = parser.getAttributeValue(0);
                      
                        	currentProduct.id = parser.getAttributeValue(1);
                        	Log.i("info", name+currentProduct.name+currentProduct.id  );
                       
                        	drugListid.add(currentProduct.id);
                        	   drugList.add(currentProduct.name);  
                        }  
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

        printProducts(Drugs);
	}

	private void printProducts(ArrayList<Drug> products)
	{
		String content = "";
		Iterator<Drug> it = products.iterator();
		while(it.hasNext())
		{
			Drug currProduct  = it.next();
			content = content + "Name :" +  currProduct.name + "\n";
			content = content + "Id :" +  currProduct.id + "\n";
			//content = content + "Color :" +  currProduct.color + "n";
//			TextView display = (TextView)findViewById(R.id.textView1);
//			display.setText(content);
		}

		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 /** Get the action view of the menu item whose id is search */
        
 
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		View v = (View) menu.findItem(R.id.action_settings).getActionView();
		 
        /** Get the edit text from the action view */
        final EditText txtSearch = ( EditText ) v.findViewById(R.id.editText1);
        final Button but = ( Button ) v.findViewById(R.id.button1);
        but.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        
		searchlist.clear();
		Toast.makeText(getBaseContext(), "Search : " + txtSearch.getText(), Toast.LENGTH_SHORT).show();
        search = txtSearch.getText().toString();
        int searchListLength = drugList.size();
        for (int i = 0; i < searchListLength; i++) {
        if (drugList.get(i).toLowerCase().contains(search)) {
        //Do whatever you want here
       searchlist.add(drugList.get(i));
        
        
        }
        }
       showitems();
	}
});
        /** Setting an action listener */
       
        txtSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
	            // Toast.makeText(getBaseContext(), "Search : " + txtSearch.getText(), Toast.LENGTH_SHORT).show();

	             searchlist.clear();
	     		//Toast.makeText(getBaseContext(), "Search : " + txtSearch.getText(), Toast.LENGTH_SHORT).show();
	             search = txtSearch.getText().toString();
	             int searchListLength = drugList.size();
	             for (int i = 0; i < searchListLength; i++) {
	             if (drugList.get(i).toLowerCase().contains(search)) {
	             //Do whatever you want here
	            searchlist.add(drugList.get(i));
	             }}
	             showitems();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
       
		
		return super.onCreateOptionsMenu(menu);
	}

}
