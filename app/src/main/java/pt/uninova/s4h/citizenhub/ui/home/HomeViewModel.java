package pt.uninova.s4h.citizenhub.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Just a test for a service. Triggers every 5 sec. with Notification.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}