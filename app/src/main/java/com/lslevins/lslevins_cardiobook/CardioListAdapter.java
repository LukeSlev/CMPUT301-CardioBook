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
 * RecyclerView Adapter for CardioStats
 *
 * This class is used to display CardioStats in the MainActivites RecyclerView list
 *
 * @author Luke Slevinsky
 */
public class CardioListAdapter extends RecyclerView.Adapter<CardioListAdapter.MyViewHolder> {
    private static final String TAG = "CardioListAdapter";
    public static final String POSITION = "com.lslevins.lslevins.POSITION";
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Context mContext;
    private DataStorage dataStorage;
    private List<CardioStats> cardioStats = new ArrayList<CardioStats>();
    private ColorStateList oldColors;

    /**
     * Custom View holder for CustomListAdapter
     *
     * This class provides a reference to the views for each data item being displayed.
     *
     * @author Luke Slevinsky
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
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

        }
    }

    /**
     * Constructor for the CardioListAdapter
     *
     * @param myDataset sets the data set for the RecyclerView
     */
    public CardioListAdapter(List<CardioStats> myDataset) {
        cardioStats = myDataset;
    }

    /**
     * onCreateViewHolder method to create new views in the view holder
     *
     * @param parent The parent view group
     * @return The resulting view holder
     */
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

    /**
     * onBindViewHolder method that replaces the contents of a view
     *
     * @param holder The view holder
     * @param position The position in the view holder list
     */
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

        // Set the blood pressure flags if the inputted pressures are outside of a healthy range
        toggleBloodPressureFlags(position, holder);

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
                // update the RecyclerView of deletions
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(),cardioStats.size());
                Toast.makeText(mContext,"Removed entry" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Helper method used to toggle the blood pressure flags based on if they are healthy values
     *
     * @param position The position of the cardioStat to check
     * @param holder The view holder
     */
    private void toggleBloodPressureFlags(int position, MyViewHolder holder) {
        if (cardioStats.get(position).getSystolicFlag()) {
            setWarningColors(holder.sysPres);
        } else {
            setNormalColors(holder.sysPres);
        }

        if (cardioStats.get(position).getDiastolicFlag()) {
            setWarningColors(holder.diaPres);
        } else {
            setNormalColors(holder.diaPres);
        }
    }

    /**
     * Helper method to set warning colors for unhealthy blood pressures
     *
     * @param tv The TextView to change
     */
    private void setWarningColors(TextView tv) {
        tv.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        tv.setBackground(ContextCompat.getDrawable(mContext,R.drawable.accent_rounded_background));
    }

    /**
     * Helper method to set warning colors for healthy blood pressures
     *
     * @param tv The TextView to change
     */
    private void setNormalColors(TextView tv) {
        tv.setTextColor(oldColors);
        tv.setBackground(ContextCompat.getDrawable(mContext,R.drawable.white_rounded_background));
    }

    /**
     * Method to return the size of the cardioStats dataset
     */
    @Override
    public int getItemCount() {
        return cardioStats.size();
    }


    /**
     * Helper method to correctly format dateTime
     */
    private String formatDateTime(LocalDateTime ldt) {
        return ldt.format(dateFormatter);
    }
}
