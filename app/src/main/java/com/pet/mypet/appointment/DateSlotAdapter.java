package com.pet.mypet.appointment;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.mypet.R;
import com.pet.webservice.ClickAdapter;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by shubham on 28/09/18.
 */

public class DateSlotAdapter extends RecyclerView.Adapter<DateSlotAdapter.MyViewHolder> {

    private List<DateModel> dateModelList;
    Context context;
    ClickAdapter ClickAdapter;

    private int selectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_titleinterest, year, genre;
        public TextView text_month,text_date,text_day,text_discount,text_save;
        LinearLayout adapter;
        RadioButton radio_button;
        CardView edit;
        public ImageView logo_image;

        public MyViewHolder(View view) {
            super(view);
            text_month = (TextView) view.findViewById(R.id.text_month);
            text_date = (TextView) view.findViewById(R.id.text_date);
            text_day = (TextView) view.findViewById(R.id.text_day);
            adapter = (LinearLayout) view.findViewById(R.id.adapter);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_view, parent, false);
        return new MyViewHolder(itemView);
    }

    public DateSlotAdapter(List<DateModel> dateModelList, Context context, ClickAdapter ClickAdapter) {
        this.dateModelList = dateModelList;
        this.context = context;
        this.ClickAdapter = ClickAdapter;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DateModel dateModel = dateModelList.get(position);


        Date date = dateModelList.get(position).getDate();
        Log.e("event_date1",date.toString()+ "date ");
        holder.text_date.setText(getDate(date)+"");
        holder.text_day.setText(getDay(date)+"");
        holder.text_month.setText(getMonth(date)+"");




//        holder.radio_button.setTag(position);

        holder.adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String converteddatestr = DateTimeFormat.strdate(dateModel.getDate());
//                itemCheckChanged(v);
                ClickAdapter.clickoncard(position,"date"+converteddatestr);
                selectedPosition=position;
                notifyDataSetChanged();
//                itemCheckChanged(v);
            }
        });

        if(selectedPosition==position){
            holder.adapter.setBackgroundColor(Color.parseColor("#567845"));
//            holder.text_title.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {

            holder.adapter.setBackgroundColor(Color.parseColor("#9177FF"));
//            holder.text_title.setTextColor(Color.parseColor("#000000"));
        }
    }

    //On selecting any view set the current position to selectedPositon and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dateModelList.size();
    }

    public static String getDay(Date date) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
//        System.out.println();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
        return simpleDateformat.format(date);
    }
    public static int getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }
    public static String getMonth(Date date) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMM"); // the day of the week abbreviated
//        System.out.println();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
        return simpleDateformat.format(date);
//        String daqte=(String)  DateFormat.format("MMM",  date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        return daqte;
    }
}
