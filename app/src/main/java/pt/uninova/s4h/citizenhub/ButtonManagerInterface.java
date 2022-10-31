package pt.uninova.s4h.citizenhub;

import android.view.MenuItem;

import java.util.List;

public interface ButtonManagerInterface {

    public boolean hasButtons();
    public List<Integer> getResourceIds();
    public List<MenuItem.OnMenuItemClickListener> getOnMenuItemClickListeners();

}
