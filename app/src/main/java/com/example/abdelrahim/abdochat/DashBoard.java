package com.example.abdelrahim.abdochat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class DashBoard extends AppCompatActivity {


    private Toolbar Dtoolbar;
    private ViewPager viewPager;
    private SectionsAdapter DashBoardSectionsAdapter;
    private TabLayout DashTablayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Dtoolbar=(Toolbar) findViewById(R.id.D_tollbar);
        setSupportActionBar(Dtoolbar);
        getSupportActionBar().setTitle("Abdo chat");

        viewPager=(ViewPager)findViewById(R.id.main_viewpager);

        DashBoardSectionsAdapter = new SectionsAdapter(getSupportFragmentManager());

        viewPager.setAdapter(DashBoardSectionsAdapter);
        DashTablayout = (TabLayout) findViewById(R.id.Dash_tabs);
        DashTablayout.setupWithViewPager(viewPager);



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_logout:
                FirebaseAuth.getInstance().signOut();
                sendtologinActivity();
                return true;

            case R.id.main_accountsetting:
                startActivity(new Intent(DashBoard.this, SettingsActivity.class));
                finish();
                return true;
            case R.id.main_all_users:
                startActivity(new Intent(DashBoard.this, AllUsersActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendtologinActivity() {

        startActivity(new Intent(DashBoard.this, MainActivity.class));
        finish();
    }





}
