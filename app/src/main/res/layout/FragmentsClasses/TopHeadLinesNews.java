package com.example.dipto.newsgoround.FragmentsClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dipto.newsgoround.R;

public class TopHeadLinesNews extends Fragment {

    View view;
    public TopHeadLinesNews(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.top_headline_news_layout ,container ,false);
        return view;
    }
}
