package vn.phamthang.navigationbar_custom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.phamthang.navigationbar_custom.Activity.ProfileActivity;
import vn.phamthang.navigationbar_custom.Fragments.FavoriteFragment;
import vn.phamthang.navigationbar_custom.Fragments.HomeFragment;
import vn.phamthang.navigationbar_custom.Fragments.OpenFragment;
import vn.phamthang.navigationbar_custom.Model.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView nameUserNavigation, emailUserNavigation;
    private BottomNavigationView bottomNavigationView;
    private OpenFragment openFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ánh xạ và sự kiện lắng nghe của các button
        init();
        onListner();
        //navigation
        acctionNavigation();
        // set thông tin trong navigation
        setInformationNavigation();
        //navigation bottom
        acctionBottomNavigation();
        //

        openFragment = new OpenFragment();
        openFragment.replaceFragment(MainActivity.this, new HomeFragment(),"Fragment_home");
        openFragment.setTitle(MainActivity.this);
        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
    }

    private void init(){
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Lấy header layout của NavigationView
        View headerView = navigationView.getHeaderView(0);
        nameUserNavigation = headerView.findViewById(R.id.nameUser_nav);
        emailUserNavigation = headerView.findViewById(R.id.emailUser_nav);

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
    }

    private void onListner(){

    }
    private void acctionNavigation(){
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,
                R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setInformationNavigation(){
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            User user = (User) intent.getSerializableExtra("user");
            // Hiển thị thông tin trên các TextView
            nameUserNavigation.setText(user.getName());
            emailUserNavigation.setText(user.getEmail());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.nav_home){
            openFragment.openHomeFragment(MainActivity.this);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);

        } else if (id == R.id.nav_fav) {
            openFragment.openFavoriteFragment(MainActivity.this);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_fav).setChecked(true); // Update the selection in bottom navigation

        } else if (id == R.id.nav_his) {

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        }else if (id == R.id.nav_logout) {
            logout();
        }

        openFragment.setTitle(MainActivity.this);

        // khi chọn 1 mục sẽ đóng drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            showConfirmationDialog();

        }
    }
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
        // Chuyển đến màn hình đăng nhập hoặc màn hình chào mừng tùy thuộc vào thiết kế của ứng dụng
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish(); // Đóng màn hình hiện tại để ngăn chặn việc quay lại
    }
    private void showConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn muốn thoát ứng dụng?");

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng hủy bỏ
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void acctionBottomNavigation(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_nav_home:
                        openFragment.openHomeFragment(MainActivity.this);
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
                        updateToolbarSelection(R.id.nav_home);
                        return true;
                    case R.id.bottom_nav_fav:
                        openFragment.openFavoriteFragment(MainActivity.this);
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_fav).setChecked(true);
                        updateToolbarSelection(R.id.nav_fav);
                        return true;
                }
                openFragment.setTitle(MainActivity.this);
                return  false;
            }
        });
    }
    private void updateToolbarSelection(int itemId) {
        navigationView.setCheckedItem(itemId);
        onNavigationItemSelected(navigationView.getMenu().findItem(itemId));
    }
    public void pauseFrag(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Tạm ngừng fragment1
        Fragment fragment1 = fragmentManager.findFragmentByTag("Fragment_home");
        if (fragment1 != null) {
            fragmentManager.beginTransaction().detach(fragment1).commit();
        }
        // Tạm ngừng fragment2
        Fragment fragment2 = fragmentManager.findFragmentByTag("Fragment_fav");
        if (fragment2 != null) {
            fragmentManager.beginTransaction().detach(fragment2).commit();
        }
    }
    public void attachFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment1 = fragmentManager.findFragmentByTag("Fragment_home");
        Fragment fragment2 = fragmentManager.findFragmentByTag("Fragment_fav");
        if (fragment1 != null) {
            fragmentManager.beginTransaction().attach(fragment1).commit();
        }

        // Kích hoạt lại fragment2
        if (fragment2 != null) {
            fragmentManager.beginTransaction().attach(fragment2).commit();
        }
    }

}