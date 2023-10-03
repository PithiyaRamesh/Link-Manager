package com.redvings.linkmanager.models

import com.google.gson.annotations.SerializedName

data class TabsModel(
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("links")
    var links: ArrayList<LinkModel>?
)
