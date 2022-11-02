package pt.uninova.s4h.citizenhub;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class OptionsButtonManager {

    private List<Integer> resourceIdList;
    private List<MenuItem.OnMenuItemClickListener> onClickListenerList;
    private Menu menu;

    public OptionsButtonManager(Menu menu, List<Integer> resourceId, List<MenuItem.OnMenuItemClickListener> onClickListener) {
        this.menu = menu;
        this.resourceIdList = resourceId;
        this.onClickListenerList = onClickListener;
    }

    public Menu addButtons() {
        int i =0;
            if (resourceIdList != null && onClickListenerList != null) {
                for (int resource : resourceIdList
                ) {
                    menu.add(resource).setTitle("Calibration").setEnabled(true).setVisible(true).setOnMenuItemClickListener(onClickListenerList.get(i));
                    i++;
                }
            }
        return menu;
    }


    public List<Integer> getResourceIdList() {
        return resourceIdList;
    }

    public void setResourceIdList(List<Integer> resourceIdList) {
        this.resourceIdList = resourceIdList;
    }

    public List<MenuItem.OnMenuItemClickListener> getOnClickListenerList() {
        return onClickListenerList;
    }

    public void setOnClickListenerList(List<MenuItem.OnMenuItemClickListener> onClickListenerList) {
        this.onClickListenerList = onClickListenerList;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
