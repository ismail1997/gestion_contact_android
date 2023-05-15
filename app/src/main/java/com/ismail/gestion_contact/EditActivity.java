package com.ismail.gestion_contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditActivity extends AppCompatActivity {


    private EditText editName,editEmail,editService,editPhone;

    private Button editButton;
    private FirebaseFirestore firestore;

    private String id;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Contact");

        firestore=FirebaseFirestore.getInstance();
        pd=new ProgressDialog(this);

        editName=findViewById(R.id.edit_name_contact_edittext);
        editEmail=findViewById(R.id.edit_email_contact_edittext);
        editService=findViewById(R.id.edit_service_contact_edittext);
        editPhone=findViewById(R.id.edit_phone_contact_edittext);

        Intent intent = getIntent();

        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        String phone =intent.getStringExtra("phone");
        String service = intent.getStringExtra("service");
        id=intent.getStringExtra("id");

        editName.setText(name, TextView.BufferType.EDITABLE);
        editEmail.setText(email, TextView.BufferType.EDITABLE);
        editService.setText(service, TextView.BufferType.EDITABLE);
        editPhone.setText(phone, TextView.BufferType.EDITABLE);


        editButton=findViewById(R.id.edit_contact_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm = editName.getText().toString();
                String em = editEmail.getText().toString();
                String ph = editPhone.getText().toString();
                String sr = editService.getText().toString();

                updateConcat(id,nm,em,ph,sr);
            }
        });

    }

    private void updateConcat(String id,String name, String email, String phone, String service) {
        pd.setTitle("Adding data to firebase");
        pd.show();

        firestore.collection("Contacts").document(id).
                update("name",name,"email",email,"service",service,"phone",phone)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(EditActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(EditActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}