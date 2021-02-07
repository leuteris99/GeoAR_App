package com.lefalexiou.geoar_app.layout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.lefalexiou.geoar_app.adapters.MainPagerAdapter;
import com.lefalexiou.geoar_app.R;
import com.lefalexiou.geoar_app.models.Place;
import com.lefalexiou.geoar_app.models.Route;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends FragmentActivity implements MenuFragment.MenuFragmentListener, MapFragment.MapFragmentListener {
    private static final String TAG = "MainActivity";
    private static final int RC_LOCATION = 123;
    private FirebaseAnalytics mFirebaseAnalytics;
    private MainPagerAdapter mainPagerAdapter;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        askLocationPermissions();

        viewPager = (ViewPager) findViewById(R.id.pager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(mainPagerAdapter);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.live_page:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.map_page:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.options_page:
                            viewPager.setCurrentItem(2);
                            break;
                        default:
                            viewPager.setCurrentItem(0);
                            break;
                    }
                    return true;
                }
            };

    @AfterPermissionGranted(RC_LOCATION)
    private void askLocationPermissions() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            Toast.makeText(this, "We track every move you make.", Toast.LENGTH_LONG).show();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "We need your location.",
                    RC_LOCATION, perms);
        }
    }

    @Override
    public void onMenuDataTransfer(Route route) {
        Log.d(TAG, "onDataTransfer: transferring route data");
        mainPagerAdapter.getMapFragmentInstance().updatePlacesData(route);
    }

    @Override
    public void gettingAwayFromPlaces() {
        mainPagerAdapter.getLiveFragmentInstance().gettingAwayFromPlaces();
    }

    @Override
    public void onMapDataTransfer(Place nearbyPlace) {
        Log.d(TAG, "onMapDataTransfer: transferring nearby place data");
        mainPagerAdapter.getLiveFragmentInstance().getNearbyPlace(nearbyPlace);
    }
}