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
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.resico.databinding.ActivityMainBinding;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

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
		navHostView = binding.navHostFragmentContentMain;

		NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
		assert navHostFragment != null;

		NavController navController = navHostFragment.getNavController();
		NavigationUI.setupWithNavController(bottomNavView, navController);

		navController.addOnDestinationChangedListener(this::OnDestinationChanged);
	}

	private void OnDestinationChanged(NavController controller, NavDestination destination, Bundle args) {
		CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) bottomNavView.getLayoutParams();
		HideBottomViewOnScrollBehavior<BottomNavigationView> scrollBehavior = (HideBottomViewOnScrollBehavior<BottomNavigationView>) params.getBehavior();
		if (scrollBehavior == null) return;
		scrollBehavior.slideUp(bottomNavView);
	}
}