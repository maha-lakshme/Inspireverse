package com.maha.inspireverse.model

data class PexelsImage(
    var total_results : String,
    var page:Int,
    var per_page:Int,
    var photos:List<Photos>,
    var next_page:String)

data class Photos(
    val id: Long,
    val width: Long,
    val height: Long,
    val url: String,
    val photographer: String,
    val photographer_url: String,
    val photographer_id: Long,
    val avg_color: String,
    val src: Src,
    val liked: Boolean,
    val alt: String,
)

data class Src(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String,
)