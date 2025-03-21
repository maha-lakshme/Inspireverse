package com.maha.inspireverse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maha.inspireverse.repository.AuthRepository
import com.maha.inspireverse.repository.FireStoreRepository
import com.maha.inspireverse.repository.QuoteRepository

class QuotesViewModelFactory(private val authRepository:AuthRepository,
                             private val firestoreRepository: FireStoreRepository,
                             private val quoteRepository: QuoteRepository
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuotesViewModel::class.java)) {
            return QuotesViewModel(authRepository,firestoreRepository,quoteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}