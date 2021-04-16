package edu.byu.cs.tweeter.client.view.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.client.R;

/**
 * The first activity that appears when the app is run.
 * Contains the "Login" and "Register" tabs
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginSectionsPagerAdapter loginSectionsPagerAdapter = new LoginSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.login_view_pager);
        viewPager.setAdapter(loginSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.loginTabs);
        tabs.setupWithViewPager(viewPager);
    }
}
