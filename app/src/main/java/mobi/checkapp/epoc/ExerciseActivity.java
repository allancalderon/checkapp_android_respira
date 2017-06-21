package mobi.checkapp.epoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.List;

import mobi.checkapp.epoc.db.DbFunctions;
import mobi.checkapp.epoc.db.activitiesContract;
import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.utils.Constants;

public class ExerciseActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener,View.OnClickListener,
        RatingBar.OnRatingBarChangeListener {
    //general
    private final String TAG = this.getClass().getName();
    private Context context;
    private Exercises exercises;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DbFunctions dbFunctions;
    private ArrayAdapter ListViewAdapter;
    String[] activitiesList = null;
    private FloatingActionButton fabExerciseActivity;
    private ImageButton btnExerciseAdd, btnExerciseEdit, btnExerciseDelete, btnExerciseFavorite, btnExerciseShare;
    //youtube video
    private YouTubePlayerFragment youTubePlayerFragment;
    private String VIDEO_ID="";
    //icon
    private Drawable iconDrawable;
    private int colorId;
    private int iconImage;
    //temporary, for testing
    private LinearLayout linearLayout;
    private TextView textExerciseDescription;
    private TextView textExerciseTitle;
    private RatingBar ratingBarExercise;
    private ListView listViewExercise;
    boolean isChanged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        //Toolbar definitions
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //other
        exercises = null;
        dbFunctions = new DbFunctions(this);
        Bundle extra;
        Intent returnIntent = new Intent();
        context = getApplicationContext();
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutExerciseYouTube);
        listViewExercise = (ListView) findViewById(R.id.listViewExercise);
        textExerciseDescription = (TextView) findViewById(R.id.textExerciseDescription);
        textExerciseTitle = (TextView) findViewById(R.id.textExerciseTitle);
        ratingBarExercise = (RatingBar) findViewById(R.id.ratingBarExercise);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        fabExerciseActivity = (FloatingActionButton) findViewById(R.id.fabExerciseActivity);
        //button definitions
        btnExerciseAdd = (ImageButton) findViewById(R.id.btnExerciseAdd);
        btnExerciseEdit = (ImageButton) findViewById(R.id.btnExerciseEdit);
        btnExerciseDelete = (ImageButton) findViewById(R.id.btnExerciseDelete);
        btnExerciseFavorite = (ImageButton) findViewById(R.id.btnExerciseFavorite);
        btnExerciseShare = (ImageButton) findViewById(R.id.btnExerciseShare);
        //receive result from preview Activity
        extra = this.getIntent().getBundleExtra(Constants.LISTEXERCISEBUNDLE);
        if(extra != null) {
            exercises = (Exercises) extra.getSerializable(Constants.LISTEXERCISEDATA);
            //get updated data
            if (exercises != null) {
                List<Exercises> tmpExerciseList = dbFunctions.queryExerciseList(
                        null,
                        null,
                        activitiesContract.ExerciseEntry._ID + "=?",
                        new String[]{String.valueOf(exercises.getIdExercises())}
                );
                if(tmpExerciseList.size() == 1)
                    exercises = tmpExerciseList.get(0);
                updateText(exercises);
            }else{
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }
        //Refer activity exercise for you tube video
        if(VIDEO_ID!=null) {
            if (!VIDEO_ID.isEmpty()) {
                youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                        .findFragmentById(R.id.fragmentExerciseYouTube);
                //new LoadViewTask(ExerciseActivity.this,"Loading exercise information...").execute();
                //setup video stream id for youtube
                youTubePlayerFragment.initialize(Constants.GOOGLE_KEY, ExerciseActivity.this);
            } else
                linearLayout.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }
        setupListView();
        ratingBarExercise.setOnRatingBarChangeListener(this);
        setupButtons();
    }

    private void setupButtons() {
        //Edit button
        if(exercises != null && exercises.getSourceType().equals(Constants.SOURCERECOMMENDATEDEJERCISEXML)) {
            //int iconImageEdit = R.drawable.ic_mode_edit_white_24dp;
            int colorImageEdit = getResources().getColor(R.color.colorButton);
            //Drawable iconDrawableEdit = context.getResources().getDrawable(iconImageEdit);
            //iconDrawableEdit.setColorFilter(context.getResources().getColor(colorId), PorterDuff.Mode.SRC_ATOP);
            //btnExerciseEdit.setImageDrawable(iconDrawableEdit);
            btnExerciseEdit.setBackgroundColor(colorImageEdit);
        }
        //favorite button
        if (exercises.isFavorite()) {
            iconImage = R.drawable.ic_star_white_36dp;
        } else {
            iconImage = R.drawable.ic_star_border_white_36dp;
        }
        iconDrawable = context.getResources().getDrawable(iconImage);
        iconDrawable.setColorFilter(context.getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        btnExerciseFavorite.setImageDrawable(iconDrawable);
        btnExerciseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AssignTimelineExerciseActivity.class);
                if(exercises != null && i != null) {
                    exercises.setRatio(ratingBarExercise.getRating());
                    Bundle exerciseInfo = new Bundle();
                    exerciseInfo.putSerializable(Constants.LISTEXERCISEDATA, exercises);
                    i.putExtra(Constants.LISTEXERCISEBUNDLE, exerciseInfo);
                    startActivityForResult(i,Constants.EXERCISEACTIVITYREQCODEADD);
                }

            }
        });
        btnExerciseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exercises != null && !exercises.getSourceType().equals(Constants.SOURCERECOMMENDATEDEJERCISEXML)) {
                    Intent i = new Intent(getApplicationContext(), ExerciseEditActivity.class);
                    exercises.setRatio(ratingBarExercise.getRating());
                    Bundle exerciseInfo = new Bundle();
                    exerciseInfo.putSerializable(Constants.LISTEXERCISEDATA, exercises);
                    i.putExtra(Constants.LISTEXERCISEBUNDLE, exerciseInfo);
                    startActivityForResult(i,Constants.EXERCISEACTIVITYREQCODEEDIT);
                }
            }
        });
        btnExerciseDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ExerciseDeleteActivity.class);
                if(exercises != null && i != null) {
                    Bundle exerciseInfo = new Bundle();
                    exerciseInfo.putSerializable(Constants.LISTEXERCISEDATA, exercises);
                    i.putExtra(Constants.LISTEXERCISEBUNDLE, exerciseInfo);
                    startActivityForResult(i,Constants.EXERCISEACTIVITYREQCODEDELETE);
                }
            }
        });
        btnExerciseFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exercises!=null) {
                    if (exercises.isFavorite()) {
                        exercises.setFavorite(false);
                        iconImage = R.drawable.ic_star_border_white_36dp;
                    } else {
                        exercises.setFavorite(true);
                        iconImage = R.drawable.ic_star_white_36dp;
                    }
                    long resultDb = dbFunctions.updateExercise(exercises);
                    if(resultDb > 0) {
                        isChanged = true;
                        iconDrawable = context.getResources().getDrawable(iconImage);
                        iconDrawable.setColorFilter(context.getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
                        btnExerciseFavorite.setImageDrawable(iconDrawable);
                    }
                }
            }
        });
        btnExerciseShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ExerciseShareActivity.class);
                if(exercises != null && i != null) {
                    Bundle exerciseInfo = new Bundle();
                    exerciseInfo.putSerializable(Constants.LISTEXERCISEDATA, exercises);
                    i.putExtra(Constants.LISTEXERCISEBUNDLE, exerciseInfo);
                    startActivityForResult(i,Constants.EXERCISEACTIVITYREQCODE);
                }
            }
        });

        //Start controller for gathering information in background
        fabExerciseActivity.setOnClickListener(this);
    }

    private void updateListView() {
        activitiesList = null;
        setupListView();
        if(ListViewAdapter!=null)
            ListViewAdapter.notifyDataSetChanged();
    }

    private void setupListView() {
        activitiesList = new String[] { "Apple", "Banana", "Mango", "Grapes"};
        //ListView adapter
        ListViewAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, activitiesList);

        // Assign adapter to ListView
        listViewExercise.setAdapter(ListViewAdapter);
        listViewExercise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue    = (String) listViewExercise.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();
            }
        });
        setListViewHeightBasedOnChildren();
    }

    private void setListViewHeightBasedOnChildren() {
        if (ListViewAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < ListViewAdapter.getCount(); i++) {
            View listItem = ListViewAdapter.getView(i, null, listViewExercise);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listViewExercise.getLayoutParams();
        params.height = totalHeight + (listViewExercise.getDividerHeight() * (ListViewAdapter.getCount() - 1));
        listViewExercise.setLayoutParams(params);
        listViewExercise.requestLayout();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating,
                                boolean fromUser) {
        if(exercises != null && fromUser){
            exercises.setRatio(rating);
            long resultDb = dbFunctions.updateExercise(exercises);
            isChanged = true;
        }
    }

    private void updateText(Exercises updatedExercise) {
        exercises.setIdGroupFK(updatedExercise.getIdGroupFK());
        exercises.setIdSubGroupFK(updatedExercise.getIdSubGroupFK());
        exercises.setIdTypeExerciseFK(updatedExercise.getIdTypeExerciseFK());
        exercises.setDescription(updatedExercise.getDescription());
        exercises.setRatio(updatedExercise.getRatio());
        exercises.setTitle(updatedExercise.getTitle());
        exercises.setOtherInfo1(updatedExercise.getOtherInfo1());
        exercises.setOtherInfo2(updatedExercise.getOtherInfo2());
        exercises.setOtherInfo3(updatedExercise.getOtherInfo3());
        exercises.setOtherInfo4(updatedExercise.getOtherInfo4());
        if(updatedExercise.getDescription()!=null)
            textExerciseDescription.setText(Html.fromHtml(updatedExercise.getDescription()));
        ratingBarExercise.setRating(updatedExercise.getRatio());
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.exerciseActivity_title));
        textExerciseTitle.setText(updatedExercise.getTitle());
        VIDEO_ID = updatedExercise.getUrlVideo();
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        /** add listeners to YouTubePlayer instance **/
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!b) {
            youTubePlayer.cueVideo(VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failured to initialize video.", Toast.LENGTH_LONG).show();
    }

    private PlaybackEventListener playbackEventListener = new PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private PlayerStateChangeListener playerStateChangeListener = new PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //if(isChanged) {
                Intent returnIntent = new Intent();
                Bundle extra = this.getIntent().getBundleExtra(Constants.LISTEXERCISEBUNDLE);
                if(extra != null) {
                    exercises = (Exercises) extra.getSerializable(Constants.LISTEXERCISEDATA);
                    returnIntent.putExtra(Constants.EXERCISEDATA, exercises);
                    setResult(Activity.RESULT_OK,returnIntent);
                }
            //}
            Log.d(TAG, "Back button clicked");
            super.onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        //if(isChanged) {
            Intent returnIntent = new Intent();
            Bundle extra = this.getIntent().getBundleExtra(Constants.LISTEXERCISEBUNDLE);
            if(extra != null) {
                exercises = (Exercises) extra.getSerializable(Constants.LISTEXERCISEDATA);
                returnIntent.putExtra(Constants.EXERCISEDATA, exercises);
                setResult(Activity.RESULT_OK,returnIntent);
            }
        //}
        super.onBackPressed();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Exercises exercise;
        if (requestCode == Constants.EXERCISEACTIVITYREQCODEDELETE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Intent returnIntent = new Intent();
                Bundle extra = this.getIntent().getBundleExtra(Constants.LISTEXERCISEBUNDLE);
                if(extra != null) {
                    exercises = (Exercises) extra.getSerializable(Constants.LISTEXERCISEDATA);
                    returnIntent.putExtra(Constants.EXERCISEDATA, exercises);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        } else if (requestCode == Constants.EXERCISEACTIVITYREQCODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Bundle exerciseInfo = data.getExtras();
                if(exerciseInfo != null) {
                    exercise = (Exercises) exerciseInfo.getSerializable(Constants.EXERCISEDATA);
                    if (exercise != null) {
                        //notifyAll();
                    }
                }
            }
        } else if (requestCode == Constants.EXERCISEACTIVITYREQCODEEDIT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Bundle exerciseInfo = data.getExtras();
                if(exerciseInfo != null) {
                    exercise = (Exercises) exerciseInfo.getSerializable(Constants.EXERCISEDATA);
                    if (exercise != null) {
                        updateText(exercise);
                    }
                }
            }
        }else if (requestCode == Constants.EXERCISEACTIVITYREQCODEADD) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Bundle exerciseInfo = data.getExtras();
                if(exerciseInfo != null) {
                    exercise = (Exercises) exerciseInfo.getSerializable(Constants.EXERCISEDATA);
                    if (exercise != null) {
                        //TODO:update list view
                        updateListView();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent i;
        switch (id) {
            case R.id.fabExerciseActivity:
                i = new Intent(this, AssignTimelineExerciseActivity.class);
                startActivity(i);
                Log.d(TAG, "Assign Exercise for Activity");
                break;
            default:
                break;
        }
    }
}