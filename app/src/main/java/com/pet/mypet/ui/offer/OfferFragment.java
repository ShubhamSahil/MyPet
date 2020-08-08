package com.pet.mypet.ui.offer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.pet.mypet.R;
import com.pet.mypet.account.LoginActivity;
import com.pet.preference.PrefrenceUtils;

public class OfferFragment extends Fragment {

    private OfferViewModel offerViewModel;
    Context context;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        offerViewModel =
                ViewModelProviders.of(this).get(OfferViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);

//        startActivity(new Intent(getActivity(), LoginActivity.class));
//        PrefrenceUtils.writeString(getActivity(),PrefrenceUtils.PREF_DEVIC_TOKEN,"");
//        getActivity().finishAffinity();
        offerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }
}