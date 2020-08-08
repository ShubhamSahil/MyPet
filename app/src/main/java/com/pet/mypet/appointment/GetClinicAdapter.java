package com.pet.mypet.appointment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class GetClinicAdapter extends RecyclerView.Adapter<GetClinicAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<GetClinicmodel> getClinicmodelArrayList;
    ClickAdapter clickAdapter;



    Double lat;
    Double longtitude;


    public GetClinicAdapter(Context context, ArrayList<GetClinicmodel> getClinicmodelArrayList , ClickAdapter clickAdapter) {
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

        final GetClinicmodel getClinicmodel = getClinicmodelArrayList.get(position);

        viewHolder.text_body.setText(getClinicmodelArrayList.get(position).getC_Address());
        viewHolder.tittle.setText(getClinicmodelArrayList.get(position).getClinicName());
//        viewHolder.text_distance.setText(getClinicmodelArrayList.get(position).getDistance());

        viewHolder.add_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAdapter.clickoncard(position,"contact"+getClinicmodel.getContact());
            }
        });

        viewHolder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    clickAdapter.clickoncard(position, getClinicmodel.getClinicPath());
                }

        });

        viewHolder.add_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getClinicmodelArrayList.get(position).getClinic_Code().equals("3")) {
                    Toast.makeText(mcontext, "Coming soon", Toast.LENGTH_LONG).show();

                } else {
                    clickAdapter.clickoncard(Integer.parseInt(getClinicmodel.getClinic_type()), "appointment" + getClinicmodel.getClinic_Code());
                }
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
        LinearLayout right;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tittle = itemView.findViewById(R.id.name);
            this.text_body = itemView.findViewById(R.id.sub_name);
            this.text_distance = itemView.findViewById(R.id.distance);
            this.add_call = itemView.findViewById(R.id.add_call);
            this.add_appointment = itemView.findViewById(R.id.add_appointment);
            this.right = itemView.findViewById(R.id.right);

        }
    }
}