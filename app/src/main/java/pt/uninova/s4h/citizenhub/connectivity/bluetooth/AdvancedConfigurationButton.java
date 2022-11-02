package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.view.MenuItem;

public class AdvancedConfigurationButton {

    private int resId;
    private String buttonText;
    private MenuItem.OnMenuItemClickListener onMenuItemClickListener;

    public AdvancedConfigurationButton(int resId, String buttonText, MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.resId = resId;
        this.buttonText = buttonText;
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public MenuItem.OnMenuItemClickListener getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }

    public void setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }
}
