package com.example.virtualdarts2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.virtualdarts2.ItemClass.LayoutOne;
import static com.example.virtualdarts2.ItemClass.LayoutTwo;

public class GameViewAdapter extends RecyclerView.Adapter {
    private List<ItemClass> itemClassList;

    // public constructor for this class
    public GameViewAdapter(List<ItemClass> itemClassList) {
        this.itemClassList = itemClassList;
    }
    // Override the getItemViewType method.
    // This method uses a switch statement
    // to assign the layout to each item
    // depending on the viewType passed
    @Override
    public int getItemViewType(int position) {
        switch (itemClassList.get(position).getViewType()) {
            case 0:
                return LayoutOne; // match
            case 1:
                return LayoutTwo; // not a match
            default:
                return -1;
        }
    }

    // Create classes for each layout ViewHolder.
    class LayoutOneViewHolder extends RecyclerView.ViewHolder {
        private TextView matchMode, matchDate, matchYourScore, rivalScore, winner;;
        private LinearLayout linearLayout_1;

        public LayoutOneViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the Views
            matchMode = itemView.findViewById(R.id.tvMatchandMode);
            matchDate = itemView.findViewById(R.id.tvGameDateMatch);
            matchYourScore = itemView.findViewById(R.id.tvYourScoreMatch);
            rivalScore = itemView.findViewById(R.id.tvRivalScore);
            winner = itemView.findViewById(R.id.tvWinner);
            linearLayout_1 = itemView.findViewById(R.id.linearlayout_1);
        }

        // method to set the views that will
        // be used further in onBindViewHolder method.
        private void setView(String _matchMode, String _matchDate, String _matchYourScore,
                             String _matchOtherScore, String _matchWinner) {
            matchMode.setText(_matchMode);
            matchDate.setText(_matchDate);
            matchYourScore.setText(_matchYourScore);
            rivalScore.setText(_matchOtherScore);
            winner.setText(_matchWinner);
        }
    }

    // similarly a class for the second layout is also
    // created.
    class LayoutTwoViewHolder extends RecyclerView.ViewHolder {
        private TextView singleMode, singleDate, singleScore;
        private LinearLayout linearLayout_2;

        public LayoutTwoViewHolder(@NonNull View itemView) {
            super(itemView);
            singleMode = itemView.findViewById(R.id.tvSingleMode);
            singleDate = itemView.findViewById(R.id.tvGameDateSingle);
            singleScore = itemView.findViewById(R.id.tvYourScoreSingle);
            linearLayout_2 = itemView.findViewById(R.id.linearlayout_2);
        }

        private void setViews(String _singleMode, String _singleDate, String _singleScore) {
            singleMode.setText(_singleMode);
            singleDate.setText(_singleDate);
            singleScore.setText(_singleScore);
        }
    }

    // In the onCreateViewHolder, inflate the
    // xml layout as per the viewType.
    // This method returns either of the
    // ViewHolder classes defined above,
    // depending upon the layout passed as a parameter.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LayoutOne:
                View layoutOne = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.game_entry, parent,
                                false);
                return new LayoutOneViewHolder(layoutOne);
            case LayoutTwo:
                View layoutTwo = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.game_entry_single, parent,
                                false);
                return new LayoutTwoViewHolder(layoutTwo);
            default:
                return null;
        }
    }
    // In onBindViewHolder, set the Views for each element
    // of the layout using the methods defined in the
    // respective ViewHolder classes.

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (itemClassList.get(position).getViewType()) {
            case LayoutOne:
                String mMode = itemClassList.get(position).getMatchMode();
                String mDate = itemClassList.get(position).getMatchDate();
                String mYScore = itemClassList.get(position).getMatchYourScore();
                String mOScore = itemClassList.get(position).getMatchOtherScore();
                String mWinner = itemClassList.get(position).getMatchWinner();
                ((LayoutOneViewHolder)holder).setView(mMode, mDate, mYScore, mOScore, mWinner);

                // The following code pops a toast message
                // when the item layout is clicked.
                // This message indicates the corresponding
                // layout.
                ((LayoutOneViewHolder)holder)
                        .linearLayout_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                /*Toast.makeText(view.getContext(),
                                                "Hello from Layout One!", Toast.LENGTH_SHORT).show();*/
                                // to do when clicked
                            }
                        });

                break;

            case LayoutTwo:
                String sMode = itemClassList.get(position).getSingleMode();
                String sDate = itemClassList.get(position).getSingleDate();
                String sScore = itemClassList.get(position).getSingleScore();
                ((LayoutTwoViewHolder)holder).setViews(sMode, sDate, sScore);
                ((LayoutTwoViewHolder)holder)
                        .linearLayout_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                /*Toast.makeText(view.getContext(),
                                                "Hello from Layout Two!", Toast.LENGTH_SHORT).show();*/
                                // to do when clicked
                            }
                        });
                break;
            default:
                return;
        }
    }

    // This method returns the count of items present in the
    // RecyclerView at any given time.
    @Override
    public int getItemCount() {
        return itemClassList.size();
    }
}
