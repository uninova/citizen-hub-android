package pt.uninova.s4h.citizenhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.File;

import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.lang.D4LException;
import care.data4life.sdk.listener.ResultListener;

public class EntryPageFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.entry_page_fragment, container, false);
        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            wasLoggedIn();
         ///   onEntry();
        }

        public boolean firstInteraction(){
            final File file = new File(requireContext().getFilesDir(),"first.txt");
            return !file.exists();
        }

        public void onEntry(){

            if(firstInteraction())
            {
                Navigation.findNavController(requireView()).navigate(EntryPageFragmentDirections.actionEntryPageFragmentToPresentationFragment());
            }
            else {
                Navigation.findNavController(requireView()).navigate(EntryPageFragmentDirections.actionEntryPageFragmentToAuthenticationFragment());
            }
        }

        private void wasLoggedIn() {
                final Data4LifeClient client = Data4LifeClient.getInstance();
                client.isUserLoggedIn(new ResultListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean value) {
                        if (value) {
                            final Intent intent = new Intent(getActivity(), MainActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            requireActivity().startActivity(intent);
                            requireActivity().finish();
                        }
                        else{
                            onEntry();
                        }
                    }

                    @Override
                    public void onError(D4LException exception) {
                        onEntry();                    }
                });
            }
}