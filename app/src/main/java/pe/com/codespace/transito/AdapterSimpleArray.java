package pe.com.codespace.transito;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Carlos on 09/04/2014.
 */
public class AdapterSimpleArray extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public AdapterSimpleArray(Context context, String[] values) {
        super(context, R.layout.single_item_twoline,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_item_twoline, viewGroup, false);
        TextView textView1 = (TextView) row.findViewById(R.id.tvTitleItem);
        TextView textView2 = (TextView) row.findViewById(R.id.tvTextItem);
        textView1.setText(values[0]);
        textView2.setText(values[1]);
        return row;
    }
}
