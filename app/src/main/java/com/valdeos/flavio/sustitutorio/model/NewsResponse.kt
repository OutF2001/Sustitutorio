package com.valdeos.flavio.sustitutorio.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles") val articles: List<News>
)