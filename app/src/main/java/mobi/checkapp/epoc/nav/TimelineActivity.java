package mobi.checkapp.epoc.nav;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.utils.CacheMainActivity;

public class TimelineActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();

    private TimelineSectionsPagerAdapter mTimelineSectionsPagerAdapter;
    private CircularViewPagerHandler circularViewPagerHandler;
    //private List<Exercises> listItems;
    private List<String> keys;
    //private LinkedHashMap<String, List<Exercises>> hashListItems;

    private Button btnPttPrevMonth;
    private Button btnPttNextMonth;
    private TextView currentPageText;
    private int currentPosition = 0;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnPttPrevMonth = (Button) findViewById(R.id.btnPttPrevMonth);
        btnPttNextMonth = (Button) findViewById(R.id.btnPttNextMonth);
        currentPageText = (TextView) findViewById(R.id.textMonthTimeline);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        keys = new ArrayList(CacheMainActivity.hashListItems.keySet());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.activity_timeline_viewpager);
        mTimelineSectionsPagerAdapter = new TimelineSectionsPagerAdapter(this, getFragmentManager(), CacheMainActivity.hashListItems);
        viewPager.setAdapter(mTimelineSectionsPagerAdapter);
        circularViewPagerHandler = new CircularViewPagerHandler(viewPager);
        viewPager.addOnPageChangeListener(circularViewPagerHandler);
        circularViewPagerHandler.setOnPageChangeListener(createOnPageChangeListener());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.activity_timeline_viewpager);
        mViewPager.setAdapter(mTimelineSectionsPagerAdapter);

        //listeners
        currentPageText.setText(keys.get(0));
        mViewPager.setCurrentItem(1, false);//getItem(-1) for previous


        btnPttPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(getItem(-1), true);//getItem(-1) for previous
            }
        });
        btnPttNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(getItem(1), true);//getItem(-1) for previous

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_timeline_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private int getItem(int i) {
        int pos =mViewPager.getCurrentItem()+i;
        if(pos > CacheMainActivity.hashListItems.size())
            pos = 1;
        if(pos <= 0 )
            pos = CacheMainActivity.hashListItems.size();
        return pos;
    }

    private ViewPager.OnPageChangeListener createOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            int position;
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if(positionOffset==0){
                    if(position >=0 && position < keys.size()) {
                        currentPageText.setText(keys.get(position));
                    }
                }
            }
            @Override
            public void onPageSelected(final int position) {
            }
            @Override
            public void onPageScrollStateChanged(final int state) {
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds listItems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }


    class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
        public static final int SET_ITEM_DELAY = 300;
        private ViewPager mViewPager;
        private ViewPager.OnPageChangeListener mListener;

        public CircularViewPagerHandler(final ViewPager viewPager) {
            mViewPager = viewPager;
            mViewPager.setCurrentItem(1, false);
        }

        public void setOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
            mListener = listener;
        }

        @Override
        public void onPageSelected(final int position) {
            handleSetCurrentItemWithDelay(position);
            invokeOnPageSelected(position);
        }

        private void handleSetCurrentItemWithDelay(final int position) {
            mViewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handleSetCurrentItem(position);
                }
            }, SET_ITEM_DELAY);
        }

        private void handleSetCurrentItem(final int position) {
            final int lastPosition = mViewPager.getAdapter().getCount() - 1;
            if(position == 0) {
                mViewPager.setCurrentItem(lastPosition - 1, false);
            } else if(position == lastPosition) {
                mViewPager.setCurrentItem(1, false);
            }
        }

        private void invokeOnPageSelected(final int position) {
            if(mListener != null) {
                mListener.onPageSelected(position - 1);
            }
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            invokeOnPageScrollStateChanged(state);
        }

        private void invokeOnPageScrollStateChanged(final int state) {
            if(mListener != null) {
                mListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            invokeOnPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        private void invokeOnPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            if(mListener != null) {
                mListener.onPageScrolled(position - 1, positionOffset, positionOffsetPixels);
            }
        }
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
