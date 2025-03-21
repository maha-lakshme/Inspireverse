package com.maha.inspireverse.main.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.maha.inspireverse.R
import com.maha.inspireverse.adapter.FullScreenQuotesAdapter
import com.maha.inspireverse.adapter.QuoteAndPicsAdapter
import com.maha.inspireverse.databinding.FragmentHomeBinding
import com.maha.inspireverse.main.GenerateQuotes
import com.maha.inspireverse.main.GenerateQuotes.generatePopularQuery
import com.maha.inspireverse.main.MainActivity
import com.maha.inspireverse.main.QuoteDetailActivity
import com.maha.inspireverse.model.Photos
import com.maha.inspireverse.model.Quote
import com.maha.inspireverse.viewmodel.QuotesViewModel



class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: QuotesViewModel by lazy {
        ViewModelProvider(requireActivity()).get(QuotesViewModel::class.java)
    }
    private lateinit var gestureDetector: GestureDetectorCompat
   // private lateinit var adapter: QuoteAndPicsAdapter

    private lateinit var fullscreen_adapter: FullScreenQuotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding and inflate the layout
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.fetchNewContent("soothing nature",30)

        fullscreen_adapter = FullScreenQuotesAdapter(emptyList(),viewModel)

        // Set up ViewPager2
        binding.viewPagerQuotes.adapter = fullscreen_adapter
        binding.viewPagerQuotes.setPageTransformer(DepthPageTransformer())
//        binding.viewPagerQuotes.isUserInputEnabled = false
//
        binding.viewPagerQuotes.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
//                Log.d("CLICKED VIEWPAGER state---",state.toString())
//                Log.e("CLICKED VIEWPAGER state needed---",ViewPager2.SCROLL_STATE_DRAGGING.toString())
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    println("Dragging selected--------")
                    viewModel.setBottomNavVisibility(false) // Hide the BottomNavigationView
                }
            }

        })
//
//        // Set up the GestureDetector to detect single taps
//        gestureDetector =
//            GestureDetectorCompat(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
//                override fun onSingleTapUp(e: MotionEvent): Boolean {
//                    println("onSingleTapUp triggered")
//                    return true
//                }
//
//                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
//                    println("onSingleTapConfirmed triggered")
//                    // Instead of directly setting the visibility here, update the ViewModel.
//                    // This call will trigger the LiveData observer in the Activity.
//                    val currentVisibility = viewModel.bottomNavVisibility.value ?: true
//                    (activity as? MainActivity)?.viewModel?.setBottomNavVisibility(!currentVisibility)  // Show bottom navigation
//                    return true  // Signal that the event is handled
//                }
//                override fun onDown(e: MotionEvent): Boolean {
//                    println("onDown triggered")
//                    // Must return true to signal that you want to process further events
//                    return true
//                }
//
//            })





        // Set up RecyclerView
//        val recyclerView: RecyclerView = binding.recyclerView
//        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Observe loading state and show/hide progress bar
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        // Observe errors and show toast
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            Log.e("Error", error)
        })

        // Observe imagesWithQuotes and update RecyclerView adapter
        viewModel.imagesWithQuotes.observe(viewLifecycleOwner, Observer { items ->
//            adapter = QuoteAndPicsAdapter(items,
//                onSave = { quoteId, isSaved ->
//                viewModel.toggleLike(quoteId, isSaved)
//            },
//                onDownload = { quote ->
//                viewModel.downloadItems(quote)
//            },
//                onQuoteCardClick = {quote,photo,imageView ->
//                    val intent = Intent(requireContext(), QuoteDetailActivity::class.java).apply {
//                        putExtra("quote_text", quote.quote)
//                        putExtra("image_url", photo.src.original)
//                    }
//                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        requireActivity(),
//                        imageView,
//                        ViewCompat.getTransitionName(imageView) ?: getString(R.string.shared_image_transition_name)
//                    )
//                    startActivity(intent,options.toBundle())
//                }
//            )
//            recyclerView.adapter = adapter

            fullscreen_adapter = FullScreenQuotesAdapter(items,viewModel)
            binding.viewPagerQuotes.adapter = fullscreen_adapter
          fullscreen_adapter.notifyDataSetChanged()

            // Restore custom theme if available; else your regular flow continues.
            applyCustomThemeIfAvailable(items)
        })

        return binding.root
    }
    private fun applyCustomThemeIfAvailable(quotesImageList:List<Pair<Quote,Photos>>) {
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val customBackgroundUrl = prefs.getString("custom_background_url", null)
        val customFont = prefs.getString("custom_font", null)

        if (customBackgroundUrl != null && customFont != null) {
            // Update the adapter to use these custom values.
            Log.e("Custombackgound url---->", customBackgroundUrl)
            Log.e("Custombackgound font---->", customFont)
            viewModel.fetchQuotesForCustomTheme(customFont,customBackgroundUrl,quotesImageList)
            viewModel.customThemeQuotes.observe(viewLifecycleOwner, Observer {quote->
                Log.e("---CUSTOM THEME IS SET ---->",quote.toString())
                fullscreen_adapter.updateItems(quote)

            })
            //    fullscreenAdapter.setCustomTheme(customBackgroundUrl, customFont)
        }
    }

}

