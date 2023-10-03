package com.redvings.linkmanager.models

import com.google.gson.annotations.SerializedName

data class LinkModel(
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("links")
    var link: String?
)
