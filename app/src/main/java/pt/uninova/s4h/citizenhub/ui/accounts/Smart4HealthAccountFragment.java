package pt.uninova.s4h.citizenhub.ui.accounts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.WorkManager;

import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.lang.D4LException;
import care.data4life.sdk.listener.Callback;
import care.data4life.sdk.listener.ResultListener;
import pt.uninova.s4h.citizenhub.MainActivity;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.ui.lobby.LobbyActivity;
import pt.uninova.s4h.citizenhub.work.WorkOrchestrator;

public class Smart4HealthAccountFragment extends Fragment {

    private AccountsViewModel viewModel;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch_daily_report_upload;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch_weekly_report_upload;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch_monthly_report_upload;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch_activity;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch_blood_pressure;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch_heart_rate;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch_lumbar_extension_training;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch_posture;

    public Smart4HealthAccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.smart4health_account_fragment, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(AccountsViewModel.class);
        setHasOptionsMenu(true);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

        switch_daily_report_upload = view.findViewById(R.id.switch_daily_report_upload);
        switch_weekly_report_upload = view.findViewById(R.id.switch_weekly_report_upload);
        switch_monthly_report_upload = view.findViewById(R.id.switch_monthly_report_upload);
        switch_activity = view.findViewById(R.id.switch_activity);
        switch_blood_pressure = view.findViewById(R.id.switch_blood_pressure);
        switch_heart_rate = view.findViewById(R.id.switch_heart_rate);
        switch_lumbar_extension_training = view.findViewById(R.id.switch_lumbar_extension_training);
        switch_posture = view.findViewById(R.id.switch_posture);

        if(viewModel.hasReportAutomaticUpload())
            switch_daily_report_upload.setChecked(true);
        switch_daily_report_upload.setOnCheckedChangeListener((compoundButton, b) -> {
            viewModel.setReportAutomaticUpload(compoundButton.isChecked());
            WorkOrchestrator workOrchestrator = new WorkOrchestrator(WorkManager.getInstance(requireContext()));
            if(compoundButton.isChecked())
                workOrchestrator.enqueueSmart4HealthUploader();
            else
                workOrchestrator.cancelSmart4HealthUploader();
        });

        if(viewModel.hasReportWeeklyAutomaticUpload())
            switch_weekly_report_upload.setChecked(true);
        switch_weekly_report_upload.setOnCheckedChangeListener((compoundButton, b) -> {
            viewModel.setReportWeeklyAutomaticUpload(compoundButton.isChecked());
            WorkOrchestrator workOrchestrator = new WorkOrchestrator(WorkManager.getInstance(requireContext()));
            if(compoundButton.isChecked());
        });

        if(viewModel.hasReportMonthlyAutomaticUpload())
            switch_weekly_report_upload.setChecked(true);
        switch_weekly_report_upload.setOnCheckedChangeListener((compoundButton, b) -> {
            viewModel.setReportMonthlyAutomaticUpload(compoundButton.isChecked());
            WorkOrchestrator workOrchestrator = new WorkOrchestrator(WorkManager.getInstance(requireContext()));
            if(compoundButton.isChecked());
        });

        if(viewModel.hasReportDataActivity())
            switch_activity.setChecked(true);
        switch_activity.setOnCheckedChangeListener((compoundButton, b) -> {
            viewModel.setReportDataActivity(compoundButton.isChecked());
            verifyAllSwitchStatus();
        });

        if(viewModel.hasReportDataBloodPressure())
            switch_blood_pressure.setChecked(true);
        switch_blood_pressure.setOnCheckedChangeListener((compoundButton, b) -> {
            viewModel.setReportDataBloodPressure(compoundButton.isChecked());
            verifyAllSwitchStatus();
        });


        if(viewModel.hasReportDataHeartRate())
            switch_heart_rate.setChecked(true);
        switch_heart_rate.setOnCheckedChangeListener((compoundButton, b) -> {
            viewModel.setReportDataHeartRate(compoundButton.isChecked());
            verifyAllSwitchStatus();
        });


