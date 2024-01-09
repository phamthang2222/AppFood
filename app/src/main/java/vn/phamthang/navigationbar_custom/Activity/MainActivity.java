package vn.phamthang.navigationbar_custom.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import vn.phamthang.navigationbar_custom.Dialog.ConfirmDialog;
import vn.phamthang.navigationbar_custom.Fragments.HomeFragment;
import vn.phamthang.navigationbar_custom.Fragments.OpenFragment;
import vn.phamthang.navigationbar_custom.Model.User;
import vn.phamthang.navigationbar_custom.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView nameUserNavigation, emailUserNavigation;
    private BottomNavigationView bottomNavigationView;
    private OpenFragment openFragment;
    private SearchView searchView;

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

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            User user = (User) intent.getSerializableExtra("user");

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
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
    private void showConfirmationDialog() {
        ConfirmDialog dialogManager = new ConfirmDialog(MainActivity.this);
        dialogManager.showConfirmationDialog(this);
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
}