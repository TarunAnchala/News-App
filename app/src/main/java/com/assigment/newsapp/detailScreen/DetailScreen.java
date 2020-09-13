package com.assigment.newsapp.detailScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.assigment.newsapp.NewsViewModel;
import com.assigment.newsapp.R;
import com.assigment.newsapp.db.NewsEntity;
import com.assigment.newsapp.utils.Utils;
import com.bumptech.glide.Glide;

public class DetailScreen extends Fragment implements View.OnClickListener {

    public static final String TAG = "DetailScreen";
    private TextView source;
    private TextView dsTitle;
    private TextView date;
    private TextView dsDescription;
    private ImageView dsLogo;
    private ImageView backIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        if (getArguments() != null && getActivity() != null) {
            NewsViewModel viewModel = ViewModelProviders.of(getActivity()).get(NewsViewModel.class);
            NewsEntity newsEntity = viewModel.getNewsObj(getArguments().getInt(Utils.POSITION));
            setData(newsEntity);
        }
    }

    private void setData(NewsEntity newsEntity) {
        source.setText(newsEntity.getSource());
        dsTitle.setText(newsEntity.getTitle());
        dsDescription.setText(newsEntity.getDescription());
        date.setText(newsEntity.getDate());
        Glide.with(this).load(newsEntity.getImageUrl()).into(dsLogo);
    }

    private void initializeViews(View view) {
        source = view.findViewById(R.id.dsSource);
        dsTitle = view.findViewById(R.id.dsTitle);
        dsDescription = view.findViewById(R.id.description);
        dsLogo = view.findViewById(R.id.newsFullImage);
        backIcon = view.findViewById(R.id.backIcon);
        backIcon.setOnClickListener(this);
        date = view.findViewById(R.id.date);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backIcon && getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }
}
