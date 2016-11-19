package com.gamila.zm.ThirdPartiesLibsDemo.locations.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.gamila.zm.ThirdPartiesLibsDemo.AppConstants;
import com.gamila.zm.ThirdPartiesLibsDemo.R;
import com.gamila.zm.ThirdPartiesLibsDemo.authorization.view.activity.AuthorizationActivity;
import com.gamila.zm.ThirdPartiesLibsDemo.locations.view.fragment.LocationsFragment;
import com.gamila.zm.ThirdPartiesLibsDemo.util.SharedPreferencesUtil;

public class HomeActivity extends AppCompatActivity {

    private String username ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*if(getIntent() != null){
           username =  getIntent().getStringExtra(AppConstants.USER_NAME);
        }*/

        username = SharedPreferencesUtil.getStringPreference(this,AppConstants.USER_NAME);
        getSupportActionBar().setTitle(getString(R.string.welcome_username,username));
        goToFragment(LocationsFragment.newInstance());


    }

    public void goToFragment(Fragment fragemt) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_fragment, fragemt).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out:

                LoginManager.getInstance().logOut();
                SharedPreferencesUtil.removePreference(this, AppConstants.GOOGLE_TOKEN);
                SharedPreferencesUtil.removePreference(this, AppConstants.IS_LOGEDIN);

                startActivity(new Intent(this, AuthorizationActivity.class));


                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
