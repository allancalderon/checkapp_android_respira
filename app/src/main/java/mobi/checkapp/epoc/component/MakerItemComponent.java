package mobi.checkapp.epoc.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.entities.MarkerItem;
import mobi.checkapp.epoc.utils.Constants;

/**
 * Created by luisbanon on 30/05/16.
 */
public class MakerItemComponent extends LinearLayout {

    public LinearLayout linearLayout;
    public MarkerItem marker;
    public TextView txtTittle;
    public TextView btnFav;
    public Context _context;

    public MakerItemComponent(Context context, MarkerItem date) {
        super(context);
        this._context = context;
        this.marker = date;
        inflate();
    }

    public void inflate(){
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout = new LinearLayout(getContext());
        layoutInflater.inflate(R.layout.marker_item, this);

        this.txtTittle = (TextView)findViewById(R.id.txtMarkerTittle);
        this.txtTittle.setText(marker.Nombre);
        this.btnFav = (TextView)findViewById(R.id.btnValue);
        btnFav.setText(marker.nValue);
        if(marker.value >= 1 && marker.value <= 5 )
            btnFav.setBackgroundColor(Constants.colorsPollutantsLevel[marker.value]);
    }



}
