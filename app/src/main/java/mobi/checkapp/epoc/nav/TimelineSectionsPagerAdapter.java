package mobi.checkapp.epoc.nav;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import mobi.checkapp.epoc.entities.Exercises;

/**
 * Created by allancalderon on 29/05/16.
 */
public class TimelineSectionsPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private LinkedHashMap<String, List<Exercises>> mHashItems;

    public TimelineSectionsPagerAdapter(final Context context, final FragmentManager fragmentManager, final LinkedHashMap<String, List<Exercises>> hashItems) {
        super(fragmentManager);
        mContext = context;
        mHashItems = hashItems;
    }

    protected Fragment getFragmentForItem(final List<Exercises> mListExercises) {
        return TimelinePlaceholderFragment.instantiateWithArgs(mContext, mListExercises);
    }

    @Override
    public Fragment getItem(final int position) {
        int itemsSize = mHashItems.size();
        List keys = new ArrayList(mHashItems.keySet());

        if(position <= 0) {
            return getFragmentForItem(mHashItems.get(keys.get(itemsSize - 1)));
            //return getFragmentForItem(mHashItems.get(keys.get(0)));
        } else if(position == itemsSize + 1) {
            return getFragmentForItem(mHashItems.get(keys.get(0)));
            //return getFragmentForItem(mHashItems.get(keys.get(itemsSize - 1)));
        }else {
            return getFragmentForItem(mHashItems.get(keys.get(position - 1)));
        }
    }


    @Override
    public int getCount() {
        final int itemsSize = mHashItems.size();
        return itemsSize > 1 ? itemsSize + 2 : itemsSize;
    }

    public int getCountWithoutFakePages() {
        return mHashItems.size();
    }

    public void setItems(final LinkedHashMap<String,List<Exercises>> items) {
        mHashItems = items;
        notifyDataSetChanged();
    }
}

