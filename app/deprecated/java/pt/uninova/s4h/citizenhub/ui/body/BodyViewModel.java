package pt.uninova.s4h.citizenhub.ui.body;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BodyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BodyViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}