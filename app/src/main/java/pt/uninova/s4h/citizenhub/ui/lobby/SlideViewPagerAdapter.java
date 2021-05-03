package pt.uninova.s4h.citizenhub.ui.lobby;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
    private final ViewPagerController viewPagerController;

    public SlideViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
        try {
            this.viewPagerController = (ViewPagerController) ctx;
        } catch (ClassCastException e) {
            throw new ClassCastException(ctx.toString()
                    + " must implement ViewPagerController");
        }

    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return 3;
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


        TextView title = view.findViewById(R.id.title);
        TextView desc = view.findViewById(R.id.desc);

        ImageView next = view.findViewById(R.id.next);
        ImageView back = view.findViewById(R.id.back);

        TextView logo_text_view = view.findViewById(R.id.logo_text);
        Spannable word = new SpannableString("Citizen");

        word.setSpan(new ForegroundColorSpan(ctx.getResources().getColor(R.color.colorS4HDarkBlue)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        logo_text_view.setText(word);
        Spannable wordTwo = new SpannableString("HUB");

        wordTwo.setSpan(new ForegroundColorSpan(ctx.getResources().getColor(R.color.colorS4HTurquoise)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        logo_text_view.append(wordTwo);

        Button btnGetStarted = view.findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(v -> {
            this.viewPagerController.stopTimerTask();
            Intent intent = new Intent(ctx, LobbyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        });


        next.setOnClickListener(v -> {
            this.viewPagerController.stopTimerTask();
            SlideActivity.viewPager.setCurrentItem(position + 1);
        });

        back.setOnClickListener(v -> {
            this.viewPagerController.stopTimerTask();
            SlideActivity.viewPager.setCurrentItem(position - 1);

        });

        switch (position) {
            case 0:
                logo.setImageResource(R.drawable.img_citizen_hub_logo_png);
                ind1.setImageResource(R.drawable.shape_seleted_oval);
                ind2.setImageResource(R.drawable.shape_unselected_oval);
                ind3.setImageResource(R.drawable.shape_unselected_oval);

                title.setText("Welcome");
                desc.setText("Thank you for using CitizenHub");
                back.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                logo_text_view.setVisibility(View.VISIBLE);

                break;
            case 1:
                logo.setImageResource(R.drawable.ic_devices);
                ind1.setImageResource(R.drawable.shape_unselected_oval);
                ind2.setImageResource(R.drawable.shape_seleted_oval);
                ind3.setImageResource(R.drawable.shape_unselected_oval);

                title.setText("Connect Devices");
                desc.setText("Simplifying different devices connection into one app");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                logo_text_view.setVisibility(View.GONE);

                break;
            case 2:
                logo.setImageResource(R.drawable.ic_heartbeat);
                ind1.setImageResource(R.drawable.shape_unselected_oval);
                ind2.setImageResource(R.drawable.shape_unselected_oval);
                ind3.setImageResource(R.drawable.shape_seleted_oval);

                title.setText("Real-time data");
                desc.setText("Live visualisation and analytics on your health and wellbeing data!");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                logo_text_view.setVisibility(View.GONE);

                break;

        }


        container.addView(view);
        return view;

    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void continueButtonPressed() {
        this.viewPagerController.stopTimerTask();
        Intent intent = new Intent(ctx, LobbyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

}

