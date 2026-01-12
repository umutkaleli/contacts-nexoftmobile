package com.example.contacts.data.remote

import com.example.contacts.data.dto.BaseResponse
import com.example.contacts.data.dto.CreateUserRequest
import com.example.contacts.data.dto.UpdateUserRequest
import com.example.contacts.data.dto.UploadImageResponse
import com.example.contacts.data.dto.UserListResponse
import com.example.contacts.data.dto.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {
    // Get All Users from backend
    @GET("/api/User/GetAll")
    suspend fun getUsers(): Response<BaseResponse<UserListResponse>>

    // Get a User by id from backend
    @GET("/api/User/{id}")
    suspend fun getUserById(
        @Path("id") id: String
    ): Response<BaseResponse<UserResponse>>

   // Create a new user
    @POST("/api/User")
    suspend fun addUser(
        @Body request: CreateUserRequest
    ): Response<BaseResponse<UserResponse>>

    // Update a user info
    @PUT("/api/User/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body request: UpdateUserRequest
    ): Response<BaseResponse<UserResponse>>

    // Delete a user
    @DELETE("/api/User/{id}")
    suspend fun deleteUser(
        @Path("id") id: String
    ): Response<BaseResponse<Any>>

    // Upload a image
    @Multipart
    @POST("/api/User/UploadImage")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<UploadImageResponse>>
}