package com.example.btp;

import java.util.ArrayList;
import java.util.Arrays;




import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class instruction extends Activity {
	  private ListView mainListView ;  
	  private ArrayAdapter<String> listAdapter ; 
	  ImageView continues;
	  EditText phone_no;
	  @Override  
	  public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	    setContentView(R.layout.instruction);   
	    TextView myTextView=(TextView) findViewById( R.id. textHeading); 
	    continues = (ImageView)findViewById(R.id.continue_image);
	    mainListView = (ListView) findViewById( R.id.mainListView ); 
	    phone_no = (EditText) findViewById( R.id.phone_no ); 
	    
	  
	    String[] planets = new String[] { "The photograph should be clearly  visible.",
	    								"The photo should  not be edited by any means otherwise it would not be considered for challan generation.",  
	                                      "For any fake image if found  the sender  would  not be rewarded  for such false acts.", 
	                                      "The app users are rewarded Rs 50 for proper image as per rules."};    
	    ArrayList<String> planetList = new ArrayList<String>();  
	    planetList.addAll( Arrays.asList(planets) );  
	      
	    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);  
	      
	  
	      
	    // Set the ArrayAdapter as the ListView's adapter.  
	    mainListView.setAdapter( listAdapter );    
	    continues.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		          // TODO Auto-generated method stub
		        	Boolean number=get_no();
		        	if(number)
		        	{
		        		Intent i = new Intent(instruction.this, PhotoIntentActivity.class);
		        		phone_no = (EditText) findViewById( R.id.phone_no );
		      		  String phoneno=phone_no.getText().toString();
		        		i.putExtra("phone_number",phoneno );
	            		startActivity(i);
		        	}
		        	else
		        	{
		        		Toast.makeText(instruction.this, "Enter Valid Phone number", 0).show();
		        	}
		        }
		      });
	  } 
	  private Boolean get_no() {
		  phone_no = (EditText) findViewById( R.id.phone_no );
		  String phoneno=phone_no.getText().toString();
		  if(phoneno.length()==10)
			  return true;
		  else
			  return false;
	}
	  private class start_activity implements OnClickListener {
		    @Override
		    public void onClick(View v) {
				
		    }}

}
