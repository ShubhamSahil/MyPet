package com.pet.mypet.appointment;

import android.content.Context;
import android.util.Log;
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
import com.pet.mypet.ui.pets.PetlistModel;
import com.pet.webservice.ClickAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<AppointmentModel> appointmentModelArrayList;
    AppointmentModelInterface clickAdapter;

    public AppointmentsAdapter(Context context, ArrayList<AppointmentModel> appointmentModelArrayList, AppointmentModelInterface clickAdapter) {
        this.mcontext = context;
        this.appointmentModelArrayList = appointmentModelArrayList;
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

        final AppointmentModel appointmentModel = appointmentModelArrayList.get(position);

        viewHolder.text_body.setText(appointmentModelArrayList.get(position).getClinic_name());
        viewHolder.tittle.setText("Appointment with "+appointmentModelArrayList.get(position).getAddress1());
        viewHolder.text_date.setText(appointmentModelArrayList.get(position).getPreferredDate()+ " "+appointmentModelArrayList.get(position).getPreferTime());
        viewHolder.eventslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAdapter.clickoncard(position,appointmentModel.getReq_number(),"Appointment with "+appointmentModelArrayList.get(position).getAddress1(),appointmentModel.getPatient_Img()
                        ,appointmentModelArrayList.get(position).getPreferredDate()+ " "+appointmentModelArrayList.get(position).getPreferTime(),appointmentModel.getClinic_name(),appointmentModel.getPatient_name()
                        ,appointmentModel.getVisitorContactNo(),appointmentModel.getMsg_disp());
            }
        });


//        Log.e("IMAGE_URL",appointmentModel.getImage());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.pet_image);
        requestOptions.error(R.drawable.pet_image);
        Glide.with(mcontext)
                .setDefaultRequestOptions(requestOptions)
                .load(appointmentModel.getPatient_Img()).into(viewHolder.image);
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
        return appointmentModelArrayList.size();
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