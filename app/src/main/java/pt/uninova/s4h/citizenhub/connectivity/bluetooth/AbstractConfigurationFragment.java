package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pt.uninova.s4h.citizenhub.connectivity.Agent;

public class AbstractConfigurationFragment extends Fragment {
    public AbstractConfigurationFragment(Agent agent) {
    }

    public AbstractConfigurationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setView(ViewGroup layout, boolean enabled) {
        setChildrenEnabled(layout, enabled);
    }

    public void setChildrenEnabled(ViewGroup layout, boolean state) {
        layout.setEnabled(state);
//        if (!layout.isEnabled()) {
//            layout.setAlpha(0.5f);
//        } else layout.setAlpha(1);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                setChildrenEnabled((ViewGroup) child, state);
            } else {
                child.setEnabled(state);
            }
        }
    }

}
