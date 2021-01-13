package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class PrivacyPolicyFragment extends Fragment {

    private Application app;
    private ArrayList<String> original_data=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        app = (Application) requireActivity().getApplication();
        TextView privacyPolicyText = (TextView) result.findViewById(R.id.text_privacy_policy);

        Scanner data_in = new Scanner(getResources().openRawResource(R.raw.privacy_policy));
        while(data_in.hasNext()){
            original_data.add(data_in.nextLine());
        }
        privacyPolicyText.setText("");
        for(int i=0; i< original_data.size(); i++){
            privacyPolicyText.append(original_data.get(i) +"\n");
        }
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
