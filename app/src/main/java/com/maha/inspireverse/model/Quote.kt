package com.maha.inspireverse.model

data class Quote(
    val id: String,
    val quote:String,
    val author:String,
    var length:Int,
    var tags:List<String> = emptyList(),
    var isSaved:Boolean =false,
    val photoUrl:String,
    val fontTypeFace:String
)

//data class QuoteResponse(
//    val count:Int,
//    val totalCunt:Int,
//    val page:Int,
//    val totalPage:Int,
//    val result:List<Quote>
//)
