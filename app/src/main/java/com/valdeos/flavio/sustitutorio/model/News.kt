package com.valdeos.flavio.sustitutorio.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news_table")
data class News(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("urlToImage") val imageUrl: String?,
    @SerializedName("url") val url: String
)