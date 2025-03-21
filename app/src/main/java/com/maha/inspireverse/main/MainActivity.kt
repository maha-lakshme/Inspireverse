package com.maha.inspireverse.main

import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GestureDetectorCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.chip.Chip
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.FirebaseApp
import com.maha.inspireverse.R
import com.maha.inspireverse.adapter.QuoteAndPicsAdapter
import com.maha.inspireverse.data.RetrofitInstance
import com.maha.inspireverse.databinding.ActivityMainBinding
import com.maha.inspireverse.repository.AuthRepository
import com.maha.inspireverse.repository.FireBaseAuthRepository
import com.maha.inspireverse.repository.FireStoreRepository
import com.maha.inspireverse.repository.FirebaseFireStoreRepositroy
import com.maha.inspireverse.repository.RetrofitQuoteRepository
import com.maha.inspireverse.viewmodel.QuotesViewModel
import com.maha.inspireverse.viewmodel.QuotesViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    // Create instances of APIs
    val quotesApi = RetrofitInstance.quotesApi
    val pexelsApi = RetrofitInstance.pexelsApi

    private var isHomeVisible = false
    val fireStoreRepository = FirebaseFireStoreRepositroy()
    val authRepository = FireBaseAuthRepository()
    val quoteRepository = RetrofitQuoteRepository(quotesApi,pexelsApi)
    val viewModel: QuotesViewModel by lazy {
        ViewModelProvider(this, QuotesViewModelFactory(authRepository,fireStoreRepository,quoteRepository))[QuotesViewModel::class.java]
    }
    private lateinit var gestureDetector: GestureDetectorCompat
//    private lateinit var adapter: QuoteAndPicsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        // Set up the toolbar
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        viewModel.bottomNavVisibility.observe(this) { isVisible ->
            Log.e("CLICKED VIEWPAGER---", isVisible.toString())
            binding.bottomNavigationView.visibility =  if (isVisible) View.VISIBLE else View.GONE
        }

        // Set up the GestureDetector to detect single taps
        gestureDetector =
            GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    // Toggle visibility
                    val currentVisibility = viewModel.bottomNavVisibility.value ?: true
                    viewModel.setBottomNavVisibility(!currentVisibility) // Toggle the state
                    return true
                }


            })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }


        // Find the NavHostFragment and get the NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.labelVisibilityMode =
            NavigationBarView.LABEL_VISIBILITY_UNLABELED

        // Set up BottomNavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        // Optional: Handle destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            isHomeVisible = destination.id == R.id.navigation_home
            when (destination.id) {
                R.id.navigation_home -> {
                    //       binding.chipGroupCategories.visibility = View.VISIBLE   // Actions for home destination
                    binding.appBarLayout.visibility = View.GONE

                }

                R.id.navigation_theme -> {
                    viewModel.setBottomNavVisibility(true)
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.toolbarTitle.text = "Themes"

                }

                R.id.navigation_storage -> {
                    //      binding.chipGroupCategories.visibility = View.GONE
                    // Actions for storage destination
                    binding.bottomNavigationView.visibility = View.VISIBLE

                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.toolbarTitle.text = "Account"

                }
            }


        }

        //       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // For Android 11 and above
//            window.setDecorFitsSystemWindows(false)
//            window.insetsController?.apply {
//                // Set light icons for both status and navigation bars
//                setSystemBarsAppearance(
//                    0, // Remove APPEARANCE_LIGHT_STATUS_BARS for light icons
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//                )
//                setSystemBarsAppearance(
//                    0, // Remove APPEARANCE_LIGHT_NAVIGATION_BARS for light icons
//                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
//                )
//            }
//        } else{            // For Android 10 and below
//            window.decorView.systemUiVisibility = (
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//            0  // dark icons on a light background
//                    )
//            // Optionally, set the navigation bar icon style:
//            window.decorView.systemUiVisibility = (
//                    window.decorView.systemUiVisibility and
//                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() // Clear light status bar flag
//                    )
//        }


//        binding.chipGroupCategories.setOnCheckedChangeListener { group, checkedId ->
//            Log.d("CHIPS CATEGORY CLICKED---",checkedId.toString())
//            if (checkedId != View.NO_ID) {
//                val chip: Chip = group.findViewById(checkedId)
//                val selectedCategory = chip.text.toString()
//                // Call fetchContent with the selected category
//                viewModel.fetchNewContent(selectedCategory)
//            }
//        }


        //ProgressBar
//        viewModel.isLoading.observe(this) { isLoading ->
//            if (isLoading) {
//              println("progredss")
//            } else {
//                println("progredss")
//
//            }
//        }
//    }

//
//        viewModel.error.observe(this, Observer {
//            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
//            Log.e("Error", it)
//        })
//
//        // Set up RecyclerView
//        val recyclerView: RecyclerView = binding.recyclerView
//        recyclerView.layoutManager = GridLayoutManager(this, 2)
//
//        //Getting the quotes and pics from api
//        viewModel.imagesWithQuotes.observe(this, Observer { items ->
//            adapter = QuoteAndPicsAdapter(items){quoteId,isSaved->
//                viewModel.toggleLike(quoteId,isSaved)
//            }
//            recyclerView.adapter = adapter
//
//        })


//        viewModel.images.observe(this, Observer { images ->
//            if (::adapter.isInitialized) {
//                Log.d("MainViewModel", "Adapter inlized")
//                adapter.notifyDataSetChanged()
//            } else {
//                adapter = ImagesAdapter(images)
//                Log.d("MainViewModel", "Notified")
//                binding.imagesGridView.adapter = adapter
//            }
//        })
    }

    private fun setupChipListeners() {

    }

    private fun onSave(url: String, quote: String) {
        // Implement download functionality
        Log.e("ToSave---", url + "-----" + quote)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if(isHomeVisible) {
            gestureDetector.onTouchEvent(event)
        }
        return super.dispatchTouchEvent(event)
    }
}
