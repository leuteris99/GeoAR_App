package com.lefalexiou.geoar_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lefalexiou.geoar_app.layout.LiveFragment;
import com.lefalexiou.geoar_app.layout.MapFragment;
import com.lefalexiou.geoar_app.layout.MenuFragment;
import com.lefalexiou.geoar_app.models.Route;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private MenuFragment menuFragment;
    private MapFragment mapFragment;
    private LiveFragment liveFragment;

    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                liveFragment = LiveFragment.newInstance();
                return liveFragment;
            case 1:
                mapFragment = MapFragment.newInstance();
                return mapFragment;
            case 2:
                menuFragment = MenuFragment.newInstance();
                return menuFragment;
            default:
                return LiveFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public MenuFragment getMenuFragmentInstance(){
        return menuFragment;
    }
    public MapFragment getMapFragmentInstance(){
        return mapFragment;
    }
}
