package com.rxkotlindaggerdemo.remote

import com.google.gson.JsonElement
import com.rxkotlindaggerdemo.remote.model.GithubUserProfile
import io.reactivex.Observable

import retrofit2.http.GET
import retrofit2.http.Path



/**
 *  AppEnd Point
 */
interface AppEndPoint {
    @GET("/users/{username}")
    fun getGithubUserDetails(@Path("username") username: String): Observable<GithubUserProfile>
}