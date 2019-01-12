package com.lslevins.lslevins_cardiobook;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luke Slevinsky on 2019-01-10.
 */

public class CardioListAdapter extends RecyclerView.Adapter<CardioListAdapter.MyViewHolder> {
    private static final String TAG = "CardioListAdapter";

    private List<CardioStats> cardioStats = new ArrayList<CardioStats>();
    private ItemClickListener mClickListener;
    public DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView sysPres, diaPres, bpm, date;

        public MyViewHolder(View v) {
            super(v);
            sysPres = (TextView) v.findViewById(R.id.sysPres);
            diaPres = (TextView) v.findViewById(R.id.diaPres);
            bpm = (TextView) v.findViewById(R.id.bpm);
            date = (TextView) v.findViewById(R.id.date);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardioListAdapter(List<CardioStats> myDataset) {
        cardioStats = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardioListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardio_card, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String dateTime = formatDateTime(cardioStats.get(position).getDateTime());

        holder.sysPres.setText(Integer.toString(cardioStats.get(position).getSystolicPressure()));
        holder.diaPres.setText(Integer.toString(cardioStats.get(position).getDiastolicPressure()));
        holder.bpm.setText(Integer.toString(cardioStats.get(position).getBpm()));
        holder.date.setText(dateTime);
    }

    // convenience method for getting data at click position
    int getItem(int id) {
        return cardioStats.get(id).getSystolicPressure();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cardioStats.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private String formatDateTime(LocalDateTime ldt) {
        return ldt.format(dateFormatter);
    }
}
