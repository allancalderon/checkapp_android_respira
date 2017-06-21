package mobi.checkapp.epoc.component;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import mobi.checkapp.epoc.ExerciseActivity;
import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.utils.CacheMainActivity;
import mobi.checkapp.epoc.utils.Constants;

/**
 * Created by luisbanon on 30/05/16.
 */
public class ListFavItem extends LinearLayout {

    public LinearLayout linearLayout;
    public LinearLayout lnEvent;
    public Exercises excersice;
    public TextView txtTittle;
    public TextView txtDescription;
    public Button btnFav;
    public Context _context;

    public ListFavItem(Context context, Exercises date) {
        super(context);
        this._context = context;
        this.excersice = date;
        inflate();
    }

    public void inflate(){
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout = new LinearLayout(getContext());
        layoutInflater.inflate(R.layout.list_fav_item, this);
        this.txtTittle = (TextView)findViewById(R.id.txtTittle);
        //this.txtDescription = (TextView)findViewById(R.id.txtDescription);
        this.btnFav = (Button)findViewById(R.id.btnFav);
        this.lnEvent = (LinearLayout)findViewById(R.id.lnEvent);

        this.txtTittle.setText(excersice.getTitle());
        //this.txtDescription.setText(excersice.getDescription());

        this.btnFav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheMainActivity.favorites.add(excersice);
            }
        });

        this.lnEvent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                i = new Intent(_context, ExerciseActivity.class);
                if(excersice != null && i != null) {
                    Bundle exerciseInfo = new Bundle();
                    exerciseInfo.putSerializable(Constants.LISTEXERCISEDATA, excersice);
                    i.putExtra(Constants.LISTEXERCISEBUNDLE, exerciseInfo);
                    _context.startActivity(i);
                }else{
                    // Prompt user to enter credentials
                    Toast.makeText(_context,
                            "Error recovering information. Please, try again!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }



}
