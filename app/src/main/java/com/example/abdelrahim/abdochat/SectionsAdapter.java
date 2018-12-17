package com.example.abdelrahim.abdochat;

import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Abd Elrahim on 12/23/2017.
 */

class SectionsAdapter extends FragmentPagerAdapter {



    public SectionsAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;

            case 1:
                ChatFragments chatFragments = new ChatFragments();
                return chatFragments;

            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence  getPageTitle(int position) {
        switch (position){

            case 0:
                return "Requests";
            case 1:
                return "Chats";

            case 2:
                return "Friends";

            default:
                return null;



        }
    }
}
