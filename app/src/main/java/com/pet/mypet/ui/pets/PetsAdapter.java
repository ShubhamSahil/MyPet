package com.pet.mypet.ui.pets;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pet.mypet.R;
import com.pet.webservice.ClickAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<PetlistModel> petlistModelArrayList;
    PetDetailsinterface petDetailsinterface;

    public PetsAdapter(Context context, ArrayList<PetlistModel> petlistModelArrayList, PetDetailsinterface petDetailsinterface) {
        this.mcontext = context;
        this.petlistModelArrayList = petlistModelArrayList;
        this.petDetailsinterface = petDetailsinterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pet_adapter, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final PetlistModel listModel = petlistModelArrayList.get(position);

        viewHolder.text_body.setText(petlistModelArrayList.get(position).getName());
        viewHolder.tittle.setText(petlistModelArrayList.get(position).getSpecies());
        viewHolder.view_foreground.setBackgroundColor(Color.parseColor(petlistModelArrayList.get(position).getIsActivated()));
        viewHolder.eventslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petDetailsinterface.clickoncard(position,listModel.getNumber(),petlistModelArrayList.get(position).getImage()
                ,petlistModelArrayList.get(position).getName(),petlistModelArrayList.get(position).getSpecies(),
                        petlistModelArrayList.get(position).getBreed(),petlistModelArrayList.get(position).getColor(),
                        petlistModelArrayList.get(position).getSex(),petlistModelArrayList.get(position).getDob(),petlistModelArrayList.get(position).getWeight(),
                        petlistModelArrayList.get(position).getMicrochip(),petlistModelArrayList.get(position).getEd_pdesexed());
            }
        });


        Log.e("IMAGE_URL",listModel.getImage());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.pet_image);
        requestOptions.error(R.drawable.pet_image);
        Glide.with(mcontext)
                .setDefaultRequestOptions(requestOptions)
                .load(listModel.getImage()).into(viewHolder.image);
//        Glide
//                .with(mcontext)
//                .load("https://"+listModel.getImage())
//                .placeholder(R.drawable.pet_image)
//                .error(R.drawable.pet_image)
//                .centerCrop()
//                .into(viewHolder.image);

    }

    @Override
    public int getItemCount() {
        return petlistModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        LinearLayout eventslayout;
        TextView tittle,text_body;
        RelativeLayout view_foreground;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tittle = itemView.findViewById(R.id.text_title);
            this.text_body = itemView.findViewById(R.id.text_subtitle);
            this.image = itemView.findViewById(R.id.image1);
            this.eventslayout = itemView.findViewById(R.id.eventslayout);
            this.view_foreground = itemView.findViewById(R.id.view_foreground);

        }
    }
}