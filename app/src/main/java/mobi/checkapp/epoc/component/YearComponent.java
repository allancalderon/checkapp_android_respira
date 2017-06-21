package mobi.checkapp.epoc.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;

import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.entities.Month;
import mobi.checkapp.epoc.entities.Year;

/**
 * Created by luisbanon on 30/05/16.
 */
public class YearComponent extends LinearLayout {

    public LinearLayout linearLayout;
    public Year year;
    public TextView txtYear;
    public LinearLayout lnMonths;
    public Context _context;

    public YearComponent(Context context, Year date) throws ParseException {
        super(context);
        this._context = context;
        this.year = date;
        inflate();
    }



    public void inflate() throws ParseException {
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout = new LinearLayout(getContext());
        layoutInflater.inflate(R.layout.year_component_layout, this);
        //this.txtYear = (TextView)findViewById(R.id.txtYear);
        this.lnMonths = (LinearLayout)findViewById(R.id.lnMonths);

        //this.txtYear.setText(String.valueOf(year.year));

        lnMonths.removeAllViews();

        for(Month month: year.listMonths){
            MonthComponent component = new MonthComponent(_context, this.year.year, month);
            lnMonths.addView(component);

        }


    }



}
