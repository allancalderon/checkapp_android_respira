package mobi.checkapp.epoc.adapter;

import android.view.View;
import android.widget.PopupMenu;

/**
 * Created by allancalderon on 04/06/16.
 */
public interface OnElItemClickListener extends PopupMenu.OnMenuItemClickListener {
    public void onElItemClicked(int listPosition, int expandedListPosition, int layoutId, View view);
}
