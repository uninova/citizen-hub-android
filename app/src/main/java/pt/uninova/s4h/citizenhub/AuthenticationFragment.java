package pt.uninova.s4h.citizenhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.lang.D4LException;
import care.data4life.sdk.listener.ResultListener;

import static care.data4life.sdk.Data4LifeClient.D4L_AUTH;

public class AuthenticationFragment extends Fragment {

    private Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.authentication_fragment, container, false);
        loginButton = result.findViewById(R.id.fragment_authentication_button_login);

        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginButton.setOnClickListener(v -> {
            Intent loginIntent = Data4LifeClient.getInstance().getLoginIntent(requireContext(), null);
            startActivityForResult(loginIntent, D4L_AUTH);
        });

        final Data4LifeClient client = Data4LifeClient.getInstance();

        client.isUserLoggedIn(new ResultListener<Boolean>() {

            @Override
            public void onSuccess(Boolean value) {
                if (value) {
                    Navigation.findNavController(requireView()).navigate(AuthenticationFragmentDirections.actionPresentationFragmentToSummaryFragment());
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(D4LException exception) {
                loginButton.setVisibility(View.VISIBLE);
                exception.printStackTrace();
            }
        });
    }

//        @Override
//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//
//            if (requestCode == D4L_AUTH) {
//                if (resultCode == RESULT_OK) {
//                    startMainActivity();
//                } else {
//                    loginButton.setVisibility(View.VISIBLE);
//                }
//            }
//
//        }
//
//        private void startMainActivity() {
//            startActivity(new Intent(this, MainActivity.class));
//        }
//    }

}
