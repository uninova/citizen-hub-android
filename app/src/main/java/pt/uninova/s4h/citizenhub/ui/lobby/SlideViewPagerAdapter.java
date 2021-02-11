package pt.uninova.s4h.citizenhub.ui.lobby;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import pt.uninova.s4h.citizenhub.R;

public class SlideViewPagerAdapter extends PagerAdapter {

    Context ctx;

    public SlideViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_screen, container, false);


        ImageView logo = view.findViewById(R.id.authentication_fragment_img_s4h_logo_image_id);

        ImageView ind1 = view.findViewById(R.id.ind1);
        ImageView ind2 = view.findViewById(R.id.ind2);
        ImageView ind3 = view.findViewById(R.id.ind3);
        ImageView ind4 = view.findViewById(R.id.ind4);


        TextView title = view.findViewById(R.id.title);
        TextView desc = view.findViewById(R.id.desc);

        ImageView next = view.findViewById(R.id.next);
        ImageView back = view.findViewById(R.id.back);

        Button btnGetStarted = view.findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, LobbyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        });
        next.setOnClickListener(v -> SlideActivity.viewPager.setCurrentItem(position + 1));

        back.setOnClickListener(v -> SlideActivity.viewPager.setCurrentItem(position - 1));

        switch (position) {
            case 0:
                logo.setImageResource(R.drawable.citizen_hub_logo);
                ind1.setImageResource(R.drawable.seleted);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.unselected);
                ind4.setImageResource(R.drawable.unselected);

                title.setText("Welcome");
                desc.setText("We appreciate your choice to use our app, swipe if you want to know better our features or skip to login");
                back.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                break;
            case 1:
                logo.setImageResource(R.drawable.ic_devices);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.seleted);
                ind3.setImageResource(R.drawable.unselected);
                ind4.setImageResource(R.drawable.unselected);

                title.setText("Connect to Devices");
                desc.setText("Within our app we will make your life as easy as possible each time you desire to connect your device to another one");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                break;
            case 2:
                btnGetStarted.setText("Skip");
                logo.setImageResource(R.drawable.ic_heartbeat);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.seleted);
                ind4.setImageResource(R.drawable.unselected);

                title.setText("Real time data");
                desc.setText("After pairing to a device you will be able to see the live data he measures from its compatible features. ");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                break;
            case 3:
                btnGetStarted.setText("Continue");
                logo.setImageResource(R.drawable.img_s4h_logo_simple_version);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.unselected);
                ind4.setImageResource(R.drawable.seleted);

                title.setText("Smart4Health");
                desc.setText("To use our application you are required to register in smart4health platform and login. ");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                break;


        }


        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

