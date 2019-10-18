package com.ninecats.flashcard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninecats.flashcard.R;

import java.util.ArrayList;



public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<Cards> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textWord;



        public MyViewHolder(View itemView) {
            super(itemView);
            this.textWord = (TextView) itemView.findViewById(R.id.word);


        }
    }

    public CustomAdapter(ArrayList<Cards> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);

        view.setOnLongClickListener(MainActivity.myLongClickListener);
        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textWord = holder.textWord;



        textWord.setText(dataSet.get(listPosition).getWord());


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
