package mobi.checkapp.epoc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import mobi.checkapp.epoc.utils.Constants;

/**
 * Created by allancalderon on 30/05/16.
 */
public class ExerciseListPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    ExerciseListTabFragment0 tab0;
    ExerciseListTabFragment1 tab1;
    ExerciseListTabFragment2 tab2;
    //ExerciseListTabFragment3 tab3;
    ExerciseListTabFragment4 tab4;

    public ExerciseListPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        tab0 = new ExerciseListTabFragment0();
        tab1 = new ExerciseListTabFragment1();
        tab2 = new ExerciseListTabFragment2();
        //tab3 = new ExerciseListTabFragment3();
        tab4 = new ExerciseListTabFragment4();
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.EXERCISELISTTABRESUME:
                //ExerciseListTabFragment0 tab0= new ExerciseListTabFragment0();
                return tab0;
            case Constants.EXERCISELISTTABRECOMMEND:
                //ExerciseListTabFragment1 tab1 = new ExerciseListTabFragment1();
                return tab1;
            case Constants.EXERCISELISTTABMYEXERCISE:
                //ExerciseListTabFragment2 tab2 = new ExerciseListTabFragment2();
                return tab2;
            /**
            case Constants.EXERCISELISTTABFAVORITES:
                //ExerciseListTabFragment3 tab3 = new ExerciseListTabFragment3();
                return tab3;
             */
            case Constants.EXERCISELISTTABTIMELINE:
                //ExerciseListTabFragment3 tab3 = new ExerciseListTabFragment3();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
