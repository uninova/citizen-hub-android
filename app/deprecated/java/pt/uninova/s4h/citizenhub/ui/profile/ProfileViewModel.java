package pt.uninova.s4h.citizenhub.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pt.uninova.s4h.citizenhub.ui.Home;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(Home.loggedEmail);
    }

    public LiveData<String> getText() {
        return mText;
    }
}