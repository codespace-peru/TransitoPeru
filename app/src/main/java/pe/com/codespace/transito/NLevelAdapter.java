package pe.com.codespace.transito;

import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Carlos on 20/04/2014.
 */
public class NLevelAdapter extends BaseAdapter {
    List<NLevelItem> list;
    List<NLevelListItem> filtered;

    public void setFiltered(ArrayList<NLevelListItem> filtered){
        this.filtered = filtered;
    }

    public NLevelAdapter(List<NLevelItem> list){
        this.list = list;
        this.filtered = filterItems();
    }


    @Override
    public int getCount() {
        return filtered.size();
    }

    @Override
    public NLevelListItem getItem(int i) {
        return filtered.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getItem(i).getView();
    }

    public NLevelFilter getFilter(){
        return new NLevelFilter();
    }

    class NLevelFilter{
        public void filter(){
            new AsyncFilter().execute();
        }
    }

    class AsyncFilter extends AsyncTask<Void,Void,ArrayList<NLevelListItem>>{
        @Override
        protected ArrayList<NLevelListItem> doInBackground(Void... voids) {
            return (ArrayList<NLevelListItem>) filterItems();
        }

        @Override
        protected void onPostExecute(ArrayList<NLevelListItem> result){
            setFiltered(result);
            NLevelAdapter.this.notifyDataSetChanged();
        }
    }

    public List<NLevelListItem> filterItems(){
        List<NLevelListItem> tempfiltered = new ArrayList<>();
        OUTER: for(NLevelListItem item:list){
            if(item.getParent()==null){
                tempfiltered.add(item);
            }else{
                NLevelListItem parent = item;
                while((parent=parent.getParent())!=null){
                    if(!parent.isExpanded()){
                        continue OUTER;
                    }
                }
                tempfiltered.add(item);
            }
        }
        return tempfiltered;
    }

    public void toggle(int i){
        filtered.get(i).toggle();
    }

}

class NLevelItem implements NLevelListItem {
    private Object wrappedObject;
    private NLevelItem parent;
    private NLevelView nLevelView;
    private boolean isExpanded=false;

    public NLevelItem(Object wrappedObject, NLevelItem parent, NLevelView nLevelView){
        this.wrappedObject = wrappedObject;
        this.parent = parent;
        this.nLevelView = nLevelView;
    }

    public Object getWrappedObject(){
        return wrappedObject;
    }

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void toggle() {
        isExpanded = !isExpanded;
    }

    @Override
    public NLevelListItem getParent() {
        return parent;
    }

    @Override
    public View getView() {
        return nLevelView.getView(this);
    }
}


