package pe.com.codespace.transito;

import android.view.View;

/**
 * Created by Carlos on 20/04/2014.
 */
public interface NLevelListItem {
    public boolean isExpanded();
    public void toggle();
    public NLevelListItem getParent();
    public View getView();
}

interface NLevelView {

    public View getView(NLevelItem item);

}
