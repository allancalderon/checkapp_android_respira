package mobi.checkapp.epoc.nav;

/**
 * Created by allancalderon on 29/05/16.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.entities.Exercises;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimelinePlaceholderFragment extends Fragment {

    private static final String BUNDLE_KEY_TIMELINE_FRAGMENT = "bundle_key_timeline_fragment";
    private static Context mContext;
    private List<Exercises> mObject;

    public static TimelinePlaceholderFragment instantiateWithArgs(final Context context, final List<Exercises> mListExercises) {
        final TimelinePlaceholderFragment fragment = (TimelinePlaceholderFragment) instantiate(context, TimelinePlaceholderFragment.class.getName());
        final Bundle args = new Bundle();
        mContext = context;
        args.putSerializable(BUNDLE_KEY_TIMELINE_FRAGMENT, (Serializable) mListExercises);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        if(arguments != null) {
            List<Exercises> mListExercises = (List<Exercises>) arguments.getSerializable(BUNDLE_KEY_TIMELINE_FRAGMENT);
            mObject = mListExercises;
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_timeline_item_fragment, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.listViewTimelineFragment);
        ArrayAdapter<Exercises> arrayAdapter = new ArrayAdapter<Exercises>(
                mContext,
                android.R.layout.simple_list_item_1,
                mObject);
        listView.setAdapter(arrayAdapter);
        return view;
    }
}
