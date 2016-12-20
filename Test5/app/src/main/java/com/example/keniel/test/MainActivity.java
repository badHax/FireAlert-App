package com.example.keniel.test;

import android.*;
import android.Manifest;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.keniel.test.R.id.textView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver broadcastReceiver2;
    private TextView textview;
    public static Double x,y;
    public static Double getY() {
        return y;
    }
    ImageButton imageButton;

    public static void setY(Double y) {
        MainActivity.y = y;
    }



    public static Double getX() {
        return x;
    }

    public static void setX(Double x) {
        MainActivity.x = x;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    textview.append("\n" + intent.getExtras().get("address"));

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        if (broadcastReceiver2 == null){
            broadcastReceiver2 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle bundle = intent.getExtras();
                    setX(bundle.getDouble("lat"));
                    setY(bundle.getDouble("lng"));
                }
            };
        }

        registerReceiver(broadcastReceiver2,new IntentFilter("latlng"));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver !=null){
            unregisterReceiver(broadcastReceiver);
        }
        if (broadcastReceiver2 !=null){
            unregisterReceiver(broadcastReceiver2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NetworkManager.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        imageButton = (ImageButton) findViewById(R.id.panic);
        textview = (TextView) findViewById(R.id.test);

        if(!runtime_permissions()){
            startTracking();
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap();
                params.put("content_type", "alert");
                params.put("description", "PANIC!!!");
                params.put("lat",MainActivity.getY().toString());
                params.put("long",MainActivity.getX().toString());
                JSONObject object = new JSONObject(params);

                NetworkManager.getInstance().somePostRequestReturningString(object, new NetworkManager.SomeCustomListener<String>()
                {
                    @Override
                    public void getResult(String result)
                    {
                        if (!result.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Your alert has been made and recieved",Toast.LENGTH_LONG).show();

                            //do what you need with the result...
                        }
                    }
                });
            }
        });


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
        getMenuInflater().inflate(R.menu.main, menu);
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

    private boolean runtime_permissions(){
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ){

            requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA},100);
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                startTracking();
            }else{
                runtime_permissions();
            }
        }
    }

    private void startTracking() {
        Intent i = new Intent(getApplicationContext(),GPS_Service.class);
        startService(i);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        FragmentManager fm = getFragmentManager();
        Fragment fragment;
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            fragment = new gmsFragment();
            fm.beginTransaction().replace(R.id.relative_layout_for_fragment, fragment).commit();
            imageButton.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_gallery) {
            fragment = new ForumFragment();
            fm.beginTransaction().replace(R.id.relative_layout_for_fragment, fragment).commit();
            imageButton.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_slideshow) {
            fragment = new CameraFragment();
            fm.beginTransaction().replace(R.id.relative_layout_for_fragment, fragment).commit();
            imageButton.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_manage) {
            fragment = new AudioFragment();
            fm.beginTransaction().replace(R.id.relative_layout_for_fragment, fragment).commit();
            imageButton.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_share) {
            fragment = new VideoFragment();
            fm.beginTransaction().replace(R.id.relative_layout_for_fragment, fragment).commit();
            imageButton.setVisibility(View.INVISIBLE);

            imageButton.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_send) {

        }
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
