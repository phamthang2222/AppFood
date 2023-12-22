package vn.phamthang.navigationbar_custom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.phamthang.navigationbar_custom.Model.User;

public class RegisActivity extends AppCompatActivity  {
    private EditText edtName, edtEmail, edtPass, edtRepeatPass;
    private Button btRegis, btBack;
    private FirebaseAuth firebaseAuth;
    private int index_idUser = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        firebaseAuth = FirebaseAuth.getInstance();
        init();
        onListener();
    }
    private void init(){
        edtName = findViewById(R.id.txtName);
        edtEmail = findViewById(R.id.txtEmail);
        edtPass = findViewById(R.id.txtPass);
        edtRepeatPass = findViewById(R.id.txtPass2);
        btRegis = findViewById(R.id.btRegis2);
        btBack = findViewById(R.id.btLogin2);
    }
    private void onListener(){
        btRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idUser = "IDUSER_" + index_idUser++;
                String email = edtEmail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String repeactPassword = edtRepeatPass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    edtEmail.setError("Không được để trống!");
                    return;
                }
                if(TextUtils.isEmpty(repeactPassword)){
                    edtEmail.setError("Không được để trống!");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    edtName.setError("Không được để trống!");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    edtPass.setError("Không được để trống!");
                    return;
                }
                if (password.length() < 6){
                    edtPass.setError("Mật khẩu lớn hơn 6 kí tự !");
                    return;
                }
                if (!password.equals(repeactPassword)){
                    edtRepeatPass.setError("Nhắc lại sai mật khẩu!");
                    return;
                }

                //đang ki trên fire base
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            // Tạo một đối tượng User với thông tin từ đăng ký
                            //User user = new User(name,email,password,"null");
                            User user = new User(idUser,name,email,password,"null");
                            // Lưu thông tin người dùng vào database (ví dụ: Firebase Realtime Database)
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                            databaseReference.child(firebaseUser.getUid()).setValue(user);

                            Toast.makeText(RegisActivity.this,"Đăng kí thành công!",Toast.LENGTH_SHORT).show();
                            //chuyển qua màn hình login vói intent
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();

                        }else{
                            Toast.makeText(RegisActivity.this,"Đăng kí thất bại!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }


}