package mobi.checkapp.epoc.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.entities.Exercises;
import mobi.checkapp.epoc.utils.Constants;

public class ELExerciseAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableDataHeader;
    private HashMap<String, List<Exercises>> expandableDataChild;
    //private SQLiteHandlerPatient dbPatient;
    private List<Exercises> treatmentList;
    private int expandGroupView;
    public OnElItemClickListener mListener;

    public ELExerciseAdapter(Context context, List<String> expandableDataHeader,
                             HashMap<String, List<Exercises>> expandableDataChild) {
        this.context = context;
        this.expandableDataHeader = expandableDataHeader;
        this.expandableDataChild = expandableDataChild;
        expandGroupView = 1;
    }

    @Override
    public Object getChild(int listPosition, int expandableDataHeader) {
        return this.expandableDataChild.get(this.expandableDataHeader.get(listPosition)).get(expandableDataHeader);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Exercises expandableText = (Exercises) getChild(listPosition, expandedListPosition);
        Drawable iconDrawable;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_list_exercise_fragment_body, null);
        }
        LinearLayout linearLayoutListExerciseText = (LinearLayout) convertView
                .findViewById(R.id.linearLayoutListExerciseText);
        TextView txtListExerciseBody = (TextView) convertView
                .findViewById(R.id.txtListExerciseBody);
        TextView txtListExerciseBodyDesc = (TextView) convertView
                .findViewById(R.id.txtListExerciseBodyDesc);
        TextView txtListExerciseBodyDesc2 = (TextView) convertView
                .findViewById(R.id.txtListExerciseBodyDesc2);
        View viewListExerciseBodyLine = convertView
                .findViewById(R.id.viewListExerciseBodyLine);
        ImageButton btnListExerciseBodyAdd = (ImageButton) convertView
                .findViewById(R.id.btnListExerciseBodyAdd);
        ImageButton btnListExerciseBodyMore = (ImageButton) convertView
                .findViewById(R.id.btnListExerciseBodyMore);
        ImageButton btnListExerciseFavorite = (ImageButton) convertView
                .findViewById(R.id.btnListExerciseFavorite);
        linearLayoutListExerciseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onElItemClicked(listPosition, expandedListPosition, R.id.linearLayoutListExerciseText, view);
            }
        });
        btnListExerciseBodyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onElItemClicked(listPosition, expandedListPosition, R.id.btnListExerciseBodyAdd, view);
            }
        });
        btnListExerciseBodyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onElItemClicked(listPosition, expandedListPosition, R.id.btnListExerciseBodyMore, view);
            }
        });
        btnListExerciseFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onElItemClicked(listPosition, expandedListPosition, R.id.btnListExerciseFavorite, view);
            }
        });
        //set data
        String text1 = expandableText.getTitle();
        String text2 = expandableText.getDescription();
        if(expandableText.getSourceType().equals(Constants.SOURCERECOMMENDATEDEJERCISEXML)) {
            text2 = expandableText.getOtherInfo1();
        }
        float ratio = expandableText.getRatio();
        String text3 = context.getResources().getString(R.string.exerciseAdapter_ratioNoInfo);
        if(ratio > 0) {
            text3 = context.getResources().getString(R.string.exerciseAdapter_ratio) + " " + ratio + "/5";
        }
        txtListExerciseBody.setText(text1);
        txtListExerciseBodyDesc.setText(text2);
        txtListExerciseBodyDesc2.setText(text3);
        //Buttons icons and color
        int iconId;
        //Button Add
        iconDrawable = context.getResources().getDrawable(R.drawable.ic_add_circle_outline_black_36dp);
        iconDrawable.setColorFilter(context.getResources().getColor(R.color.material_grey_600), PorterDuff.Mode.SRC_ATOP);
        btnListExerciseBodyAdd.setImageDrawable(iconDrawable);
        //Button More
        iconDrawable = context.getResources().getDrawable(R.drawable.ic_more_circle_outline_black_36dp);
        iconDrawable.setColorFilter(context.getResources().getColor(R.color.material_grey_600), PorterDuff.Mode.SRC_ATOP);
        btnListExerciseBodyMore.setImageDrawable(iconDrawable);
        //Button Favorites
        if(expandableText.isFavorite())
            iconId = R.drawable.ic_star_white_36dp;
        else
            iconId = R.drawable.ic_star_border_white_36dp;
        iconDrawable = context.getResources().getDrawable(iconId);
        iconDrawable.setColorFilter(context.getResources().getColor(R.color.material_grey_600), PorterDuff.Mode.SRC_ATOP);
        btnListExerciseFavorite.setImageDrawable(iconDrawable);

        if((getChildrenCount(listPosition) == (expandedListPosition+1)) && (getGroupCount() != (listPosition+1))) {
            viewListExerciseBodyLine.setVisibility(View.INVISIBLE);
        }else{
            viewListExerciseBodyLine.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableDataChild.get(this.expandableDataHeader.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableDataHeader.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableDataHeader.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_list_exercise_fragment_header, null);
        }
        TextView txtListExerciseHeader = (TextView) convertView
                .findViewById(R.id.txtListExerciseHeader);
        View viewListExerciseHeaderLine = convertView
                .findViewById(R.id.viewListExerciseHeaderLine);
        ImageView imageListExerciseHeader = (ImageView) convertView.findViewById(R.id.imageListExerciseHeader);
        if(listTitle!= null)
            txtListExerciseHeader.setText(listTitle);
        //icon
        Drawable icon;
        int iconId;
        if (isExpanded) {
            iconId = R.drawable.ic_expand_less_white_24dp;
        } else {
            iconId = R.drawable.ic_expand_more_white_24dp;
        }
        icon = convertView.getResources().getDrawable(iconId);
        icon.setColorFilter(convertView.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        imageListExerciseHeader.setImageDrawable(icon);
        if(listPosition == 0){
            viewListExerciseHeaderLine.setVisibility(View.INVISIBLE);
        }else{
            viewListExerciseHeaderLine.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }
}