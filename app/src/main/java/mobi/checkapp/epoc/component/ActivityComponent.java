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
import mobi.checkapp.epoc.entities.Activity;
import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.utils.CacheMainActivity;
import mobi.checkapp.epoc.utils.Constants;

/**
 * Created by luisbanon on 30/05/16.
 */
public class ActivityComponent extends LinearLayout {

    public LinearLayout linearLayout;
    public Activity activity;
    public TextView txtTitulo;
    public TextView txtDescription;
    public Context _context;

    public ActivityComponent(Context context, Activity date) {
        super(context);
        this._context = context;
        this.activity = date;
        inflate();
    }

    public void inflate(){
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout = new LinearLayout(getContext());
        layoutInflater.inflate(R.layout.activity_component, this);
        this.txtTitulo = (TextView)findViewById(R.id.txtTitulo);
        this.txtDescription = (TextView)findViewById(R.id.txtDescription);

        this.txtTitulo.setText(activity.titulo_actividad);
        this.txtDescription.setText(activity.cuerpo_actividad);

    }



}
