package vn.phamthang.navigationbar_custom.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import vn.phamthang.navigationbar_custom.Adapter.DetailImgFoodAdapter;
import vn.phamthang.navigationbar_custom.Model.DetailFoodPhoto;
import vn.phamthang.navigationbar_custom.R;

public class DetailFoodActivity extends AppCompatActivity {
    private TextView tvName, tvPrice,tvStatus, tvDes;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private DetailImgFoodAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        initView();
        initData();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPagerDetailImgFood);
        tvName = findViewById(R.id.tvFoodNameDetail);
        tvPrice = findViewById(R.id.tvFoodPriceDetail);
        tvStatus = findViewById(R.id.tvFoodStatusDetail);
        tvDes = findViewById(R.id.tvFoodDesDetail);
        circleIndicator = findViewById(R.id.circleIndicator);
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            String urlImg= intent.getStringExtra("FoodUrlImg");
            String name =intent.getStringExtra("FoodName");
            double price =intent.getDoubleExtra("FoodPrice",0);
            String description = intent.getStringExtra("FoodDes");
            String status =intent.getStringExtra("FoodStatus");

            slideDetailImg();

            tvName.setText(name);
            tvPrice.setText(String.format("%,.0f đ",price));
            tvStatus.setText(status);
            tvDes.setText(description);
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
        ArrayList<String> list = intent.getStringArrayListExtra("FoodListDetailImg");
        List<DetailFoodPhoto> listDetail = new ArrayList<>();
        // Duyệt qua từng URL trong list và khởi tạo và thêm DetailFoodPhoto vào listDetail
        for (String url : list) {
            DetailFoodPhoto detailPhoto = new DetailFoodPhoto();
            detailPhoto.setUrlDetailFoodPhoto(url);
            listDetail.add(detailPhoto);
        }
        return listDetail;
    }
}