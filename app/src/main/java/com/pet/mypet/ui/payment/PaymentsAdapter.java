package com.pet.mypet.ui.payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pet.mypet.R;
import com.pet.mypet.appointment.AppointmentModel;
import com.pet.mypet.appointment.AppointmentModelInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<PaymentModel> paymentModelArrayList;
    AppointmentModelInterface clickAdapter;

    public PaymentsAdapter(Context context, ArrayList<PaymentModel> paymentModelArrayList, AppointmentModelInterface clickAdapter) {
        this.mcontext = context;
        this.paymentModelArrayList = paymentModelArrayList;
        this.clickAdapter = clickAdapter;
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

        final PaymentModel appointmentModel = paymentModelArrayList.get(position);

        viewHolder.text_body.setText(paymentModelArrayList.get(position).getRemarks());
        viewHolder.tittle.setText("Amount due "+paymentModelArrayList.get(position).getAmount_due()+ " SAR");
        viewHolder.text_date.setText(paymentModelArrayList.get(position).getReceiveTime());
        viewHolder.eventslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAdapter.clickoncard(position,appointmentModel.getId(),"","","",""
                ,"","",paymentModelArrayList.get(position).getAmount_due());
            }
        });

        viewHolder.image.setImageResource(R.drawable.money);


    }

    @Override
    public int getItemCount() {
        return paymentModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        LinearLayout eventslayout;
        TextView tittle,text_body,text_date;
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