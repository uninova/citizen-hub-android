package pt.uninova.s4h.citizenhub;

import android.view.View;

public class ButtonManager {

    private int resourceId;
    private View.OnClickListener onClickListener;

    public ButtonManager(int resourceId, View.OnClickListener onClickListener) {
        this.resourceId = resourceId;
        this.onClickListener = onClickListener;
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
}
