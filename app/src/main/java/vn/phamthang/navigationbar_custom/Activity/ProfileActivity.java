package vn.phamthang.navigationbar_custom.Activity;

import static vn.phamthang.navigationbar_custom.Fragments.ChangePassWordDialogFragment.currentPasswordInFireBase;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.phamthang.navigationbar_custom.Dialog.ConfirmDialog;
import vn.phamthang.navigationbar_custom.Fragments.ChangePassWordDialogFragment;
import vn.phamthang.navigationbar_custom.Model.User;
import vn.phamthang.navigationbar_custom.R;

public class ProfileActivity extends AppCompatActivity {
    private Button btBack, btUpdate;
    private CardView btChangePassword;
    private EditText edtEmail, edtName, edtPhone;
    private TextView edtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        acctionNavigation();
        fillInformationUserFromFireBase();
        onListener();
        // Thiết lập ActionBar

    }
    private void init(){
        btBack =findViewById(R.id.btBackProfile);
        btUpdate = findViewById(R.id.btUpdateProfile);
        btChangePassword = findViewById(R.id.btChangePassWord);

        edtEmail = findViewById(R.id.txtUserEmail);
        edtName = findViewById(R.id.txtUserName);
        edtPhone = findViewById(R.id.txtUserPhone);
        edtPass = findViewById(R.id.txtUserPassword);

        edtEmail.setInputType(InputType.TYPE_CLASS_TEXT);
        edtName.setInputType(InputType.TYPE_CLASS_TEXT);
        edtPhone.setInputType(InputType.TYPE_CLASS_TEXT);
    }
    private void onListener() {
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();


            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail, newName, newPhone,newPass;
                newEmail = edtEmail.getText().toString().trim();
                newName = edtName.getText().toString().trim();
                newPhone = edtPhone.getText().toString().trim();
                newPass = currentPasswordInFireBase;

                User updateUser = new User(newName, newEmail,newPass,newPhone);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();

                // Tham chiếu đến nút của người dùng hiện tại trong Realtime Database
                DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                // Cập nhật thông tin người dùng
                currentUserRef.setValue(updateUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                // Cập nhật thành công
                                Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi có lỗi xảy ra
                                Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the fragment
                ChangePassWordDialogFragment changePasswordFragment = new ChangePassWordDialogFragment();

                // Get the FragmentManager
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Start a transaction
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Show the fragment
                changePasswordFragment.show(transaction, "change_password_dialog");

            }
        });
    }
    private void acctionNavigation(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Kích hoạt mũi tên quay lại
            actionBar.setTitle("Thông tin cá nhân"); // Đặt tiêu đề nếu cần
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void showConfirmationDialog() {
        ConfirmDialog dialogManager = new ConfirmDialog(getApplicationContext());
        dialogManager.showConfirmationDialogAtProfileActivity(this);
    }

    private void fillInformationUserFromFireBase(){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Người dùng đã đăng nhập, lấy UID
        String uId = currentUser.getUid();

        // Tham chiếu đến nút "users" trong Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Tham chiếu đến nút con có UID tương ứng với người dùng đã đăng nhập
        DatabaseReference currentUserRef = usersRef.child(uId);

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Kiểm tra xem có dữ liệu cho người dùng hiện tại hay không
                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu từ nút con của người dùng hiện tại
                    String username = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phoneNumber").getValue(String.class);
                    String pass = dataSnapshot.child("passWord").getValue(String.class);

                    edtEmail.setText(email);
                    edtName.setText(username);
                    edtPhone.setText(phone);
                    edtPass.setText(pass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setTitle(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Thông tin cá nhân");
        }
    }

}