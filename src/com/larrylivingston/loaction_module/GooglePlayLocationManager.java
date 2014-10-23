package com.larrylivingston.loaction_module;

import java.math.BigDecimal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

public class GooglePlayLocationManager implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	
	private final String ERROR_MESSAGE = "Google Play Service is not available";
	private String TAG = "LOCATION";
	private static double[] latLng = new double[2];
	private Context context;
	private static final int UPDATE_INTERVAL_IN_MILLISECONDS = 100;
	private static final int FAST_INTERVAL_CEILING_IN_MILLISECONDS = 60000;
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	// A request to connect to Location Services
	private LocationRequest mLocationRequest;
	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;
	/*
	 * Note if updates have been turned on. Starts out as "false"; is set to
	 * "true" in the method handleRequestSuccess of LocationUpdateReceiver.
	 */
	boolean mUpdatesRequested = false;
	boolean connected = false;
	private static GooglePlayLocationManager googlePlayLocationManager;

	private GooglePlayLocationManager(Context context) {
		mLocationRequest = LocationRequest.create();
		this.context = context;
		/*
		 * Set the update interval
		 */
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the interval ceiling to one minute
		mLocationRequest
				.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		// Note that location updates are off until the user turns them on
		mUpdatesRequested = true;
		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(context, this, this);
		mLocationClient.connect();
	}

	public static GooglePlayLocationManager getGooglePlayLocationManager(
			Context context) {
		if (googlePlayLocationManager == null) {
			synchronized (GooglePlayLocationManager.class) {
				if (googlePlayLocationManager == null) {
					googlePlayLocationManager = new GooglePlayLocationManager(
							context);
				}
			}
		}
		return googlePlayLocationManager;
	}
	
	

	public void stopLocationUpdate() {
		mLocationClient.removeLocationUpdates(this);
	}

	//Update the user's location periodically
	public void startLocationUpdate() {
		mUpdatesRequested = true;
		if (servicesConnected()) {
			startPeriodicUpdates();
		}
	}

	public double[] getLocation() {

		// If Google Play Services is available
		if (servicesConnected()) {
			// Get the current location
			Location currentLocation = mLocationClient.getLastLocation();
			latLng[0] = currentLocation.getLatitude();
			latLng[1] = currentLocation.getLongitude();
			// Display the current location in the UI
			return latLng;
		}
		return null;
	}
	
	public static double getDistatce( double lat1, double lng1, double lat2, 
			double lng2) {

		
		double R = 6371;
		double distance;
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLng = (lng2 - lng1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
		distance=distance/1000;
		BigDecimal   b   =   new   BigDecimal(distance*1000);  
		double   distanceInKM   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		   		return distanceInKM;
	}

	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d(TAG, TAG + "Google Play Service is available!");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			Toast.makeText(context, ERROR_MESSAGE, Toast.LENGTH_LONG).show();
			return false;
		}
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle bundle) {
		if (mUpdatesRequested) {
			connected = true;
			startPeriodicUpdates();
		}
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
	}

	/**
	 * @param location
	 *            The updated location.
	 */
	@Override
	public void onLocationChanged(Location location) {
		// Update the static location
		latLng[0] = location.getLatitude();
		latLng[1] = location.getLongitude();
	}

	/**
	 * In response to a request to start updates, send a request to Location
	 * Services
	 */
	private void startPeriodicUpdates() {
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}


}
