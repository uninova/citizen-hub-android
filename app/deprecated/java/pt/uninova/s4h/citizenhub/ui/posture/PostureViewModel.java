package pt.uninova.s4h.citizenhub.ui.posture;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pt.uninova.s4h.citizenhub.ui.Home;

public class PostureViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PostureViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}