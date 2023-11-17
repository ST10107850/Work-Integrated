package com.example.work_intergrated.ui.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.work_intergrated.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView profileImage;
    EditText name, email, phoneNumber, address;
    Button update;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile,container, false);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        name = root.findViewById(R.id.profile_name);
        email = root.findViewById(R.id.profile_email);
        phoneNumber = root.findViewById(R.id.profile_number);
        address = root.findViewById(R.id.profile_address);
        profileImage = root.findViewById(R.id.profile_img);

        update = root.findViewById(R.id.update);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });

        return root;
    }

    private void updateUserProfile() {
        String newName = name.getText().toString().trim();
        String newEmail = email.getText().toString().trim();
        String newPhoneNumber = phoneNumber.getText().toString().trim();
        String newAddress = address.getText().toString().trim();

        // Update the user's profile information in the database
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").document(auth.getUid())
                .update("name", newName, "email", newEmail, "phoneNumber", newPhoneNumber, "address", newAddress)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getType() != null) {
            Uri profileUri = data.getData();
            profileImage.setImageURI(profileUri);

            final StorageReference reference = storage.getReference().child("Profile_Images")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Image Uploaded successfully....", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}