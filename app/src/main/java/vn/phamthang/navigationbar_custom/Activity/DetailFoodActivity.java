package vn.phamthang.navigationbar_custom.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import vn.phamthang.navigationbar_custom.R;

public class DetailFoodActivity extends AppCompatActivity {
    private TextView tvName, tvPrice,tvQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        initView();
        initData();
    }

    private void initView() {
        tvName = findViewById(R.id.tvFoodNameDetail);
        tvPrice = findViewById(R.id.tvFoodPriceDetail);
        tvQuantity = findViewById(R.id.tvFoodQuanityDetail);
    }

    private void initData() {
        Intent intent = new Intent();
//        String name =intent.getExtras().getString("FoodName");
//        String price =intent.getExtras().getString("FoodPrice");
//        String quantity =intent.getExtras().getString("FoodQuantity");
//
//        tvName.setText(name);
//        tvPrice.setText(price);
//        tvQuantity.setText(quantity);
    }
}