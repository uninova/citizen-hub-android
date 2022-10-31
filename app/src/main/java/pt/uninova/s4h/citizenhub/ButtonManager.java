package pt.uninova.s4h.citizenhub;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ButtonManager {

    private int resourceId;
    private MenuItem.OnMenuItemClickListener onClickListener;
    private Menu menu;

    public ButtonManager(int resourceId, MenuItem.OnMenuItemClickListener onClickListener) {
        this.resourceId = resourceId;
        this.onClickListener = onClickListener;
    }

    public Menu addButton(Menu menu){
        menu.add(resourceId);
        menu.getItem(resourceId).setOnMenuItemClickListener(onClickListener);
        return menu;
    }

    public Menu removeButton(Menu menu){
        menu.removeItem(resourceId);
        return menu;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public MenuItem.OnMenuItemClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(MenuItem.OnMenuItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
