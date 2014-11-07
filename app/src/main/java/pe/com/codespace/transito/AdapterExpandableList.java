package pe.com.codespace.transito;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Carlos on 23/11/13.
 */
public class AdapterExpandableList extends BaseExpandableListAdapter {
    private Context context;
    private List<String> _listHeader;
    private HashMap<String, List<String>> _listChild;

    public AdapterExpandableList(Context context, List<String> listHeader, HashMap<String, List<String>> listChild){
        this.context = context;
        this._listHeader = listHeader;
        this._listChild = listChild;
    }


    @Override
    public int getGroupCount() {
        return this._listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listChild.get(this._listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listChild.get(this._listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        View row = view;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.explistview_group_item, null);
        }
        String title = (String) getGroup(groupPosition);
        TextView textView1 = (TextView) row.findViewById(R.id.tvGroupExp);
        TextView textView2 = (TextView) row.findViewById(R.id.tvItemExp);
        textView1.setText("Código " + title);
        textView2.setVisibility(View.GONE);
        //row.setPadding(-30,0,0,0);
        return row;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        View row = view;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.explistview_group_item, null);
        }
        String text = (String) getChild(groupPosition, childPosition);
        TextView textView1 = (TextView) row.findViewById(R.id.tvGroupExp);
        TextView textView2 = (TextView) row.findViewById(R.id.tvItemExp);
        ///////myTextView.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
        textView2.setText(Html.fromHtml(text));
        textView1.setVisibility(View.GONE);
        //row.setPadding(20,0,0,0);
        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}




