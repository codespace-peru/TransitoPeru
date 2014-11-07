package pe.com.codespace.transito;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Carlos on 04/04/2014.
 */
public class AdapterImageMenu extends BaseAdapter {

    private List<Item> items = new ArrayList<Item>();
    private LayoutInflater inflater;

    public AdapterImageMenu(Context ctx){
        inflater = LayoutInflater.from(ctx);
        items.add(new Item("Reglamento de Tránsito", R.drawable.corvette));
        items.add(new Item("Reglamento de Licencias de Conducir", R.drawable.crowler));
        items.add(new Item("Reglamento de Vehículos", R.drawable.newbeatle));
        items.add(new Item("Señales de Tránsito", R.drawable.newmini));
        items.add(new Item("Consultas Online", R.drawable.globe));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).drawableId;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;
        if(v == null){
            v = inflater.inflate(R.layout.menu_item,viewGroup, false);
            v.setTag(R.id.grid_item_image, v.findViewById(R.id.grid_item_image));
            v.setTag(R.id.grid_item_label, v.findViewById(R.id.grid_item_label));
        }
        picture = (ImageView) v.getTag(R.id.grid_item_image);
        name = (TextView) v.getTag(R.id.grid_item_label);
        Item item = (Item) getItem(pos);
        picture.setImageResource(item.drawableId);
        name.setText(item.name);
//      v.setBackgroundColor(Color.CYAN);
        return v;
    }

    private class Item{
        final String name;
        final int drawableId;
        Item(String name, int drawableId){
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}
