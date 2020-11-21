package pt.uninova.s4h.citizenhub;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_about, container, false);
        TextView clickableTextLink = result.findViewById(R.id.text_about_2paragraph);
        String linkText = "<a href='https://www.smart4health.eu/'>www.smart4health.eu";
        clickableTextLink.setText(Html.fromHtml(linkText));
        clickableTextLink.setMovementMethod(LinkMovementMethod.getInstance());

        /*
        //to test loading screen
        final View result = inflater.inflate(R.layout.fragment_loading, container, false);
        ProgressDialog nDialog;
        nDialog = new ProgressDialog(getContext());
        nDialog.setMessage("Loading Device Data..");
        nDialog.setTitle("Retrieving Data"); //nice without this
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
        nDialog.dismiss(); //TO remove dialog (still shows progress stuff)*/

        return result;
    }

}