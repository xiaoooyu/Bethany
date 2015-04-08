package com.cloudnapps.bethany;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private Drawable mActionBarBackgroundDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.k11.bethany.R.layout.activity_main);

        mActionBarBackgroundDrawable = getResources().getDrawable(com.k11.bethany.R.drawable.ab_background);
        mActionBarBackgroundDrawable.setAlpha(0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            NotifyingScrollFragment fragment = new NotifyingScrollFragment();
            fragment.setActionBar(actionBar);
            fragment.setActionBarBackgroundDrawable(mActionBarBackgroundDrawable);
            transaction.replace(com.k11.bethany.R.id.content_fragment, fragment);
            transaction.commit();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.k11.bethany.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.k11.bethany.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}