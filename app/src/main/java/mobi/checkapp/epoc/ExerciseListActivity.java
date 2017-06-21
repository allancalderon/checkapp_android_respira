package mobi.checkapp.epoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;

import mobi.checkapp.epoc.utils.Constants;

public class ExerciseListActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private ViewPager viewPager = null;

    private ExerciseListPagerAdapter adapter = null;
    private TabLayout tabLayout;

    //variables
    private FloatingActionButton fabExerciseActivity;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exercise);
        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list_exercise_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Lista de Ejercicios");
        //tab setup
        tabLayout = (TabLayout) findViewById(R.id.tab_list_exercise_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ExerciseListTabResume)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ExerciseListTabRecommend)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ExerciseListTabMyExercise)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.ExerciseListTabFavorites)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.activity_list_exercise_viewpager);
        adapter = new ExerciseListPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        //setup float action button
        fabExerciseActivity = (FloatingActionButton) findViewById(R.id.fabExerciseActivity);
        fabExerciseActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ExerciseEditActivity.class);
                startActivityForResult(i, Constants.EXERCISEREQCODEACTIVITY);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.d(TAG, "Back button clicked");
            super.onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.EXERCISEREQCODEACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //updateFragments();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
    @Override
    public void updateFragments() {
        int currentTab = viewPager.getCurrentItem();
        adapter.notifyDataSetChanged();
        //adapter = new ExerciseListPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentTab);
    }
     */

    /**
    @Override
    public void OnResume() {
        super.onResume();
        //updateFragments();
    }
    */

}
