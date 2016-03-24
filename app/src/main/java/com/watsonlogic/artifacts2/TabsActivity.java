package com.watsonlogic.artifacts2;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabsActivity extends AppCompatActivity implements LocationListener {
    private static final long MIN_DELTA_DISTANCE = 0; //no minimum change in distance req'd
    private static final long MIN_TIME_BW_UPDATES = 1; //no delay between updates req'd
    private final String TAG = "TabsActivity";
    public Context ctx;
    protected Location loc;
    private LocationManager locMgr;
    private boolean GPSReady, networkReady;
    private double latitude, longitude;
    private TextView tv;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private Location getLocation() {
        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        GPSReady = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkReady = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        String provider = null;
        if (GPSReady) {
            provider = LocationManager.GPS_PROVIDER;
            Log.d(TAG, "GPS Enabled");
        } else if (networkReady) {
            provider = LocationManager.NETWORK_PROVIDER;
            Log.d(TAG, "Network Enabled");
        }
        if (loc == null) {
            try {
                locMgr.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DELTA_DISTANCE, this);
                if (locMgr != null) {
                    loc = locMgr.getLastKnownLocation(provider);
                    if (loc != null) {
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return loc;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");
        Log.d(TAG, location.getLatitude() + "," + location.getLongitude());
        tv.append(location.getLatitude() + "," + location.getLongitude() + "\n");
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



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment f = null;
            switch(position){
                case 0:
                    f = LocationFragment.newInstance(position+1);
                    break;
                case 1:
                    f = LeaveArtifactFragment.newInstance(position+1);
                    break;
                case 2:
                    f = ProfileFragment.newInstance(position+1);
                    break;
                default:
                    f = LocationFragment.newInstance(position+1);
            }
            return f;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
