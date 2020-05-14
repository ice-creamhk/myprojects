package com.example.travel01.ui.personalinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;

import com.example.travel01.Complaint;
import com.example.travel01.R;
import com.example.travel01.ShowComplaints;
import com.example.travel01.databinding.FragmentPersonalinfoBinding;
import com.example.travel01.signin;

public class PersonalFragment extends Fragment {

    private PersonalViewModel personalViewModel;
    View root;
    Button switch_bt;
    TextView nowname_tv;
    TextView nowidentify_tv;
    FragmentPersonalinfoBinding binding;
    TextView info_com_tv;
    Button info_com_bt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personalinfo,container,false);

        switch_bt=binding.switchBt;
        nowname_tv=binding.nowname;
        nowidentify_tv=binding.nowidentify;
        info_com_tv=binding.infoComTv;
        info_com_bt=binding.infoComBt;


        personalViewModel= ViewModelProviders.of(requireActivity(),new SavedStateViewModelFactory(getActivity().getApplication(),this)).get(PersonalViewModel.class);
        binding.setData(personalViewModel);
        binding.setLifecycleOwner(requireActivity());

        return binding.getRoot() ;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel

        switch_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), signin.class);
                startActivityForResult(intent,2);
            }
        });


        info_com_tv.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShowComplaints.class);
                intent.putExtra("name",nowname_tv.getText().toString());
                startActivity(intent);
            }
        });


        info_com_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Complaint.class);
                intent.putExtra("name",nowname_tv.getText().toString());
                startActivity(intent);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==3){
           String str1=data.getStringExtra("nowname");
            nowname_tv.setText(str1);
            nowidentify_tv.setText("普通会员");
            personalViewModel.setUsername(str1);
            personalViewModel.save();
        }

    }
}
