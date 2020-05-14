package com.example.travel01;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.travel01.ui.dashboard.DashboardFragment;
import com.example.travel01.ui.home.HomeFragment;
import com.example.travel01.ui.notifications.NotificationsFragment;
import com.example.travel01.ui.personalinfo.PersonalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AllMainActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    Fragment now_fragment;

    HomeFragment homeFragment_only;
    DashboardFragment dashboardFragment_only;
    NotificationsFragment notificationsFragment_only;
    PersonalFragment personalFragment_only;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                    //天气视图
                case R.id.navigation_home:
                    switchfragments(homeFragment_only);
                    return true;
                    //景点视图
                case R.id.navigation_dashboard:
                    switchfragments(dashboardFragment_only);
                    return true;
                    //酒店视图
                case R.id.navigation_notifications:
                    switchfragments(notificationsFragment_only);
                    return true;
                    //个人信息视图
                case R.id.navigation_info:
                    switchfragments(personalFragment_only);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_info)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        homeFragment_only = new HomeFragment();
        dashboardFragment_only = new DashboardFragment();
        notificationsFragment_only = new NotificationsFragment();
        personalFragment_only = new PersonalFragment();

        setDefaultFragment(homeFragment_only);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    // 设置默认进来是tab 显示的页面
    private void setDefaultFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.nav_host_fragment, fragment);
        transaction.commit();
        now_fragment = fragment;
    }

    public void switchfragments(Fragment to) {
        //now_fragment即当前视图
        if (now_fragment != to) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            //没有被添加则添加视图，并隐藏原来视图
            if (!to.isAdded()) {
                transaction.hide(now_fragment).add(R.id.nav_host_fragment, to).commit();
            }
            //否则直接隐藏当前视图，显示点击视图
            else {
                transaction.hide(now_fragment).show(to).commit();
            }
            now_fragment = to;
        }

    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
}
