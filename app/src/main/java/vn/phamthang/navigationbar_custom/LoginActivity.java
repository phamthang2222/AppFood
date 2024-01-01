package vn.phamthang.navigationbar_custom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.phamthang.navigationbar_custom.Model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText edtPass,edtEmail;
    private Button btLogin;
    private LinearLayout tvRegis;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        setTitle("Trang chủ");
        // khởi tạo acc cho nhanh
        init();
        initAccout();
        onListener();

    }

    private void init(){
        edtEmail = findViewById(R.id.txtEmail);
        edtPass = findViewById(R.id.txtPass);
        btLogin = findViewById(R.id.btLogin);
        tvRegis = findViewById(R.id.btRegis);
    }
    private void onListener(){

        //đăng kí
        tvRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisActivity.class)); // chuyển sang màn hình register

            }
        });

        //đăng nhập
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    edtEmail.setError("Chưa nhập email");
                    return;
                }
                else if(TextUtils.isEmpty(email)){
                    edtPass.setError("Chưa nhập mật khẩu");
                    return;
                }
                else if (password.length() < 6){
                    edtPass.setError("Mật khẩu lớn hơn 6 kí tự !");
                    return;
                }
                if(firebaseAuth != null){
                    //đang nhập trên fire base
                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                                                .child(firebaseUser.getUid());
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    User user = snapshot.getValue(User.class);

                                                    // Tạo Intent và đính kèm dữ liệu người dùng
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.putExtra("user", user);

                                                    // Chuyển sang màn hình mới
                                                    startActivity(intent);
                                                    finish(); // Để ngăn chặn người dùng quay lại màn hình đăng nhập
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        //thông báo
                                        Toast.makeText(LoginActivity.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();

                                    }else{
                                        Toast.makeText(LoginActivity.this,"Sai tên đăng nhập hoặc mật khẩu!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this,"Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    private void initAccout(){
        String email = "t1@gmail.com";
        String pass = "123456";
        edtEmail.setText(email);
        edtPass.setText(pass);

    }

}