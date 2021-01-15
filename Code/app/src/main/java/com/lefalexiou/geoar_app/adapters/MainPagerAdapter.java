package com.lefalexiou.geoar_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.lefalexiou.geoar_app.layout.LiveFragment;
import com.lefalexiou.geoar_app.layout.MapFragment;
import com.lefalexiou.geoar_app.layout.MenuFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return LiveFragment.newInstance();
            case 1:
                return MapFragment.newInstance();
            case 2:
                return MenuFragment.newInstance();
            default:
                return LiveFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
