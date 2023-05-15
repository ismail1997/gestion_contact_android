package com.ismail.gestion_contact.dialogFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ismail.gestion_contact.R;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    private TextView nameTextView;
    private TextView serviceTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle mArgs = getArguments();
        String nameContact = mArgs.getString("name");
        String serviceContact = mArgs.getString("service");

        View view = inflater.inflate(R.layout.contact_item_detail_layout,container,false);
        nameTextView=view.findViewById(R.id.name_contact_fragment_detail);
        serviceTextView=view.findViewById(R.id.service_contact_fragment_detail);


        nameTextView.setText(nameContact);
        serviceTextView.setText(serviceContact);
        return view;
    }
}
