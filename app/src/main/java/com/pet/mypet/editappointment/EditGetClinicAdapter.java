package com.pet.mypet.editappointment;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.mypet.R;
import com.pet.mypet.appointment.GetClinicmodel;
import com.pet.webservice.ClickAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditGetClinicAdapter extends RecyclerView.Adapter<EditGetClinicAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<EditGetClinicmodel> getClinicmodelArrayList;
    ClickAdapter clickAdapter;

    Double lat;
    Double longtitude;


    public EditGetClinicAdapter(Context context, ArrayList<EditGetClinicmodel> getClinicmodelArrayList , ClickAdapter clickAdapter) {
        this.mcontext = context;
        this.getClinicmodelArrayList = getClinicmodelArrayList;
        this.clickAdapter = clickAdapter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clinic_listadapter, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final EditGetClinicmodel getClinicmodel = getClinicmodelArrayList.get(position);

        viewHolder.text_body.setText(getClinicmodelArrayList.get(position).getC_Address());
        viewHolder.tittle.setText(getClinicmodelArrayList.get(position).getClinicName());
        viewHolder.text_distance.setText(getClinicmodelArrayList.get(position).getDistance());
        viewHolder.row.setBackgroundColor(Color.parseColor(getClinicmodelArrayList.get(position).getColorcode()));
        viewHolder.add_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAdapter.clickoncard(position,"contact"+getClinicmodel.getContact());
            }
        });

        viewHolder.add_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAdapter.clickoncard(Integer.parseInt(getClinicmodel.getClinic_type()),"appointment"+getClinicmodel.getClinic_Code());
            }
        });


        Log.e("IMAGE_URL",getClinicmodelArrayList.get(position).getClinicName());


    }

    @Override
    public int getItemCount() {
        return getClinicmodelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView tittle,text_body,text_distance;

        Button add_call,add_appointment;
        LinearLayout row;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tittle = itemView.findViewById(R.id.name);
            this.text_body = itemView.findViewById(R.id.sub_name);
            this.text_distance = itemView.findViewById(R.id.distance);
            this.add_call = itemView.findViewById(R.id.add_call);
            this.add_appointment = itemView.findViewById(R.id.add_appointment);
            this.row = itemView.findViewById(R.id.row);

        }
    }
}