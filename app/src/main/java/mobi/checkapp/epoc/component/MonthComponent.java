package mobi.checkapp.epoc.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.entities.Day;
import mobi.checkapp.epoc.entities.Month;
import mobi.checkapp.epoc.utils.Utils;

/**
 * Created by luisbanon on 30/05/16.
 */
public class MonthComponent extends LinearLayout {

    public LinearLayout linearLayout;
    public Month month;
    public int year;
    public TextView txtMonth;
    public LinearLayout lnDays;
    public Context _context;

    public MonthComponent(Context context, int year, Month date) {
        super(context);
        this._context = context;
        this.month = date;
        this.year = year;
        inflate();
    }

    public void inflate() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout = new LinearLayout(getContext());
        layoutInflater.inflate(R.layout.month_component_layout, this);
        this.txtMonth = (TextView)findViewById(R.id.txtMonth);
        this.lnDays = (LinearLayout)findViewById(R.id.lnDays);
        String monthFull = Utils.getMonth(this._context, Integer.valueOf(month.month)) + " " + String.valueOf(year);
        this.txtMonth.setText(String.valueOf(monthFull));

        lnDays.removeAllViews();

        for(Day day: month.listDays){
            DayComponent component = new DayComponent(_context, year, month.month, day);
            lnDays.addView(component);
        }
    }
}
