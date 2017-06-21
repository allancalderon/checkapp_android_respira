package mobi.checkapp.epoc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import mobi.checkapp.epoc.MapActivity;
import mobi.checkapp.epoc.R;
import mobi.checkapp.epoc.entities.MyItem;
import mobi.checkapp.epoc.utils.CacheMainActivity;

/**
 * Created by luisbanon on 31/05/16.
 */
public class MyPlacesDialog {

    public static Dialog dialog;

    public static void dialog(final Activity _context) {


        dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_places_dialog);

        final ListView lstMyplaces = (ListView) dialog
                .findViewById(R.id.lstPlaces);

        ArrayList<String>lista = new ArrayList<>();
        for(MyItem i: CacheMainActivity.myItems)
            lista.add(i.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(_context,
                android.R.layout.simple_list_item_1, android.R.id.text1, lista);

        lstMyplaces.setAdapter(adapter);

        lstMyplaces.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyItem i = CacheMainActivity.myItems.get(position);
                ((MapActivity)_context).zoomPlace(i.getPosition());
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
}
