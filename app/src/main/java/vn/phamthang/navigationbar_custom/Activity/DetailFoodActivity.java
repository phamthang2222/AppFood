package vn.phamthang.navigationbar_custom.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import vn.phamthang.navigationbar_custom.Adapter.DetailImgFoodAdapter;
import vn.phamthang.navigationbar_custom.Helper.UpdateItemInRealtime;
import vn.phamthang.navigationbar_custom.Model.DetailFoodPhoto;
import vn.phamthang.navigationbar_custom.Model.FavFood;
import vn.phamthang.navigationbar_custom.Model.Food;
import vn.phamthang.navigationbar_custom.R;

public class DetailFoodActivity extends AppCompatActivity {
    private TextView tvName, tvPrice,tvStatus, tvDes;
    private Button btAddToCart;
    private ImageButton btAddToFav;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private DetailImgFoodAdapter photoAdapter;
    private boolean isFav,isSale;
    private String name,urlImg,description,status, childKey,uniqueId;
    private Double price;
    private FavFood favFood;
    private ArrayList<String> listImgDetail;
    private ArrayList<FavFood> listFavFood;
    private boolean isActionRequested  = false;
    private Handler handler = new Handler();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("ListFavFood");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        initView();
        initData();
        onClick();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPagerDetailImgFood);
        tvName = findViewById(R.id.tvFoodNameDetail);
        tvPrice = findViewById(R.id.tvFoodPriceDetail);
        tvStatus = findViewById(R.id.tvFoodStatusDetail);
        tvDes = findViewById(R.id.tvFoodDesDetail);
        btAddToCart =findViewById(R.id.btAddToCart);
        btAddToFav = findViewById(R.id.btAddFavFood);
        circleIndicator = findViewById(R.id.circleIndicator);
    }

    private void initData() {
        listFavFood = new ArrayList<>();
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            urlImg= intent.getStringExtra("FoodUrlImg");
            name =intent.getStringExtra("FoodName");
            price =intent.getDoubleExtra("FoodPrice",0);
            description = intent.getStringExtra("FoodDes");
            status =intent.getStringExtra("FoodStatus");
            isFav =intent.getBooleanExtra("IsFoodFav",false);
            isSale = intent.getBooleanExtra("IsFoodSale",false);
            childKey = intent.getStringExtra("childKey");
            slideDetailImg();

            favFood = new FavFood(getIdUser(), urlImg, name, status, description, price, listImgDetail, isFav, isSale);

            tvName.setText(name);
            tvPrice.setText(String.format("%,.0f đ",price));
            tvStatus.setText(status);
            tvDes.setText(description);

            Toast.makeText(DetailFoodActivity.this," "+childKey,Toast.LENGTH_SHORT).show();
        }else{
            tvName.setText("Chua co du lieu");
        }

    }

    private void slideDetailImg() {
        photoAdapter = new DetailImgFoodAdapter(DetailFoodActivity.this,getListPhoto());
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    private List<DetailFoodPhoto> getListPhoto() {
        Intent intent = getIntent();
        listImgDetail = intent.getStringArrayListExtra("FoodListDetailImg");
        List<DetailFoodPhoto> listDetail = new ArrayList<>();
        // Duyệt qua từng URL trong list và khởi tạo và thêm DetailFoodPhoto vào listDetail
        for (String url : listImgDetail) {
            DetailFoodPhoto detailPhoto = new DetailFoodPhoto();
            detailPhoto.setUrlDetailFoodPhoto(url);
            listDetail.add(detailPhoto);
        }
        return listDetail;
    }
    private void onClick(){
        btAddToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isActionRequested) { // Nếu chưa có hành động nào được yêu cầu.
                    isActionRequested = true; // Đặt flag là có hành động được yêu cầu.
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            action1();
                            isActionRequested = false; // Đặt lại flag.
                        }
                    }, 2000);  // 2 giây.
                } else {

                }
            }
        });
    }

    private void action1() {
        if(favFood.isFav() == false){
            listFavFood.add(favFood);
            favFood.setFav(true);
            addFavFoodToFirebase(favFood);
        }else if(favFood.isFav()== true){
            removeFavFoodByNameAndUidFromFirebase(favFood.getName(), getIdUser());
            listFavFood.remove(favFood);
        }

        for(int i =0; i<listFavFood.size();i++){
            Log.d("FavFoodSize",String.valueOf(listFavFood.size()));
        }
    }
    private String getIdUser(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        return uid;
    }
    private void addFavFoodToFirebase(FavFood favFood) {
        String uid = getIdUser();
        uniqueId = myRef.child(uid).push().getKey();

        myRef.child(uid).child(uniqueId).setValue(favFood)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailFoodActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private void removeFavFoodByNameAndUidFromFirebase(String foodName, String uid) {
        Toast.makeText(getApplicationContext(),""+uniqueId,Toast.LENGTH_SHORT).show();
        Query query = myRef.child(uid).child(uniqueId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().child(uid).child(uniqueId).removeValue() // Xóa mục này từ Firebase
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(DetailFoodActivity.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetailFoodActivity.this, "Lỗi xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}