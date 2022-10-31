package pt.uninova.s4h.citizenhub;

import android.view.Menu;
import android.view.View;

public class ButtonManager {

    private int resourceId;
    private View.OnClickListener onClickListener;
    private Menu menu;

    public ButtonManager(int resourceId, View.OnClickListener onClickListener, Menu menu) {
        this.resourceId = resourceId;
        this.onClickListener = onClickListener;
        this.menu = menu;
    }



    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
