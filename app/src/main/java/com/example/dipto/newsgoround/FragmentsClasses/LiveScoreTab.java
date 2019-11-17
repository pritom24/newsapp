package com.example.dipto.newsgoround.FragmentsClasses;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dipto.newsgoround.R;
import com.example.dipto.newsgoround.livescore.Model;
import com.example.dipto.newsgoround.livescore.MyAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class LiveScoreTab extends Fragment {

    View view;
    private RecyclerView mRecylerView;
    private RecyclerView.Adapter mAdapter;
    private List<Model> modelList;

    private String url = "http://cricapi.com/api/matches?apikey=0xhWTKZdCHdAMtKt9BCmMHRXzLu2";


    public LiveScoreTab(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.live_score_tab_layout  , container , false);

        mRecylerView=view.findViewById(R.id.recyclerView);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        modelList = new ArrayList<>();

        loadUrlData();




        return view;
    }



    private void loadUrlData(){

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {

                    JSONArray jsonArray = new JSONObject(response).getJSONArray("matches");

                    for(int i=0;i<jsonArray.length();i++){
                        try{

                            String uniqueId = jsonArray.getJSONObject(i).getString("unique_id");
                            String team1 = jsonArray.getJSONObject(i).getString("team-1");
                            String team2 = jsonArray.getJSONObject(i).getString("team-2");
                            String matchType = jsonArray.getJSONObject(i).getString("type");
                            String matchStatus = jsonArray.getJSONObject(i).getString("matchStarted");

                            if(matchStatus.equals("true")){
                                matchStatus = "Match started";
                            }else{
                                matchStatus = "Match not started yet";
                            }

                            String dateTimeGMT = jsonArray.getJSONObject(i).getString("dateTimeGMT");
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            format1.setTimeZone(TimeZone.getTimeZone(dateTimeGMT));
                            Date date = format1.parse(dateTimeGMT);

                            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            format2.setTimeZone(TimeZone.getTimeZone("GMT"));

                            String dateTime = format2.format(date);

                            Model model = new Model(uniqueId , team1 , team2 , matchType , matchStatus , dateTime);
                            modelList.add(model);

                        }catch (Exception e){

                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    mAdapter = new MyAdapter(modelList , getContext());
                    mRecylerView.setAdapter(mAdapter);

                }catch (Exception e){
                    Toast.makeText(getActivity() , ""+e.getMessage() , Toast.LENGTH_SHORT).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity() , "Error: "+error.toString() , Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }
}
