package com.example.a7bitstask.user.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.a7bitstask.PermissionClass;
import com.example.a7bitstask.R;
import com.example.a7bitstask.home.MainActivity;
import com.example.a7bitstask.user.UserEntityData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ProfileCreateActivity extends AppCompatActivity {

    private static final int CAMER_OPTION = 101;
    private static final int GALLERY_OPTION = 111;

    private CircularImageView uploadeProfile_circularImageView;
    private ImageView cameraOptionImageView;
    private EditText nameEdt, emailEdt;
    private AppCompatButton saveButton;
    private ConstraintLayout constraintLayout;
    private String mName, mobielNumber, emailID, id;
    private TextView mobileNumberTxtView;

    private PermissionClass permissionsClass;
    DatabaseReference databaseReference;

    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create);

        uploadeProfile_circularImageView = findViewById(R.id.uploadeProfile_circularImageView);
        constraintLayout = findViewById(R.id.constraintLayout);
        cameraOptionImageView = findViewById(R.id.camera_option_imageView);
        nameEdt = findViewById(R.id.name_edt);
        emailEdt = findViewById(R.id.email_edt);
        saveButton = findViewById(R.id.saveButton);
        mobileNumberTxtView = findViewById(R.id.mobileNumber_txtView);

        mobielNumber = getIntent().getStringExtra("mobielNumber");
        Log.v("tag", "Profile Mobile Number" + mobielNumber);
        mobileNumberTxtView.setText(mobielNumber);


        permissionsClass = new PermissionClass(getApplicationContext());
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        cameraOptionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfileCreateActivity.this, permissionsClass.permissionsArray(), 200);
                } else {
                    selectImageOption();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = nameEdt.getText().toString().trim();
                emailID = emailEdt.getText().toString().trim();

                if (mName.length() == 0) {
                    Snackbar snackbar = Snackbar.make(constraintLayout, R.string.enter_Your_name, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (mName.length() < 3) {
                    Snackbar snackbar = Snackbar.make(constraintLayout, R.string.invalid_name, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (emailID.length() == 0) {
                    Snackbar snackbar = Snackbar.make(constraintLayout, R.string.input_email_id, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (!emailID.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
                    Snackbar snackbar = Snackbar.make(constraintLayout, R.string.invalide_email_id, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    onImageInsert();
                }
            }
        });


    }

    public void selectImageOption() {

        final CharSequence[] item = {"Camera", "Gallery", "Remove", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileCreateActivity.this);
        builder.setTitle("Profile Picture");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (item[position].equals("Camera")) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMER_OPTION);
                } else if (item[position].equals("Gallery")) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select File"), GALLERY_OPTION);
                } else if (item[position].equals("Remove")) {
                    uploadeProfile_circularImageView.setImageDrawable(getResources().getDrawable(R.drawable.login_icon));
                } else if (item[position].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMER_OPTION && resultCode == RESULT_OK) {

            Bitmap bitmapImage = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmapImage, "Title", null);
            filePath = Uri.parse(path);

            uploadeProfile_circularImageView.setImageBitmap(bitmapImage);


        } else if (requestCode == GALLERY_OPTION && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            uploadeProfile_circularImageView.setImageURI(filePath);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        }
    }


    public void onImageInsert() {
        progressDialog
                = new ProgressDialog(ProfileCreateActivity.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        id = databaseReference.push().getKey();
        StorageReference ref
                = storageReference
                .child("images/" + UUID.randomUUID().toString());

        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        if (uri != null) {
                            onDataProfileInsert(uri);
                            Log.v("tag", "valueOfImageRef" + uri);
                        } else {
                            Log.v("tag", "valueOfImageRef" + "Failed");
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast
                        .makeText(ProfileCreateActivity.this,
                                "Failed " + e.getMessage(),
                                Toast.LENGTH_SHORT)
                        .show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    public void onDataProfileInsert(Uri uri) {
        String imageURI = String.valueOf(uri);
        id = databaseReference.push().getKey();
        UserEntityData userEntityData = new UserEntityData(id, mName, mobielNumber, emailID, imageURI);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.child(id).setValue(userEntityData);

                if (snapshot.exists()) {
                    Log.v("tag", "UserDataInserted" + "Successfully");

                    Toast.makeText(ProfileCreateActivity.this, String.valueOf(R.string.profile_saved_message), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileCreateActivity.this, MainActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();


                } else {
                    Log.v("tag", "UserDataInserted" + "Failed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("tag", "UserData Resule" + "SomeWentWrong");
            }
        });
    }


}