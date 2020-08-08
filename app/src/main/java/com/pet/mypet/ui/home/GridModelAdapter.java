package com.pet.mypet.ui.home;

import android.content.Context;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridModelAdapter extends RecyclerView.Adapter<GridModelAdapter.MyViewHolder> {

    private List<ServiceForRecyclerView> serviceForRecyclerViews;
    Context context;
    LinearLayout adapter;
    ClickAdapter clickAdapter;
    public static ArrayList<String> eventsliststring;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_titleinterest, year, genre;
        public TextView text_title,text_regprice,text_offprice,text_discount,text_save, text_review;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridviewed_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    public GridModelAdapter(List<ServiceForRecyclerView> serviceForRecyclerViews, Context context, ClickAdapter clickAdapter) {
        this.serviceForRecyclerViews = serviceForRecyclerViews;
        this.context = context;
        this.clickAdapter = clickAdapter;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        final ServiceForRecyclerView categoryModel = serviceForRecyclerViews.get(position);

//        adapter.setBackgroundColor(categoryModel.getColor());
//        Log.e("adapterresponse1",categoryModel.getName()+ " "+categoryModel.getIcon());
        holder.text_title.setText(categoryModel.getName());


        try {
            Picasso.with(context).load(categoryModel.getImage()).placeholder(context.getResources().getDrawable(R.drawable.app_icon)).into(holder.logo_image);
        }
        catch (Exception e){

        }

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAdapter.clickoncard(position,categoryModel.getName());
            }
        });

    }




    @Override
    public int getItemCount() {
        return serviceForRecyclerViews.size();
    }


}
