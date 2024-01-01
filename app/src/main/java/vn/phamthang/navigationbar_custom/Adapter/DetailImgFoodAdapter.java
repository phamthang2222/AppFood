package vn.phamthang.navigationbar_custom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.phamthang.navigationbar_custom.Model.DetailFoodPhoto;
import vn.phamthang.navigationbar_custom.R;

public class DetailImgFoodAdapter extends PagerAdapter {
    private Context mContext;
    private List<DetailFoodPhoto> mListPhotoDetail;

    public DetailImgFoodAdapter(Context mContext, List<DetailFoodPhoto> mListPhotoDetail) {
        this.mContext = mContext;
        this.mListPhotoDetail = mListPhotoDetail;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.detail_img_food_layout,container,false);
        ImageView imgPhoto = view.findViewById(R.id.imgPhotoDetail);
        DetailFoodPhoto photo = mListPhotoDetail.get(position);
        if(photo != null){
            Glide.with(mContext).load(photo.getUrlDetailFoodPhoto()).into(imgPhoto);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(mListPhotoDetail != null){
            return mListPhotoDetail.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
