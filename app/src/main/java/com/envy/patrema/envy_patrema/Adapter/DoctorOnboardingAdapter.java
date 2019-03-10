package com.envy.patrema.envy_patrema.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.R;

public class DoctorOnboardingAdapter extends PagerAdapter {

    Context context;

    public DoctorOnboardingAdapter(Context context) {
        this.context = context;
    }

    private int[] slide_images = {
            R.drawable.doctor_stetho,
            R.drawable.secure_icon,
            R.drawable.doctor_icon
    };

    private String[] slide_headings = {
            "Welcome Doctor",
            "Security",
            "Setup Your Profile"
    };

    private String[] slide_content = {
            "An electronic health record app that stores and maintains your patients' health activity and health history.",
            "We aim to ensure confidentiality, integrity and authorized availability of these medical records.",
            "Please setup your profile by entering your basic details."

    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.onboarding_slide_layout, container, false);

        ImageView image = view.findViewById(R.id.imgSliderImage);
        TextView heading = view.findViewById(R.id.txtSliderHeading);
        TextView content = view.findViewById(R.id.txtSliderContent);

        image.setImageResource(slide_images[position]);
        heading.setText(slide_headings[position]);
        content.setText(slide_content[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
