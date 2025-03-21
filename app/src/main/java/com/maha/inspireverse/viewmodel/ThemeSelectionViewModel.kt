package com.maha.inspireverse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maha.inspireverse.R
import com.maha.inspireverse.data.RetrofitInstance
import com.maha.inspireverse.model.ThemeModel
import kotlinx.coroutines.launch

class ThemeSelectionViewModel:ViewModel() {

    private val _themeList = MutableLiveData<List<ThemeModel>>()
      val themeList :LiveData<List<ThemeModel>> get() = _themeList

    init {
        //_themeList.value = generateThemes()
        getImageFromApi("serene lakes celestial patterns")
    }

    fun getImageFromApi(query:String){
        var fontList = listOf(
            "charm",
            "courgette_regular",
            "delius",
            "montserrat_regular",
            "satisfy",
            "tangerine_regular"
        )
        viewModelScope.launch {
            try {
              val imageResponse= RetrofitInstance.pexelsApi.searchImages(query,"z54ItlsgEc2BsYo8g6VeKZssWeoTcEpk8KMoVEiA5SDMqzKOcWZIUGs6")
                if(imageResponse.isSuccessful){
                    val photos = imageResponse.body()?.photos ?: emptyList()
                    val imagePhotoSize = minOf(photos.size,fontList.size)
                    val list = List(imagePhotoSize,){index ->
                        ThemeModel(imageUrl = photos[index].src.original, fontName = fontList[index])
                    }
                    _themeList.value =list
                }
            }catch (e: Exception){

            }
        }

    }

//    fun generateThemes():List<ThemeModel>{
//        var themeImgList = listOf(
//"https://images.pexels.com/photos/31097787/pexels-photo-31097787.jpeg?auto=compress\u0026cs=tinysrgb\u0026h=130" ,
//           "https://www.pexels.com/photo/milky-way-and-body-of-water-wallpaper-414277/",
//           "https://www.pexels.com/photo/moon-among-the-clouds-in-the-sky-16134513/",
//            "https://www.pexels.com/photo/drone-photography-of-pink-and-blue-ocean-water-8414049/",
//           "https://www.pexels.com/photo/clouds-in-the-sky-during-sunset-19754924/",
//           "https://www.pexels.com/photo/artistic-water-splash-with-vivid-colors-31129525/"
//        )
//
//        // Pair by index (for simplicity).
//        val size = minOf(themeImgList.size, fontList.size)
//        return List(size) { index ->
//            ThemeModel(imageUrl = themeImgList[index], fontName = fontList[index])
//        }
//    }
}