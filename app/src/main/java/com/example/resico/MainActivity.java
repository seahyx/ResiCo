package com.example.resico;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.resico.databinding.ActivityMainBinding;
import com.example.resico.ui.forum.ForumNewPostActivity;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
	private final String TAG = this.getClass().getSimpleName();
	private final ActivityResultLauncher<Intent> forumCreatePostLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> Log.d(TAG, "Returned from create new post page."));
	private ActivityMainBinding binding;
	private BottomNavigationView bottomNavView;
	private FloatingActionButton forumAddPostFab;
	private FragmentContainerView navHostView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		bottomNavView = binding.bottomNav;
		forumAddPostFab = binding.forumsAddPost;
		navHostView = binding.navHostFragmentContentMain;

		NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
		assert navHostFragment != null;

		NavController navController = navHostFragment.getNavController();
		NavigationUI.setupWithNavController(bottomNavView, navController);

		navController.addOnDestinationChangedListener(this::OnDestinationChanged);
		forumAddPostFab.setOnClickListener(view -> {
			Intent newPostIntent = new Intent(this, ForumNewPostActivity.class);
			forumCreatePostLauncher.launch(newPostIntent);
		});
	}

	private void OnDestinationChanged(NavController controller, NavDestination destination, Bundle args) {
		if (destination.getId() == R.id.nav_forums) {
			forumAddPostFab.show();
		} else {
			forumAddPostFab.hide();
		}

		CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) bottomNavView.getLayoutParams();
		HideBottomViewOnScrollBehavior<BottomNavigationView> scrollBehavior = (HideBottomViewOnScrollBehavior<BottomNavigationView>) params.getBehavior();
		if (scrollBehavior == null) return;
		scrollBehavior.slideUp(bottomNavView);
	}
}