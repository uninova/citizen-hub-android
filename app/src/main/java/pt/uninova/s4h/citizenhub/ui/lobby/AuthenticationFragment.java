package pt.uninova.s4h.citizenhub.ui.lobby;

import static care.data4life.sdk.Data4LifeClient.D4L_AUTH;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.lang.D4LException;
import care.data4life.sdk.listener.ResultListener;
import pt.uninova.s4h.citizenhub.BuildConfig;
import pt.uninova.s4h.citizenhub.MainActivity;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.ui.accounts.AccountsViewModel;

public class AuthenticationFragment extends Fragment {
    private Button loginButton;
    private Button skipButton;
    private TextView citizenHubLogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_authentication, container, false);
        TextView textView = result.findViewById(R.id.citizen_hub_text_logo);
        Spannable word = new SpannableString(getString(R.string.fragment_authentication_spannable_string_citizen));

        word.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorS4HDarkBlue)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(word);
        Spannable wordTwo = new SpannableString("HUB");

        wordTwo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorS4HTurquoise)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(wordTwo);


        loginButton = result.findViewById(R.id.authentication_fragment_login_button);
        loginButton.setOnClickListener((View v) -> {
            final Intent loginIntent = Data4LifeClient.getInstance().getLoginIntent(requireActivity(), null);

            startActivityForResult(loginIntent, D4L_AUTH);
        });

        skipButton = result.findViewById(R.id.authentication_fragment_skip_login);
        skipButton.setOnClickListener((View v) -> {

            final AccountsViewModel viewModel = new ViewModelProvider(requireActivity()).get(AccountsViewModel.class);
            viewModel.disableSmart4HealthAccount();

            final Activity activity = requireActivity();
            final Intent intent = new Intent(activity, MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            activity.startActivity(intent);
            activity.finish();
        });

        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.SKIP_AUTH) {
            final Activity activity = requireActivity();
            final Intent intent = new Intent(activity, MainActivity.class);
//                    Navigation.findNavController(getView()).navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToSummaryFragment());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            activity.startActivity(intent);
            activity.finish();
        } else {
            authenticate();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == D4L_AUTH) {
            authenticate();
        }
    }

    private void authenticate() {
        final Data4LifeClient client = Data4LifeClient.getInstance();

        client.isUserLoggedIn(new ResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean value) {
                if (value) {
                    final Activity activity = requireActivity();
                    final Intent intent = new Intent(activity, MainActivity.class);
//                    Navigation.findNavController(getView()).navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToSummaryFragment());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    activity.startActivity(intent);
                    activity.finish();
                } else {
                    //    loginButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(D4LException exception) {
                loginButton.setVisibility(View.VISIBLE);
                exception.printStackTrace();
            }
        });
    }

}