        if(viewModel.hasReportDataLumbarExtensionTraining())
            switch_lumbar_extension_training.setChecked(true);
        switch_lumbar_extension_training.setOnCheckedChangeListener((compoundButton, b) -> {
            viewModel.setReportDataLumbarExtensionTraining(compoundButton.isChecked());
            verifyAllSwitchStatus();
        });


        if(viewModel.hasReportDataPosture())
            switch_posture.setChecked(true);
        switch_posture.setOnCheckedChangeListener((compoundButton, b) -> {
            viewModel.setReportDataPosture(compoundButton.isChecked());
            verifyAllSwitchStatus();
        });

        verifyAllSwitchStatus();
    }

    private void verifyAllSwitchStatus(){
        WorkOrchestrator workOrchestrator = new WorkOrchestrator(WorkManager.getInstance(requireContext()));
        if(!switch_activity.isChecked() && !switch_blood_pressure.isChecked() && !switch_heart_rate.isChecked() && !switch_lumbar_extension_training.isChecked() && !switch_posture.isChecked()){
            switch_daily_report_upload.setChecked(false);
            switch_daily_report_upload.setClickable(false);
            workOrchestrator.cancelSmart4HealthUploader();
            switch_weekly_report_upload.setChecked(false);
            switch_weekly_report_upload.setClickable(false);
            switch_monthly_report_upload.setChecked(false);
            switch_monthly_report_upload.setClickable(false);
        }
        else {
            if(!switch_daily_report_upload.isClickable()){
                //switch_daily_report_upload.setChecked(true);
                switch_daily_report_upload.setClickable(true);
                //workOrchestrator.enqueueSmart4HealthUploader();
            }
            if(!switch_weekly_report_upload.isClickable()){
                //switch_weekly_report_upload.setChecked(true);
                switch_weekly_report_upload.setClickable(true);
                //workOrchestrator.enqueueSmart4HealthUploader();
            }
            if(!switch_monthly_report_upload.isClickable()){
                //switch_monthly_report_upload.setChecked(true);
                switch_monthly_report_upload.setClickable(true);
                //workOrchestrator.enqueueSmart4HealthUploader();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.smart4health_account_fragment, menu);

        //viewModel = new ViewModelProvider(requireActivity()).get(AccountsViewModel.class);

        if (viewModel.hasSmart4HealthAccount()) {
            menu.removeItem(R.id.smart4health_account_fragment_menu_log_in);
            menu.findItem(R.id.smart4health_account_fragment_menu_log_out).setOnMenuItemClickListener((MenuItem item) -> {
                final Data4LifeClient client = Data4LifeClient.getInstance();

                client.logout(new Callback() {
                    @Override
                    public void onSuccess() {
                        final Activity activity = requireActivity();
                        final Intent intent = new Intent(activity, LobbyActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivity(intent);
                        activity.finish();

                        WorkOrchestrator workOrchestrator = new WorkOrchestrator(WorkManager.getInstance(requireContext()));
                        workOrchestrator.cancelSmart4HealthUploader();
                    }

                    @Override
                    public void onError(@NonNull D4LException exception) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), getString(R.string.fragment_smart4health_account_fragment_logout_failure_toast), Toast.LENGTH_SHORT).show();
                            exception.printStackTrace();
                        });
                    }
                });

                return true;
            });
        } else {
            menu.removeItem(R.id.smart4health_account_fragment_menu_log_out);
            menu.findItem(R.id.smart4health_account_fragment_menu_log_in).setOnMenuItemClickListener((MenuItem item) -> {
                final Data4LifeClient client = Data4LifeClient.getInstance();
                client.isUserLoggedIn(new ResultListener<Boolean>() {
                    @Override
                    public void onError(@NonNull D4LException e) {

                    }

                    @Override
                    public void onSuccess(Boolean value) {
                        if (value) {
                            final Activity activity = requireActivity();
                            final Intent intent = new Intent(activity, MainActivity.class);
//                    Navigation.findNavController(getView()).navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToSummaryFragment());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            activity.startActivity(intent);
                            activity.finish();

                            WorkOrchestrator workOrchestrator = new WorkOrchestrator(WorkManager.getInstance(requireContext()));
                            workOrchestrator.enqueueSmart4HealthUploader();
                        }
                    }
                });
                return true;

            });
        }
    }
}