package com.example.ice_cream.lrange_control.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ice_cream.lrange_control.R;

import java.util.ArrayList;

public class daohang extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    Fragment remoteSys;
    Fragment touchSys;
    Fragment hotSys;
    Fragment ipSys;
    Fragment upSys;
    ArrayList<Fragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daohang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentList=new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.daohang, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.Ip_set:
                showIpsetting();
                break;
            case R.id.Remote_con:
                showRemoteFileSys();
                break;
            case R.id.local_fld:

                break;
            case R.id.mouse_pad:
                showTouchActivity();
                break;
            case R.id.hot_mgr:
                showHotActivity();
                break;
            case  R.id.Down_lst:

                break;
            case R.id.Up_lst:
                showUploadActivity();
                break;
            default:
 }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showUploadActivity(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(upSys==null) {
            upSys = new UploadActivity();
            fragmentList.add(upSys);
            transaction.add(R.id.fram_lay,upSys);
        }
        hideAllFragments(transaction);
        transaction.show(upSys);
        transaction.commit();
    }

    private void showIpsetting(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(ipSys==null) {
            ipSys = new ipsettingsActivity();
            fragmentList.add(ipSys);
            transaction.add(R.id.fram_lay,ipSys);
        }
        hideAllFragments(transaction);
        transaction.show(ipSys);
        transaction.commit();
    }

    private void showHotActivity(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(hotSys==null) {
            hotSys = new HotActivity();
            fragmentList.add(hotSys);
            transaction.add(R.id.fram_lay,hotSys);
        }
        hideAllFragments(transaction);
        transaction.show(hotSys);
        transaction.commit();
    }

    private void showTouchActivity(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(touchSys==null) {
            touchSys = new TouchActivity();
            fragmentList.add(touchSys);
            transaction.add(R.id.fram_lay,touchSys);
        }
        hideAllFragments(transaction);
        transaction.show(touchSys);
        transaction.commit();
    }

    private void showRemoteFileSys() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(remoteSys==null) {
            remoteSys = new RemoteFileFragment();
            fragmentList.add(remoteSys);
            transaction.add(R.id.fram_lay,remoteSys);
        }
        hideAllFragments(transaction);
        transaction.show(remoteSys);
        transaction.commit();

    }
    private void hideAllFragments(FragmentTransaction transaction){
        // 不隐藏其他fragment的话，帧上有很多fragment重叠在一起
        for (int i = 0; i <fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            if(fragment!=null){
                transaction.hide(fragment);
            }
        }
    }


}
