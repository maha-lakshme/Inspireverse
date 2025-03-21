package com.maha.inspireverse.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.maha.inspireverse.R
import com.maha.inspireverse.adapter.SavedListAdapter
import com.maha.inspireverse.databinding.FragmentSavedFolderBinding
import com.maha.inspireverse.repository.AuthRepository
import com.maha.inspireverse.repository.FireBaseAuthRepository
import com.maha.inspireverse.repository.FirebaseFireStoreRepositroy
import com.maha.inspireverse.viewmodel.QuotesViewModel
import com.maha.inspireverse.viewmodel.QuotesViewModelFactory

class UserFolderFragment: Fragment() {
    private lateinit var binding:FragmentSavedFolderBinding
    private val viewModel: QuotesViewModel by lazy {
        ViewModelProvider(requireActivity()).get(QuotesViewModel::class.java)
    }
    private lateinit var savedListAdapter :SavedListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

       binding = FragmentSavedFolderBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner= viewLifecycleOwner

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(),1)
        savedListAdapter = SavedListAdapter(emptyList(),viewModel) // Provide an empty list initially
        recyclerView.adapter = savedListAdapter

        viewModel.fetchLikedQuotes()
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    viewModel.error.observe(viewLifecycleOwner, Observer {
        Log.e("ERROR----",it)
    })
        // Observe LiveData and update the adapter's list
        viewModel.likedQuotes.observe(viewLifecycleOwner, Observer { likedList ->
            Log.d("LikedList----",likedList.toString())
            savedListAdapter = SavedListAdapter(likedList,viewModel)
            recyclerView.adapter =savedListAdapter
            savedListAdapter.updateList(likedList)

        })

        return binding.root
    }
}