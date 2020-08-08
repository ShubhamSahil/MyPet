package com.pet.mypet.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.pet.mypet.R;
import com.squareup.picasso.Picasso;

public class PagerFragment1 extends Fragment{
    private String title;
    private int page;
    static Context context1;



        public static PagerFragment1 newInstance (String aaya, Context context){

        PagerFragment1 fragment = new PagerFragment1();
        context1 = context;
//        clickAdapter1=clickAdapter;
        Bundle args = new Bundle();
        args.putString("AAYA", aaya);
        fragment.setArguments(args);
        return fragment;


}
    public PagerFragment1() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String aaya = this.getArguments().getString("AAYA");
        View view = inflater.inflate(R.layout.view_adv, container, false);
//        TextView tv = (TextView) view.findViewById(R.id.textView);
        ImageView imageView = view.findViewById(R.id.textView);
//        imageView.setText(aaya);
//        TextView tsura = (TextView) view.findViewById(R.id.sura);
//        TextView tayah = (TextView) view.findViewById(R.id.ayah);
        try {
            Picasso.with(context1).load(aaya).into(imageView);
        }
        catch (Exception e){
            e.printStackTrace();
        }

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickAdapter1.click("","");
//            }
//        });
        return view;
    }
}
