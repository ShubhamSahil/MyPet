package com.pet.mypet.appointment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pet.mypet.R;
import com.pet.webservice.ClickAdapter;

import java.util.ArrayList;

public class GridTimeModelAdapter extends RecyclerView.Adapter<GridTimeModelAdapter.MyViewHolder> {

        private ArrayList<TimeModel> timeModelList;
        Context context;
        ClickAdapter clickAdapter;

        private int selectedPosition = -1;
        public static ArrayList<String> eventsliststring;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView image_titleinterest, year, genre;
            public TextView text_title,text_regprice,text_offprice,text_discount,text_save, text_review;
            LinearLayout adapter;
            public ImageView logo_image,ic_delete;
            RatingBar rating_bar;

            public MyViewHolder(View view) {
                super(view);

                logo_image = (ImageView) view.findViewById(R.id.image_tile);
                text_title = (TextView) view.findViewById(R.id.text_title);
                adapter = (LinearLayout) view.findViewById(R.id.adapter);

            }
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridviewedtime_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

    public GridTimeModelAdapter(ArrayList<TimeModel> timeModelList, Context context, ClickAdapter clickAdapter) {
            this.timeModelList = timeModelList;
            this.context = context;
            this.clickAdapter = clickAdapter;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        final EditTimeModel timeModel = timeModelList.get(position);

//        Log.e("adapterresponse1",timeModel.getTimeslot());
            holder.text_title.setText(timeModelList.get(position).getTimeslot().toString());
//            if (position ==selectedPosition) {
//                adapter.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//
//            }
//            else {
////            adapter.setBackgroundColor(context.getResources().getColor(R.color.wh));
//
//            }

//            holder.adapter.setTag(position);


            holder.adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickAdapter.clickoncard(position,"time"+timeModelList.get(position).getStrtime()+"");
                    selectedPosition=position;
                    notifyDataSetChanged();
//                itemCheckChanged(view);
                }
            });

            if(selectedPosition==position){
                holder.adapter.setBackgroundColor(Color.parseColor("#567845"));
                holder.text_title.setTextColor(Color.parseColor("#ffffff"));
            }
            else
            {
                holder.adapter.setBackgroundDrawable(context.getDrawable(R.drawable.customborder));
                holder.text_title.setTextColor(Color.parseColor("#000000"));
            }


        }

        private void itemCheckChanged(View v) {
            selectedPosition = (Integer) v.getTag();
            notifyDataSetChanged();
        }


        @Override
        public int getItemCount() {
            return timeModelList.size();
        }
    }

