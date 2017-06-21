package mobi.checkapp.epoc.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.entities.Activity;
import mobi.checkapp.epoc.entities.Day;

/**
 * Created by luisbanon on 30/05/16.
 */
public class DayComponent extends LinearLayout {

    public LinearLayout linearLayout;
    public Day day;
    public int year;
    public int month;
    public TextView txtDay;
    public TextView txtWeekDay;
    public LinearLayout lnActivity;
    public Context _context;

    public DayComponent(Context context, int year, int month, Day date) {
        super(context);
        this._context = context;
        this.day = date;
        this.year = year;
        this.month = month;
        inflate();
    }

    public void inflate() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout = new LinearLayout(getContext());
        layoutInflater.inflate(R.layout.day_component_layout, this);
        this.txtDay = (TextView)findViewById(R.id.txtDay);
        this.txtWeekDay = (TextView)findViewById(R.id.txtWeekDay);
        this.lnActivity = (LinearLayout)findViewById(R.id.lnActivity);

        this.txtDay.setText(String.valueOf(day.day));

        Calendar c = Calendar.getInstance();
        c.set(year, month-1, day.day, 0, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE");
        String finalDate = dateFormat.format(c.getTime());
        this.txtWeekDay.setText(finalDate);

        lnActivity.removeAllViews();

        for(Activity activity: day.listActivities){

            ActivityComponent component = new ActivityComponent(_context,activity);

            /**
            component.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(_context, ExerciseEditActivity.class);

                    ((MainActivity) _context).startActivityForResult(i, Constants.EXERCISEREQCODE);
                }
            });
             */
            lnActivity.addView(component);

        }


    }



}
