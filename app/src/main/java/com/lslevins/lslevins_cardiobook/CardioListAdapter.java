package com.lslevins.lslevins_cardiobook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luke Slevinsky on 2019-01-10.
 */

public class CardioListAdapter extends RecyclerView.Adapter<CardioListAdapter.MyViewHolder> {
    private static final String TAG = "CardioListAdapter";
    public static final String POSITION = "com.lslevins.lslevins.POSITION";
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Context mContext;
    private DataStorage dataStorage;
    private List<CardioStats> cardioStats = new ArrayList<CardioStats>();
    private ItemClickListener mClickListener;
    private ColorStateList oldColors;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView sysPres, diaPres, bpm, date, comment;
        public ImageButton edit, delete;

        public MyViewHolder(View v) {
            super(v);
            sysPres = (TextView) v.findViewById(R.id.sysPres);
            diaPres = (TextView) v.findViewById(R.id.diaPres);
            bpm = (TextView) v.findViewById(R.id.bpm);
            date = (TextView) v.findViewById(R.id.date);
            comment = (TextView) v.findViewById(R.id.comment);
            edit = (ImageButton) v.findViewById(R.id.editButton);
            delete = (ImageButton) v.findViewById(R.id.deleteButton);

            oldColors =  sysPres.getTextColors();

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
    public CardioListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        dataStorage = new DataStorage(mContext);

        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardio_card, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String dateTime = formatDateTime(cardioStats.get(position).getDateTime());

        holder.sysPres.setText(Integer.toString(cardioStats.get(position).getSystolicPressure()));
        holder.diaPres.setText(Integer.toString(cardioStats.get(position).getDiastolicPressure()));
        holder.bpm.setText(Integer.toString(cardioStats.get(position).getBpm()));
        holder.date.setText(dateTime);
        holder.comment.setText(cardioStats.get(position).getComment());

        toggleBloodPressureFlags(position, holder.sysPres);
        toggleBloodPressureFlags(position,holder.diaPres);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: EDIT:"+holder.getAdapterPosition());
                Intent intent = new Intent(mContext, AddCardioStatActivity.class);
                intent.putExtra(MainActivity.REQUEST_MESSAGE, MainActivity.EDIT);
                intent.putExtra(POSITION,holder.getAdapterPosition());
                AddCardioStatActivity.updateIntentStats(intent,cardioStats.get(holder.getAdapterPosition()));
                ((Activity) mContext).startActivityForResult(intent, MainActivity.EDIT);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: DELETE:"+cardioStats.get(holder.getAdapterPosition()).toString());
                // Remove the item on remove/button click
                dataStorage.deleteStat(cardioStats.get(holder.getAdapterPosition()));

                cardioStats.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(),cardioStats.size());

                Toast.makeText(mContext,"Removed entry" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleBloodPressureFlags(int position, TextView textView) {
        if (cardioStats.get(position).getSystolicFlag()) {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            textView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.accent_rounded_background));
        } else {
            textView.setTextColor(oldColors);
            textView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.white_rounded_background));
        }
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
