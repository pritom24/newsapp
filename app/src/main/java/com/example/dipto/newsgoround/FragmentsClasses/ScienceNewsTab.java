package com.example.dipto.newsgoround.FragmentsClasses;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dipto.newsgoround.Adapter;
import com.example.dipto.newsgoround.DetailNewsContentActivity;
import com.example.dipto.newsgoround.MainActivity;
import com.example.dipto.newsgoround.Model.Article;
import com.example.dipto.newsgoround.Model.News;
import com.example.dipto.newsgoround.R;
import com.example.dipto.newsgoround.api.ApiClient;
import com.example.dipto.newsgoround.api.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScienceNewsTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    public static final String API_KEY = "5c5d34a75f14476198797ef9f2fb6e9c";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;

    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button buttonRetry;





    public ScienceNewsTab(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.science_news_layout ,container ,false);


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);


        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);


        onLoadingSwipeRefresh("");

        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage =view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        buttonRetry = view.findViewById(R.id.buttonRetry);






        return view;
    }


    public void LoadJson(final String keyWord)
    {

        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //final String country = Utils.getCountry();
        String category = "science";
        String country = "us";
        String language = "en";

        Call <News> call;
        if(keyWord.length() > 0 ){
            call = apiInterface.getNewsSearch(keyWord , language , "publishedAt",API_KEY);
        }else{
            call = apiInterface.getScienceNews(country , category , API_KEY);
        }


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {


                if(response.isSuccessful() && response.body().getArticles() != null) {

                    if(!articles.isEmpty()) {

                        articles.clear();
                    }

                    articles = response.body().getArticles();
                    adapter = new Adapter(articles , getActivity());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                    initListener();



                }else{
                    swipeRefreshLayout.setRefreshing(false);

                    String errorCode;
                    switch (response.code()){
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;

                    }

                    showErrorMessage(R.drawable.no_result ,
                            "No Result",
                            "Please Try Again\n"+
                                    errorCode);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(R.drawable.no_result ,
                        "Oops..",
                        "Network failure , Please Try Again\n"+
                                t.toString());

            }
        });
    }






    public  void initListener() {
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(getActivity() , DetailNewsContentActivity.class);
                //Toast.makeText(MainActivity.this , "Clicked",Toast.LENGTH_SHORT).show();

                ImageView imageView = view.findViewById(R.id.img);

                Article article = articles.get(position);
                intent.putExtra("url" , article.getUrl());
                intent.putExtra("title" , article.getTitle());
                intent.putExtra("img" , article.getUrlToImage());
                intent.putExtra("date" , article.getPublishedAt());
                intent.putExtra("source" , article.getSource().getName());
                intent.putExtra("author" , article.getAuthor());

                Pair<View , String> pair = Pair.create((View) imageView , ViewCompat.getTransitionName(imageView));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        pair
                );

                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    startActivity(intent , optionsCompat.toBundle());
                }else{
                    startActivity(intent);
                }


            }

        });

    }






    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyWord){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadJson(keyWord);
            }
        });
    }



    public void showErrorMessage(int imageView , String title , String message) {
        if(errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefresh("");
            }
        });
    }





}
