package com.example.resico;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.resico.databinding.ActivityMainBinding;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

	private ActivityMainBinding binding;
	private BottomNavigationView bottomNavView;
	private FragmentContainerView navHostView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		bottomNavView = binding.bottomNav;
		navHostView = binding.contentMain.navHostFragmentContentMain;

		NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
		assert navHostFragment != null;

		NavController navController = navHostFragment.getNavController();
		navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> toggleBottomNavBehavior(false));
		NavigationUI.setupWithNavController(bottomNavView, navController);
	}

	private void  toggleBottomNavBehavior(boolean scrollToHide) {
		// Get the layout params of the bottom nav
		CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) bottomNavView.getLayoutParams();
		params.setBehavior(scrollToHide ? new HideBottomViewOnScrollBehavior<CoordinatorLayout>(this, null) : null);

		// Add margin to Nav Host fragment if the layout behaviour is not added
		((ConstraintLayout.LayoutParams) navHostView.getLayoutParams()).setMargins(
				0, 0, 0, scrollToHide ? 0 : bottomNavView.getHeight()
		);
	}
}