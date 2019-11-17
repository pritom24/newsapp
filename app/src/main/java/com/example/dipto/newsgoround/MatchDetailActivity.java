package com.example.dipto.newsgoround;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MatchDetailActivity extends AppCompatActivity {

    private TextView mTeam1, mTeam2, mMatchStatus, mScoreText, mDescriptionText, mDateText, mMatchResultText;
    private TextView batTeam1Title, batTeam2Title, batTeam1Detail, batTeam2Detail;
    private TextView fieldTeam1Title, fieldTeam2Title, fieldTeam1Detail, fieldTeam2Detail;
    private TextView bowlTeam1Title, bowlTeam2Title, bowlTeam1Detail, bowlTeam2Detail;


    private String url = "http://cricapi.com/api/cricketScore?apikey=0xhWTKZdCHdAMtKt9BCmMHRXzLu2&unique_id=";

    private String urlofMatchSummary = "http://cricapi.com/api/fantasySummary/?apikey=0xhWTKZdCHdAMtKt9BCmMHRXzLu2&unique_id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

        Intent intent = getIntent();
        String unique_id = intent.getStringExtra("match_id");
        String date = intent.getStringExtra("date");
        url = url + unique_id;


        //__________match summary________
        urlofMatchSummary = urlofMatchSummary + unique_id;
        fieldTeam1Title = findViewById(R.id.fieldTeam1Title);
        fieldTeam2Title = findViewById(R.id.fieldTeam2Title);
        fieldTeam1Detail = findViewById(R.id.fieldTeam1Detail);
        fieldTeam2Detail = findViewById(R.id.fieldTeam2Detail);

        bowlTeam1Title = findViewById(R.id.bowlTeam1Title);
        bowlTeam1Detail = findViewById(R.id.bowlTeam1Detail);
        bowlTeam2Title = findViewById(R.id.bowlTeam2Title);
        bowlTeam2Detail = findViewById(R.id.bowlTeam2Detail);

        batTeam1Detail = findViewById(R.id.batTeam1Detail);
        batTeam1Title = findViewById(R.id.batTeam1Title);
        batTeam2Title = findViewById(R.id.batTeam2TitleText);
        batTeam2Detail = findViewById(R.id.batTeam2Detail);


        //____________match summary code finnish________
        mTeam1 = findViewById(R.id.team1NameDetail);
        mTeam2 = findViewById(R.id.team2NameDetail);
        mMatchStatus = findViewById(R.id.matchStatusTextDetail);
        mScoreText = findViewById(R.id.scoreTextDetail);
        //mDescriptionText = findViewById(R.id.descriptionTextDetail);
        mDateText = findViewById(R.id.matchDateTextDetail);
        // mMatchResultText =findViewById(R.id.resultTextDetail);

        mDateText.setText(date);

        loadData();
        loadDataForMatchSummary();


    }


    private void loadData() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {


                    JSONObject jsonObject = new JSONObject(response);

                    String team1 = jsonObject.getString("team-1");
                    String team2 = jsonObject.getString("team-2");
                    String matchStatus = jsonObject.getString("matchStarted");

                    if (matchStatus.equals("true")) {
                        matchStatus = "Match started";
                    } else {
                        matchStatus = "Match not started yet";
                    }

                    mTeam1.setText(team1);
                    mTeam2.setText(team2);
                    mMatchStatus.setText(matchStatus);

                    try {
                        String score = jsonObject.getString("score");
                        //String description = jsonObject.getString("description");
                        //String result = jsonObject.getString("stat");


                        mScoreText.setText(score);
                        //mDescriptionText.setText(description);
                        //mMatchResultText.setText(result);


                    } catch (Exception e) {
                        Toast.makeText(MatchDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MatchDetailActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void loadDataForMatchSummary() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                urlofMatchSummary, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataObject = jsonObject.getJSONObject("data");

                    JSONArray fieldJArray = dataObject.getJSONArray("fielding");
                    JSONArray bowlJArray = dataObject.getJSONArray("bowling");
                    JSONArray batJArray = dataObject.getJSONArray("batting");


                    //_____________fielding Summary________

                    JSONObject field0 = fieldJArray.getJSONObject(0);
                    JSONObject field1 = fieldJArray.getJSONObject(1);

                    String field1Title = field0.getString("title");
                    String field2Title = field1.getString("title");
                    JSONArray field1ScoresJArray = field0.getJSONArray("scores");
                    JSONArray field2ScoresJArray = field1.getJSONArray("scores");

                    fieldTeam1Title.setText(field1Title);
                    for (int i = 0; i < field1ScoresJArray.length(); i++) {
                        String name = field1ScoresJArray.getJSONObject(i).getString("name");
                        String bowled = field1ScoresJArray.getJSONObject(i).getString("bowled");
                        String catchh = field1ScoresJArray.getJSONObject(i).getString("catch");
                        String lbw = field1ScoresJArray.getJSONObject(i).getString("lbw");
                        String runout = field1ScoresJArray.getJSONObject(i).getString("runout");
                        String stumped = field1ScoresJArray.getJSONObject(i).getString("stumped");

                        fieldTeam1Detail.append("Name: " + name
                                + "\nBowled: " + bowled
                                + "\nCatch: " + catchh
                                + "\nLBW: " + lbw
                                + "\nRunOut: " + runout
                                + "\nStumped: " + stumped + "\n\n"
                        );


                    }

                    fieldTeam2Title.setText(field2Title);
                    for (int i = 0; i < field2ScoresJArray.length(); i++) {
                        String name = field2ScoresJArray.getJSONObject(i).getString("name");
                        String bowled = field2ScoresJArray.getJSONObject(i).getString("bowled");
                        String catchh = field2ScoresJArray.getJSONObject(i).getString("catch");
                        String lbw = field2ScoresJArray.getJSONObject(i).getString("lbw");
                        String runout = field2ScoresJArray.getJSONObject(i).getString("runout");
                        String stumped = field2ScoresJArray.getJSONObject(i).getString("stumped");

                        fieldTeam2Detail.append("Name: " + name
                                + "\nBowled: " + bowled
                                + "\nCatch: " + catchh
                                + "\nLBW: " + lbw
                                + "\nRunOut: " + runout
                                + "\nStumped: " + stumped + "\n\n"
                        );


                    }


                    //_____________bowling Summary____________

                    JSONObject bowl0 = bowlJArray.getJSONObject(0);
                    JSONObject bowl1 = bowlJArray.getJSONObject(1);

                    String bowl1Title = bowl0.getString("title");
                    String bowl2Title = bowl1.getString("title");
                    JSONArray bowl1ScoresJArray = bowl0.getJSONArray("scores");
                    JSONArray bowl2ScoresJArray = bowl1.getJSONArray("scores");

                    bowlTeam1Title.setText(bowl1Title);
                    for (int i = 0; i < bowl1ScoresJArray.length(); i++) {
                        String bowlerName = bowl1ScoresJArray.getJSONObject(i).getString("bowler");
                        String overs = bowl1ScoresJArray.getJSONObject(i).getString("O");
                        String wickets = bowl1ScoresJArray.getJSONObject(i).getString("W");
                        String runs = bowl1ScoresJArray.getJSONObject(i).getString("R");
                        String zeroes = bowl1ScoresJArray.getJSONObject(i).getString("0s");
                        String fours = bowl1ScoresJArray.getJSONObject(i).getString("4s");
                        String sixes = bowl1ScoresJArray.getJSONObject(i).getString("6s");
                        String m = bowl1ScoresJArray.getJSONObject(i).getString("M");
                        String econ = bowl1ScoresJArray.getJSONObject(i).getString("Econ");

                        bowlTeam1Detail.append("Name: " + bowlerName
                                + "\nOvers " + overs
                                + "\nWickets: " + wickets
                                + "\nRuns: " + runs
                                + "\n0s: " + zeroes
                                + "\n4s: " + fours
                                + "\n6s: " + sixes
                                + "\nMs: " + m
                                + "\nEcon: " + econ
                                + "\n\n"
                        );


                    }


                    bowlTeam2Title.setText(bowl2Title);
                    for (int i = 0; i < bowl2ScoresJArray.length(); i++) {
                        String bowlerName = bowl2ScoresJArray.getJSONObject(i).getString("bowler");
                        String overs = bowl2ScoresJArray.getJSONObject(i).getString("O");
                        String wickets = bowl2ScoresJArray.getJSONObject(i).getString("W");
                        String runs = bowl2ScoresJArray.getJSONObject(i).getString("R");
                        String zeroes = bowl2ScoresJArray.getJSONObject(i).getString("0s");
                        String fours = bowl2ScoresJArray.getJSONObject(i).getString("4s");
                        String sixes = bowl2ScoresJArray.getJSONObject(i).getString("6s");
                        String m = bowl2ScoresJArray.getJSONObject(i).getString("M");
                        String econ = bowl2ScoresJArray.getJSONObject(i).getString("Econ");

                        bowlTeam2Detail.append("Name: " + bowlerName
                                + "\nOvers " + overs
                                + "\nWickets: " + wickets
                                + "\nRuns: " + runs
                                + "\n0s: " + zeroes
                                + "\n4s: " + fours
                                + "\n6s: " + sixes
                                + "\nMs: " + m
                                + "\nEcon: " + econ
                                + "\n\n"
                        );


                    }


                    //_____________batting Summary________
                    JSONObject bat0 = batJArray.getJSONObject(0);
                    JSONObject bat1 = batJArray.getJSONObject(1);

                    String bat1Title = bat0.getString("title");
                    String bat2Title = bat1.getString("title");
                    JSONArray bat1ScoresJArray = bat0.getJSONArray("scores");
                    JSONArray bat2ScoresJArray = bat1.getJSONArray("scores");

                    batTeam1Title.setText(bat1Title);
                    batTeam2Title.setText(bat2Title);

                    for (int i = 0; i < bat1ScoresJArray.length(); i++) {
                        String batsman = bat1ScoresJArray.getJSONObject(i).getString("batsman");
                        //String dismissal = bat1ScoresJArray.getJSONObject(i).getString("dismissal");

                        String sr = bat1ScoresJArray.getJSONObject(i).getString("SR");
                        String sixes = bat1ScoresJArray.getJSONObject(i).getString("6s");
                        String fours = bat1ScoresJArray.getJSONObject(i).getString("4s");
                        String balls = bat1ScoresJArray.getJSONObject(i).getString("B");
                        String runs = bat1ScoresJArray.getJSONObject(i).getString("R");
                        String dismissal_info = bat1ScoresJArray.getJSONObject(i).getString("dismissal-info");


                        batTeam1Detail.append("Batsman Name: " + batsman
                                // +"\nDismissal Status: "+dismissal
                                + "\nStrike Rate: " + sr
                                + "\nSixes: " + sixes
                                + "\nFours: " + fours
                                + "\nBalls Played: " + balls
                                + "\nRuns: " + runs
                                + "\nDismissal Info: " + dismissal_info
                                + "\n\n"
                        );


                    }
                    for (int i = 0; i < bat2ScoresJArray.length(); i++) {
                        String batsman2 = bat2ScoresJArray.getJSONObject(i).getString("batsman");
                        // String dismissal2 = bat2ScoresJArray.getJSONObject(i).getString("dismissal");
                        String sr2 = bat2ScoresJArray.getJSONObject(i).getString("SR");
                        String sixes2 = bat2ScoresJArray.getJSONObject(i).getString("6s");
                        String fours2 = bat2ScoresJArray.getJSONObject(i).getString("4s");
                        String balls2 = bat2ScoresJArray.getJSONObject(i).getString("B");
                        String runs2 = bat2ScoresJArray.getJSONObject(i).getString("R");
                        String dismissal_info2 = bat2ScoresJArray.getJSONObject(i).getString("dismissal-info");


                        batTeam2Detail.append("Batsman Name: " + batsman2
                                // +"\nDismissal Status: "+dismissal2
                                + "\nStrike Rate: " + sr2
                                + "\nSixes: " + sixes2
                                + "\nFours: " + fours2
                                + "\nBalls Played: " + balls2
                                + "\nRuns: " + runs2
                                + "\nDismissal Info: " + dismissal_info2
                                + "\n\n"
                        );

                    }


                } catch (Exception e) {
                    Toast.makeText(MatchDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MatchDetailActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return super.onSupportNavigateUp();
    }
}
