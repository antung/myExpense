package com.antang.myexpense;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.antang.myexpense.ui.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = Config.TAG_PREFIX + "MainActivity";
    private static final boolean DEBUG = Config.DEBUG;

    private AppBarConfiguration mAppBarConfiguration;
    private FloatingActionButton mFab;
    private NavController mNavController;
    private TextView mBtnAction;
    private ImageView mBtnLogo;
    private TextView mTvName;
    private BaseFragment mCurrentFragment;
    InputMethodManager mIm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener((View v) -> {
//            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
            mNavController.navigate(R.id.nav_edit_expense);
        });

        mTvName = findViewById(R.id.tv_title_bar_name);
        mBtnAction = findViewById(R.id.btn_title_bar_action);
        mBtnAction.setOnClickListener((v) -> {
            if (mCurrentFragment != null) {
                mCurrentFragment.onActionButtonClicked();
            }
        });
        mBtnLogo = findViewById(R.id.btn_title_bar_logo);
        mBtnLogo.setOnClickListener((v) -> {
            boolean handled = false;
            if (mCurrentFragment != null) {
                handled = mCurrentFragment.onLogoButtonClicked();
            }
            if (!handled) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_edit_expense, R.id.nav_edit_account,
                R.id.nav_view_expense, R.id.nav_sync, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, mNavController);
        mNavController.addOnDestinationChangedListener((NavController controller,
                                                        NavDestination destination,
                                                        Bundle arguments) -> {
            if (DEBUG) {
                Log.d(TAG, "onDestinationChanged id " + destination.getId() + "==" + (destination.getId() == R.id.nav_home));
            }
            if (destination.getId() == R.id.nav_home) {
                mFab.show();
                mTvName.setVisibility(View.VISIBLE);
                mBtnAction.setVisibility(View.GONE);
                mBtnLogo.setImageResource(R.mipmap.ic_launcher);
            } else {
                mFab.hide();
                mTvName.setVisibility(View.GONE);
                mBtnAction.setVisibility(View.VISIBLE);
                mBtnLogo.setImageResource(R.drawable.ic_back);
            }
            hideKeyboardIfNeeded();
        });

        // monitor current active fragment to dispatch Action button and Logo button click event
        // such Save, Back
        getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).
                getChildFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                mCurrentFragment = (BaseFragment)f;
                mCurrentFragment.onConfigActionButton(mBtnAction);
                mCurrentFragment.onConfigLogoButton(mBtnLogo);
            }
        }, true);

        // we may support later bottom navigation bar
        // BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view);
        // NavigationUI.setupWithNavController(bottomNavView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void hideKeyboardIfNeeded() {
        if (mIm.isActive()) {
            mIm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    // NOT work, FW bug?  why HomeFragment mFragmentId is nav_host_fragment?
//    private BaseFragment getCurrentActiveFragment(int currentFragmentId) {
//        Fragment hostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//        return (BaseFragment)hostFragment.getChildFragmentManager().
//                findFragmentById(mCurrentFragmentId);
//        // either way getFragmentManager().getPrimaryNavigationFragment (API 26)
//        // or register registerFragmentLifecycleCallbacks to track curent fragment
//    }
}
