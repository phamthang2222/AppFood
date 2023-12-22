package vn.phamthang.navigationbar_custom.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.phamthang.navigationbar_custom.R;

public class ChangePassWordDialogFragment extends DialogFragment {

    private EditText edtOlfPass, edtNewPass,edtRepeactNewPass;
    private Button btChange;
    public static String currentPasswordInFireBase;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d("tag","change pass");
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_pass_word, container, false);

        edtOlfPass = view.findViewById(R.id.txtUserPassword);
        edtNewPass = view.findViewById(R.id.txtNewPassword);
        edtRepeactNewPass = view.findViewById(R.id.txtcheckPassword);
        btChange = view.findViewById(R.id.btUpdatePW);

        onListener();
        return view;
    }
    private void onListener(){
        getCurrentPass();
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });
    }
    private void changePass(){

        String currentPassword = edtOlfPass.getText().toString().trim();
        String newPassword = edtNewPass.getText().toString().trim();
        String repeactPassword = edtRepeactNewPass.getText().toString().trim();

        //nếu pass nhập không đúng thì thông báo lỗi
        if(currentPasswordInFireBase.equals(currentPassword)){
            //kiểm tra việc nhập lại mật khẩu mới có trung nhau hay không
            if(newPassword.equals(repeactPassword)){
                // Thực hiện update mật khẩu mới trên Firebase
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Cập nhật mật khẩu mới
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Mật khẩu cập nhật thành công trong Firebase Authentication
                                    // Cập nhật mật khẩu trong Realtime Database
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                                            .child(user.getUid())
                                            .child("passWord");

                                    userRef.setValue(newPassword)
                                            .addOnCompleteListener(databaseTask -> {
                                                if (databaseTask.isSuccessful()) {
                                                    // Mật khẩu cập nhật thành công trong Realtime Database
                                                    getCurrentPass();
                                                    Toast.makeText(getContext(), "Mật khẩu đã được cập nhật", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Lỗi cập nhật mật khẩu trong Realtime Database
                                                    Toast.makeText(getContext(), "Lỗi cập nhật thông tin", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // Có lỗi xảy ra trong quá trình cập nhật mật khẩu
                                    Toast.makeText(getContext(), "Lỗi cập nhật ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }else{
                edtRepeactNewPass.setError("Nhập lại chưa đúng mật khẩu!");
            }
        }else{
            edtOlfPass.setError("Không đúng mật khẩu!");
        }

    }
    private void getCurrentPass() {
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
                    currentPasswordInFireBase = dataSnapshot.child("passWord").getValue(String.class);
                    Log.d("mk: ", currentPasswordInFireBase);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}