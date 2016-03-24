package com.watsonlogic.artifacts2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private final String TAG = "MainActivity";
    final private Context ctx = this;
    private Button beginBtn;
    private Resources resources;
    private String enableNetwork, settings, cancel, unableToConnect, yes, no, gpsDisabled, locationServicesMustBeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolBar();
        setFab();
        getStringResources();
        getViews();


    }

    private void getStringResources() {
        resources = ctx.getResources();
        enableNetwork = resources.getString(R.string.enable_network);
        settings = resources.getString(R.string.action_settings);
        cancel = resources.getString(R.string.cancel);
        unableToConnect = resources.getString(R.string.unable_to_connect);
        yes = resources.getString(R.string.yes);
        no = resources.getString(R.string.no);
        gpsDisabled = resources.getString(R.string.gps_disabled);
        locationServicesMustBeEnabled = resources.getString(R.string.location_services_must_be_enabled);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        if (!isNetworkConnected()) {
            return;
        }
        if (!isLocationEnabled()) {
            return;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        boolean mobileReady = isConnectedMobile(ni);
        boolean wifiReady = isConnectedWifi(ni);
        if (mobileReady || wifiReady) {
            return true;
        }
        promptUserToEnableConnection();
        return false;
    }

    public boolean isConnectedMobile(NetworkInfo ni) {
        return ni != null && ni.isConnected() && ni.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public boolean isConnectedWifi(NetworkInfo ni) {
        return ni != null && ni.isConnected() && ni.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private void promptUserToEnableConnection() {
        DialogInterface.OnClickListener p = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        };
        DialogInterface.OnClickListener n = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.finish();
            }
        };
        generateDialog(enableNetwork, unableToConnect, settings, cancel, false, p, n);
    }

    private void generateDialog(String msg, String title, String pos, String neg, boolean cancelable, DialogInterface.OnClickListener posD, DialogInterface.OnClickListener negD) {
        AlertUtil au = new AlertUtil(msg, title, pos, neg, cancelable, posD, negD, ctx);
        au.buildAlert();
        au.showAlert();
    }

    private boolean checkLocationPermissions() {
        if ((Build.VERSION.SDK_INT >= 23) &&
                (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        if (!checkLocationPermission()) {
            Log.d("LOCATION (permission)", "permission denied!");
            return false;
        }
        return true;
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    private boolean isLocationEnabled() {
        if (!checkLocationPermissions()) return false;
        LocationManager locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled && !networkEnabled) {
            promptUserToEnableLocation();
            return false;
        }
        return true;
    }

    private void promptUserToEnableLocation() {
        DialogInterface.OnClickListener p = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };
        DialogInterface.OnClickListener n = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { //do nothing
            }
        };
        generateDialog(gpsDisabled, locationServicesMustBeEnabled, yes, no, false, p, n);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void getViews() {
        beginBtn = (Button) findViewById(R.id.begin_btn);
    }

    public void launchTabsActivity(View v) {
        startActivity(new Intent(this, TabsActivity.class));
    }

    private void setToolBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void setFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
