package pt.uninova.s4h.citizenhub;

import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;

public class OptionsMenuManager {

    private Menu menu;
    public ArrayMap<String, MenuItem.OnMenuItemClickListener> buttonMap = new ArrayMap<>();

    public OptionsMenuManager(Menu menu, ArrayMap<String, MenuItem.OnMenuItemClickListener> buttonMap) {
        this.menu = menu;
        this.buttonMap = buttonMap;
    }

    private Menu updateMenu() {
        for (Map.Entry<String, MenuItem.OnMenuItemClickListener> entry : buttonMap.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            menu.add(0, Integer.parseInt(entry.getKey()), 0, entry.getKey());
            menu.getItem(Integer.parseInt(entry.getKey())).setOnMenuItemClickListener(entry.getValue());
        }
        return menu;
    }

    public Menu getMenu() {
        return menu;
    }


    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
