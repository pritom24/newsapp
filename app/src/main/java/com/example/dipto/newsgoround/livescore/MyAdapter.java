package com.example.dipto.newsgoround.livescore;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dipto.newsgoround.MatchDetailActivity;
import com.example.dipto.newsgoround.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Model> modelList;
    private Context context;

    public MyAdapter(List<Model> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row , viewGroup , false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final Model model = modelList.get(position);
        viewHolder.team1Name.setText(model.getTeam1());
        viewHolder.team2Name.setText(model.getTeam2());
        viewHolder.matchStatusName.setText(model.getMatchStatus());
        viewHolder.matchTypeName.setText(model.getMatchType());
        viewHolder.dateText.setText(model.getDate());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String matchId = model.getId();
                String date = model.getDate();

                Intent intent = new Intent(context , MatchDetailActivity.class);
                intent.putExtra("match_id" , matchId);
                intent.putExtra("date" , date);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView team1Name , team2Name , matchTypeName , matchStatusName , dateText;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            team1Name = itemView.findViewById(R.id.team1Name);
            team2Name =itemView.findViewById(R.id.team2Name);
            matchTypeName = itemView.findViewById(R.id.matchTypeText);
            matchStatusName = itemView.findViewById(R.id.matchStatusText);
            dateText = itemView.findViewById(R.id.matchDateText);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }

}
