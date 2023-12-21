package vn.phamthang.navigationbar_custom.Fragments;

import android.content.Context;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vn.phamthang.navigationbar_custom.R;

public class OpenFragment {
    private static final int FRAHMENT_HOME = 1;
    private static final int FRAHMENT_FAVORITE = 2;
    private int currentFragment = 1 ;

    public void openHomeFragment(AppCompatActivity activity){
        if(currentFragment != FRAHMENT_HOME){
            replaceFragment(activity, new HomeFragment(),"Fragment_home");
            currentFragment = FRAHMENT_HOME;
        }
    }
    public void openFavoriteFragment(AppCompatActivity activity){
        if(currentFragment != FRAHMENT_FAVORITE){
            replaceFragment(activity,new FavoriteFragment(),"Fragment_fav");
            currentFragment = FRAHMENT_FAVORITE;
        }
    }
    public void replaceFragment(AppCompatActivity activity, Fragment fragment,String tag){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main,fragment,tag);
        transaction.commit();
    }

    public void setTitle(Context context){
        String title ="";
//        ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();

        switch (currentFragment){
            case FRAHMENT_HOME:
                title = context.getString(R.string.nav_home);
                break;
            case FRAHMENT_FAVORITE:
                title = context.getString(R.string.nav_fav);
                break;

        }
        if(((AppCompatActivity) context).getSupportActionBar() != null){
            ((AppCompatActivity) context).getSupportActionBar().setTitle(title);
        }
    }
    public void setTitleFavorite(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Yêu thích"); // Set the desired title for the Favorite fragment
        }
    }
    public void setTitleHome(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Trang chủ"); // Set the desired title for the Favorite fragment
        }
    }
}
