package com.rxkotlindaggerdemo.remote.model

import com.google.gson.annotations.SerializedName

/**
 * GithubUserProfile data model
 */
data class GithubUserProfile(@SerializedName("login") var userName: String = "",
                             @SerializedName("id") var userId: Int = 0,
                             @SerializedName("avatar_url") var avatarUrl: String = "",
                             @SerializedName("html_url") var profileUrl: String = "",
                             @SerializedName("name") var name: String = "Not found",
                             @SerializedName("company") var company: String = "",
                             @SerializedName("location") var location: String = "",
                             @SerializedName("bio") var bio: String = "",
                             @SerializedName("followers") var follower: String = "",
                             @SerializedName("following") var following: String = ""
)