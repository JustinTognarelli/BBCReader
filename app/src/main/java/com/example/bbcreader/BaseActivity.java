package com.example.bbcreader;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

/**
 * A base activity for the whole app that contains the toolbar and the navigation drawer
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle toggle;

    /**
     * Creates the base layout and manages navigation for the app
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Add actions to the drawer

                if (menuItem.getItemId() == R.id.Home){
                    displayFragment(new Loading());
                } else if (menuItem.getItemId() == R.id.Settings){
                    displayFragment(new SettingsActivity());
                } else if (menuItem.getItemId() == R.id.Favourites){
                    displayFragment(new FavouritesActivity());
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        displayFragment(new Loading());
    }

    /**
     * Shows a help popup to help the user navigate the app
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getMessage())
                    .setTitle(getResources().getString(R.string.Help));

            builder.setPositiveButton(getResources().getString(R.string.Close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /**
     * Sets which page is loaded at the moment
     */

    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    /**
     * Gets a message based off which layout is currently loaded in the fragment
     */

    private String getMessage(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment);

        String message = "";
        if (currentFragment instanceof MainActivity) {
            message = getResources().getString(R.string.MainActivityHelp);
        } else if (currentFragment instanceof SettingsActivity) {
            message = getResources().getString(R.string.SettingsHelp);
        } else if (currentFragment instanceof ArticleInfo) {
            message = getResources().getString(R.string.ArticleInfoHelp);
        } else if (currentFragment instanceof FavouritesActivity) {
            message = getResources().getString(R.string.FavHelp);
        }
        return message;
    }

}
