package com.maha.inspireverse.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.maha.inspireverse.data.RetrofitInstance
import com.maha.inspireverse.model.Photos
import com.maha.inspireverse.model.Quote
import com.maha.inspireverse.model.ThemeModel
import com.maha.inspireverse.model.VideoFile
import com.maha.inspireverse.repository.AuthRepository
import com.maha.inspireverse.repository.FireStoreRepository
import com.maha.inspireverse.repository.QuoteRepository
import kotlinx.coroutines.launch

class QuotesViewModel(private val authRepository: AuthRepository,
                      private val firestoreRepository:FireStoreRepository,
                      private val quotesRepository: QuoteRepository): ViewModel() {

    private val _imagesWithQuotes = MutableLiveData<List<Pair<Quote, Photos>>>()
    val imagesWithQuotes: LiveData<List<Pair<Quote, Photos>>> get() = _imagesWithQuotes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _likedQuotes = MutableLiveData<ArrayList<Quote>>()
    val likedQuotes: LiveData<ArrayList<Quote>> get() = _likedQuotes

    private val _themeList = MutableLiveData<List<ThemeModel>>()
    val themeList: LiveData<List<ThemeModel>> get() = _themeList

    private val _customThemeQuotes = MutableLiveData<List<Pair<Quote, Photos>>>()
    val customThemeQuotes: LiveData<List<Pair<Quote, Photos>>> get() = _customThemeQuotes

    private val _bottomNavVisibility = MutableLiveData<Boolean>().apply {
        value = true // Set default value
    }
    val bottomNavVisibility: LiveData<Boolean> = _bottomNavVisibility

    private val _videosUrl =MutableLiveData<List<VideoFile>?>()
    val videosUrl : MutableLiveData<List<VideoFile>?> get() = _videosUrl

    private var authenticatedUser: FirebaseUser? = null

    init {
        signInAnonymously() // Authenticate the user once when the ViewModel is created
    }
    fun setBottomNavVisibility(isVisible: Boolean) {
        println("upating visibility")
        _bottomNavVisibility.value = isVisible
    }

    fun signInAnonymously() {
        viewModelScope.launch {
            val result = authRepository.signInAnonymously()
            if (result.isSuccess) {
                authenticatedUser = result.getOrNull() // Store the authenticated user
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Authentication failed"
                _error.value = errorMessage
            }
        }

    }

    fun fetchNewContent(category: String, quoteLimit: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val quotes = quotesRepository.fetchQuotes(quoteLimit)
                val photos = quotesRepository.fetchImages(category)
                if (quotes.isNotEmpty() && photos.isNotEmpty()) {
                    val minSize = minOf(quotes.size, photos.size)
                    val quoteImageList =
                        (0 until minSize).filter { index -> quotes[index].length <= 150 }
                            .map { index -> quotes[index] to photos[index] }
                    _imagesWithQuotes.value = quoteImageList
                } else {
                    _error.value = "Error fetching content"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getImageFromApi(query: String) {
        _isLoading.value = true
        var fontList = listOf(
            "ambery_garden",
            "bigshoulders",
            "chuckwe",
            "haloberry",
            "charm",
            "courgette_regular",
            "delius",
            "montserrat_regular",
            "satisfy",
            "tangerine_regular",
            "jonova_regular",
            "labybu",
            "langer",

            )
        viewModelScope.launch {
            try {
                val photo = quotesRepository.fetchImages(query)
                if (photo.isNotEmpty()) {
                    //val photos = imageResponse.body()?.photos ?: emptyList()
                    val imagePhotoSize = minOf(photo.size, fontList.size)
                    val list = List(imagePhotoSize) { index ->
                        ThemeModel(
                            imageUrl = photo[index].src.original,
                            fontName = fontList[index]
                        )
                    }
                    _themeList.value = list
                } else {
                    _error.value = "Error fetching content"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }

    }

    fun fetchQuotesForCustomTheme(
        fontType: String,
        imageUrl: String,
        quoteList: List<Pair<Quote, Photos>>
    ) {
        val quotes = quoteList ?: emptyList()
        var customQuoteList = quotes.map { (quote, photo) ->
            val updatedQuote = quote.copy(
                photoUrl = imageUrl,
                fontTypeFace = fontType
            )
            // Update photo's src by copying it and only replacing the portrait.
            val updatedPhoto = photo.copy(
                src = photo.src.copy(
                    portrait = imageUrl
                )
            )
            Pair(updatedQuote, updatedPhoto)
        }
        _customThemeQuotes.value = customQuoteList

    }

    fun storeDataInFirestore(quoteImageList: List<Pair<Quote, Photos>>) {
        val userId = authenticatedUser?.uid
            if (userId != null) {
                viewModelScope.launch {
                    // Call FirestoreRepository to store quotes
                    val result = firestoreRepository.storeQuotes(userId, quoteImageList)
                    if (result.isFailure) {
                        _error.value = "Failed to store data: ${result.exceptionOrNull()?.message}"
                    }
                }
            } else {
                _error.value = "User ID is null. Cannot store data."

        }
    }

    fun fetchLikedQuotes() {
        val userId = authenticatedUser?.uid

        if (userId != null) {
            viewModelScope.launch {
                val result = firestoreRepository.fetchLikedQuotes(userId)
                if (result.isSuccess) {
                    val likedQuotes = result.getOrNull()
                    _likedQuotes.value = likedQuotes?.let { ArrayList(it) } ?: arrayListOf()
                } else {
                    val errorMessage =
                        result.exceptionOrNull()?.message ?: "Failed to fetch liked quotes"
                    _error.value = errorMessage
                }
            }
        } else {
            _error.value = "User is not authenticated."
        }
    }

    fun toggleLike(likedQouteImage: Pair<Quote, Photos>, isLiked: Boolean) {
        var (quote, photo) = likedQouteImage
        val userId = authenticatedUser?.uid
        Log.d("userId", userId.toString())
        Log.d("quote", quote.toString())
        Log.d("photo", photo.toString())

            if (userId != null) {
                viewModelScope.launch {
                    val result = firestoreRepository.updateLikeStatus(userId, quote, isLiked)
                    if (result.isSuccess) {
                        Log.d("Firestore", "Like status updated successfully.")
                    } else {
                        _error.value = "Failed to update status: ${result.exceptionOrNull()?.message}"
                    }
                }
            }else {
                _error.value = "User not authenticated."
            }

    }

    fun toggleLikeAndDislike(quoteId: String, isLiked: Boolean) {
        val userId = authenticatedUser?.uid
        if (userId != null) {
            viewModelScope.launch {
                val result = firestoreRepository.toggleLikeStatus(userId, quoteId, isLiked)
                if (result.isFailure) {
                    _error.value = "Failed to update like status: ${result.exceptionOrNull()?.message}"
                }
            }
        } else {
            _error.value = "User not authenticated."
        }


    }
    fun fetchVideos(){
        viewModelScope.launch {
            try {
                val videoResponse = RetrofitInstance.pexelVideoApi.getVideosFromApi("z54ItlsgEc2BsYo8g6VeKZssWeoTcEpk8KMoVEiA5SDMqzKOcWZIUGs6")
                if(videoResponse.isSuccessful){
                    val videoUrlList = videoResponse.body()?.videos ?: emptyList()
                   // _videosUrl.value =videoUrlList

                    val smallVideos = videoResponse.body()?.videos?.mapNotNull { video ->
                        video.video_files.find { it.quality == "sd"  && it.width!! > it.height!! }
                    }
                    _videosUrl.value =smallVideos
                    smallVideos?.forEach { videoFile ->
                        println("Small Video URL: ${videoFile.link}")
                    }
                }else{
                    Log.e("Viderourl Error---",videoResponse.message())

                }
            }catch (e:Exception){
            Log.e("Videro Error---",e.toString())
            }
        }
    }
}
