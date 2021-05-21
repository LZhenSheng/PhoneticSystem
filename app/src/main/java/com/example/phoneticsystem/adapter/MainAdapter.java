package com.example.phoneticsystem.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.phoneticsystem.fragment.DiscoveryFragment;
import com.example.phoneticsystem.fragment.FirstPagesFragment;
import com.example.phoneticsystem.fragment.MeFragment;


/**
 * 主界面ViewPager的Adapter
 *
 */
public class MainAdapter extends BaseFragmentPagerAdapter<Integer> {

    public MainAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    /**
     * 返回Fragment
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FirstPagesFragment.newInstance();
        } else {
            return MeFragment.newInstance();
        }
    }


}