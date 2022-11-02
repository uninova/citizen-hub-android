package pt.uninova.s4h.citizenhub;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class OptionsButtonManager {

    private Integer resourceId;
    private String buttonText;
    private MenuItem.OnMenuItemClickListener onClickListener;
    private Menu menu;

    public OptionsButtonManager(Menu menu, Integer resourceId, String buttonText, MenuItem.OnMenuItemClickListener onClickListener) {
        this.menu = menu;
        this.resourceId = resourceId;
        this.buttonText = buttonText;
        this.onClickListener = onClickListener;
    }

    public Menu addButton() {
        menu.add(resourceId).setTitle(buttonText).setOnMenuItemClickListener(onClickListener).setVisible(true).setEnabled(true);
        return menu;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
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
