package com.example.fusedlocationprovider;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements LocationListener,ConnectionCallbacks{

	Location loc;
	double var_latitude,var_longitude;
	Button update;
	TextView tv_lat,tv_long;
	LocationRequest locationRequest;
	GoogleApiClient mGoogleApiClient;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tv_lat = (TextView) findViewById(R.id.latitude);
        tv_long = (TextView) findViewById(R.id.longitude);
        update = (Button) findViewById(R.id.btn_Update);
        
        //connect the google API client if
        // if connected will call onConnected()
        // if failed will call onConnectionSuspended() 
        mGoogleApiClient.connect();
       
      
        /*
         * To store the parameters for request to fused location Provider 
         * We need a create a 'locationRequest' object that will set he levels 
         * of accuracy by :
         * 	  -	setInterval() :: Set the interval after which the request is done again
         * 	  -	setFastestInterval() :: This method sets the fastest rate in milliseconds at 
         * 								which your app can handle location updates
         * 	  -	setPriority()
         * 			there are various priority levels that You can set according to the use of battery life
         * 			
         * 			1.PRIORITY_BALANCED_POWER_ACCURACY - Use this setting to request location precision 
         * 												 to within a city block, which is an accuracy of approximately 100 meters.
         * 												(NOTE: From wifi/network)
         * 			2.PRIORITY_HIGH_POWER_ACCURACY - Uses GPS
         * 			3.PRIORITY_LOW_POWER_ACCURACY - WITHIN CITY 10km
         * 			4.PRIORITY_NO_POWER_ACCURACY - No location request is done by the app but uses requests done by other apps
         */
        
        locationRequest = new LocationRequest();
    	locationRequest.setInterval(5000);
    	locationRequest.setFastestInterval(1000);
    	locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        
       
        /*
    	 * Request location from location services and
    	 * google's 'FusedLocationApi' 
    	 *  parameters are GoogleAPICient,locationRequest object and location Listener
    	 */
    	update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Updating Location...", Toast.LENGTH_LONG).show();
		    	LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest,MainActivity.this);
			}
		});
    	    	        
    }
    
    
    //Unregister the Location Updater when activity is pause
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    	super.onPause();
    }
    
   
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.v("location","Location Changed");
		loc=location;	
		if (loc!=null) {
			var_latitude=loc.getLatitude();
			var_longitude=loc.getLongitude();
			tv_lat.setText(""+var_latitude);
			tv_long.setText(""+var_longitude);
		}else{
			tv_lat.setText("-------");
			tv_long.setText("-------");
		}
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this,"Google API Client Connected", Toast.LENGTH_LONG).show();
		loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (loc==null) {
			Toast.makeText(getApplicationContext(), "LastKnownLocation is null", Toast.LENGTH_LONG).show();
			var_latitude=loc.getLatitude();
			var_longitude=loc.getLongitude();
			tv_lat.setText(""+var_latitude);
			tv_long.setText(""+var_longitude);
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this,"Google API Client Not Connected", Toast.LENGTH_LONG).show();
	}
}
