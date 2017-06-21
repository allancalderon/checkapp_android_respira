package mobi.checkapp.epoc;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mobi.checkapp.epoc.db.DbFunctions;
import mobi.checkapp.epoc.entities.TimelineAssign;
import mobi.checkapp.epoc.utils.CacheMainActivity;

public class AssignTimelineAnnotationActivity extends Activity {
    private static final int ANNOTATION_TYPE_ASSIGN = 2;
    private final String TAG = this.getClass().getName();

    public Button play,stop,record, btnNewAnnotationSave, btnNewAnnotationCancel;
    private MediaRecorder myAudioRecorder = null;
    private String outputFile = null;
    public TextView textNewAnnotationDateTime, textNewAnnotationLocation;
    private int year, month, day, hour, minute;
    private Calendar calendar;
    public TextView editTextNewAnnotationDescription;
    private DbFunctions dbFunctions = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_annotation);

        dbFunctions = new DbFunctions(this);

        play=(Button)findViewById(R.id.button3);
        stop=(Button)findViewById(R.id.button2);
        record=(Button)findViewById(R.id.button);

        textNewAnnotationDateTime = (TextView)findViewById(R.id.textNewAnnotationDateTime);
        textNewAnnotationLocation = (TextView)findViewById(R.id.textNewAnnotationLocation);

        btnNewAnnotationSave = (Button)findViewById(R.id.btnNewAnnotationSave);
        btnNewAnnotationCancel = (Button)findViewById(R.id.btnNewAnnotationCancel);
        editTextNewAnnotationDescription = (TextView)findViewById(R.id.editTextNewAnnotationDescription);

        stop.setEnabled(false);
        play.setEnabled(false);

        try{
            myAudioRecorder=new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);


            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddhhmmss");
                    String date = dt.format(new Date());
                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + date + ".mp3";

                    myAudioRecorder.setOutputFile(outputFile);
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    record.setEnabled(false);
                    stop.setEnabled(true);

                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;

                    stop.setEnabled(false);
                    play.setEnabled(true);

                    Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                    MediaPlayer m = new MediaPlayer();

                    try {
                        m.setDataSource(outputFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        m.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    m.start();
                    Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


        btnNewAnnotationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNewAnnotationSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimelineAssign timelineAssign = new TimelineAssign();
                timelineAssign.setIdTypeAssignment(ANNOTATION_TYPE_ASSIGN);
                timelineAssign.setIdExerciseFK(0);
                timelineAssign.setDay(day);
                timelineAssign.setMonth(month+1);
                timelineAssign.setYear(year);
                timelineAssign.setHour(hour);
                timelineAssign.setMinute(minute);
                if(CacheMainActivity.DirectionLatLng != null) {
                    timelineAssign.setLatitude(CacheMainActivity.DirectionLatLng.latitude);
                    timelineAssign.setLongitude(CacheMainActivity.DirectionLatLng.longitude);
                }
                timelineAssign.setDescription(String.valueOf(editTextNewAnnotationDescription.getText()));
                if(outputFile != null) {
                    timelineAssign.setAudioFile(outputFile);
                }
                if(outputFile != null || editTextNewAnnotationDescription.getText().length() > 0){
                    long result = dbFunctions.addTimelineAssign(timelineAssign);
                    if(result > 0)
                        finish();
                    else
                        Toast.makeText(AssignTimelineAnnotationActivity.this,
                                "Error saving",
                                Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AssignTimelineAnnotationActivity.this,
                            getResources().getString(R.string.assign_timeline_mesage_empty_text)+
                                    String.valueOf(editTextNewAnnotationDescription.getText()),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

        textNewAnnotationDateTime.setText(String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year) );

        textNewAnnotationDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

        textNewAnnotationLocation.setText(CacheMainActivity.Direction);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            year = arg1;
            month = arg2;
            day = arg3;

            textNewAnnotationDateTime.setText(String.valueOf(arg3)+"/"+String.valueOf(arg2+1)+"/"+String.valueOf(arg1) );
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
