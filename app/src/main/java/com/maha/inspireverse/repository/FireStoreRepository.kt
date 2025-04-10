package com.maha.inspireverse.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.maha.inspireverse.model.Photos
import com.maha.inspireverse.model.Quote
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface FireStoreRepository {
    suspend fun storeQuotes(
        userId: String?,
        quoteImageList: List<Pair<Quote, Photos>>
    ): Result<Unit>

    suspend fun fetchLikedQuotes(userId: String): Result<List<Quote>>
    suspend fun updateLikeStatus(userId: String, quote: Quote, isLiked: Boolean): Result<Unit>
    suspend fun toggleLikeStatus(userId: String, quoteId: String, isLiked: Boolean): Result<Unit>
}

class FirebaseFireStoreRepositroy : FireStoreRepository {
    private val fireStore = FirebaseFirestore.getInstance()

    override suspend fun storeQuotes(
        userId: String?,
        quoteImageList: List<Pair<Quote, Photos>>
    ): Result<Unit> {

        return suspendCancellableCoroutine { cont ->
            val userQuotesRef =
                fireStore.collection("users").document(userId.toString()).collection("quotes")
            val batch = fireStore.batch()
            quoteImageList.forEach { (quote, photo) ->
                val quoteData = hashMapOf(
                    "quoteId" to quote.id,
                    "quoteText" to quote.quote,
                    "author" to quote.author,
                    "length" to quote.length,
                    "photoUrl" to photo.src.original,
                    "isLiked" to quote.isSaved,
                    "tags" to quote.tags
                )
                val docRef = userQuotesRef.document(quote.id.toString())
                batch.set(docRef, quoteData)
            }
            batch.commit().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cont.resume(Result.success(Unit))
                } else {
                    cont.resume(Result.failure(task.exception ?: Exception("Error storing data")))
                }
            }
        }
    }

    override suspend fun fetchLikedQuotes(userId: String): Result<List<Quote>> {
        return suspendCancellableCoroutine { cont ->
            val quotesRef = fireStore.collection("users").document(userId).collection("quotes")
            quotesRef.whereEqualTo("isLiked", true).get()
                .addOnSuccessListener { querySnapshot ->
                    val likedQuotes = querySnapshot.documents.mapNotNull { document ->
                        // Parse document data into a Quote object
                        Quote(
                            id = document.getString("quoteId")?.toString() ?: "",
                            quote = document.getString("quoteText") ?: "",
                            author = document.getString("author") ?: "",
                            length = document.getLong("length")?.toInt() ?: 0,
                            tags = document.get("tags") as? List<String> ?: emptyList(),
                            isSaved = document.getBoolean("isLiked") ?: false,
                            photoUrl = document.getString("photoUrl") ?: "",
                            fontTypeFace = "default"
                        )
                    }
                    cont.resume(Result.success(likedQuotes))
                }
                .addOnFailureListener { exception ->
                    cont.resume(Result.failure(exception))
                }
        }

    }

    override suspend fun updateLikeStatus(
        userId: String,
        quote: Quote,
        isLiked: Boolean
    ): Result<Unit> {
        return suspendCancellableCoroutine { cont ->

            val docRef = fireStore.collection("users").document(userId).collection("quotes")
                .document(quote.id.toString())
            // Use set() with merge options to create or update the document
            val data = mapOf(
                "quoteId" to quote.id,
                "quoteText" to quote.quote,
                "length" to quote.length,
                "author" to quote.author,
                "photoUrl" to quote.photoUrl,
                "tags" to quote.tags,
                "isLiked" to isLiked
            )
            docRef.set(data, SetOptions.merge()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cont.resume(Result.success(Unit))
                    Log.d("Firestore", "Like status updated successfully.")
                } else {
                    cont.resume(
                        Result.failure(
                            task.exception ?: Exception("Failed to update like status")
                        )
                    )
                    Log.e("Firestore", "Failed to update like status.", task.exception)
                }

            }
        }
    }

    override suspend fun toggleLikeStatus(
        userId: String,
        quoteId: String,
        isLiked: Boolean
    ): Result<Unit> {
        return suspendCancellableCoroutine { cont ->
            val docRef = fireStore
                .collection("users")
                .document(userId)  // Reference the user's document
                .collection("quotes")
                .document(quoteId)
            Log.d("Firestore", userId)
            Log.d("quoteid", quoteId)

            docRef.update("isLiked", isLiked).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cont.resume(Result.success(Unit))
                    Log.d("Firestore", "Like status updated successfully.")
                } else {
                    cont.resume(
                        Result.failure(
                            task.exception ?: Exception("Failed to update like status")
                        )
                    )
                    Log.e("Firestore", "Failed to update like status.", task.exception)
                }
            }
        }
    }
}


