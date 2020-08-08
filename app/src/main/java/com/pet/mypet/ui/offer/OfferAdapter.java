package com.pet.mypet.ui.offer;

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
import com.pet.mypet.ui.pets.PetDetailsinterface;

import java.util.ArrayList;

import at.blogc.android.views.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<OfferlistModel> offerlistModelArrayList;
    PetDetailsinterface petDetailsinterface;

    public OfferAdapter(Context context, ArrayList<OfferlistModel> offerlistModelArrayList, PetDetailsinterface petDetailsinterface) {
        this.mcontext = context;
        this.offerlistModelArrayList = offerlistModelArrayList;
        this.petDetailsinterface = petDetailsinterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offer_adapter, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        final OfferlistModel listModel = offerlistModelArrayList.get(position);

        viewHolder.tittle.setText(offerlistModelArrayList.get(position).getHeading());
        viewHolder.text_body.setText(offerlistModelArrayList.get(position).getDescription());
        viewHolder.text_valid.setText("Valid till "+offerlistModelArrayList.get(position).getEnddate());


        viewHolder.text_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.text_body.isExpanded()) {
                    viewHolder.text_body.collapse();
//                    buttonToggle.setText(R.string.expand);
                } else {
                    viewHolder.text_body.expand();
//                    buttonToggle.setText(R.string.collapse);
                }
            }
        });



//        viewHolder.eventslayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                petDetailsinterface.clickoncard(position,listModel.getNumber(), offerlistModelArrayList.get(position).getImage()
//                , offerlistModelArrayList.get(position).getName(), offerlistModelArrayList.get(position).getSpecies(),
//                        offerlistModelArrayList.get(position).getBreed(), offerlistModelArrayList.get(position).getColor(),
//                        offerlistModelArrayList.get(position).getSex(), offerlistModelArrayList.get(position).getDob(), offerlistModelArrayList.get(position).getWeight(),
//                        offerlistModelArrayList.get(position).getMicrochip());
//            }
//        });


        Log.e("IMAGE_URL",listModel.getImage());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.pet_image);
        requestOptions.error(R.drawable.pet_image);
        Glide.with(mcontext)
                .setDefaultRequestOptions(requestOptions)
                .load(listModel.getImage()).into(viewHolder.image);


    }

    @Override
    public int getItemCount() {
        return offerlistModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        LinearLayout eventslayout;
        TextView tittle,text_valid;
        ExpandableTextView text_body;
        RelativeLayout view_foreground;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tittle = itemView.findViewById(R.id.text_title);
            this.text_body = itemView.findViewById(R.id.text_subtitle);
            this.text_valid = itemView.findViewById(R.id.text_valid);
            this.image = itemView.findViewById(R.id.image1);
            this.eventslayout = itemView.findViewById(R.id.eventslayout);
            this.view_foreground = itemView.findViewById(R.id.view_foreground);

        }
    }
}