package com.pet.mypet.ui.history;

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
import com.pet.mypet.appointment.AppointmentModelInterface;
import com.pet.mypet.ui.payment.PaymentModel;
import com.pet.mypet.ui.pets.PetDetailsinterface;
import com.pet.mypet.ui.pets.PetlistModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<HistoryModel> historyModelArrayList;
    AppointmentModelInterface petDetailsinterface;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyModelArrayList, AppointmentModelInterface petDetailsinterface) {
        this.mcontext = context;
        this.historyModelArrayList = historyModelArrayList;
        this.petDetailsinterface = petDetailsinterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.appointment_adapter, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final HistoryModel listModel = historyModelArrayList.get(position);

        viewHolder.text_body.setText(historyModelArrayList.get(position).getpName());
        viewHolder.tittle.setText("Debit amount " + historyModelArrayList.get(position).getDebitamount() + " SAR");
        viewHolder.text_date.setText(historyModelArrayList.get(position).getTrdate());
        viewHolder.eventslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petDetailsinterface.clickoncard(position, listModel.getpNumber(), "", "", "", ""
                        , "", "", listModel.getCnumber());
            }
        });

        viewHolder.image.setImageResource(R.drawable.money);

    }

    @Override
    public int getItemCount() {
        return historyModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        LinearLayout eventslayout;
        TextView tittle, text_body, text_date;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tittle = itemView.findViewById(R.id.text_title);
            this.text_body = itemView.findViewById(R.id.text_subtitle);
            this.text_date = itemView.findViewById(R.id.text_date);
            this.image = itemView.findViewById(R.id.image1);
            this.eventslayout = itemView.findViewById(R.id.eventslayout);

        }
    }
}