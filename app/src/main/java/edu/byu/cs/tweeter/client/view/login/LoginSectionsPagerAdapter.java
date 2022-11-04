package edu.byu.cs.tweeter.client.view.login;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.byu.cs.tweeter.R;

/**
 * Used by LoginActivity to implement the "login" and "register" tabs.
 */
public class LoginSectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String LOG_TAG = "LoginSectionsPagerAdapter";

    private static final int LOGIN_FRAGMENT_POSITION = 0;
    private static final int REGISTER_FRAGMENT_POSITION = 1;
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.loginTabTitle, R.string.registerTabTitle};
    private final Context mContext;


    public LoginSectionsPagerAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == LOGIN_FRAGMENT_POSITION) {
            return LoginFragment.newInstance();
        } else if (position == REGISTER_FRAGMENT_POSITION) {
            return RegisterFragment.newInstance();
        } else {
            Log.e(LOG_TAG, "Unknown fragment requested.");
            return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
