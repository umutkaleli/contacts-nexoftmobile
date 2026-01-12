package com.example.contacts.data.dto

import com.google.gson.annotations.SerializedName

enum class HttpStatusCode(val code: Int) {
    // Most used http status codes
    @SerializedName("200") OK(200),
    @SerializedName("201") CREATED(201),
    @SerializedName("202") ACCEPTED(202),
    @SerializedName("204") NO_CONTENT(204),
    @SerializedName("400") BAD_REQUEST(400),
    @SerializedName("401") UNAUTHORIZED(401),
    @SerializedName("403") FORBIDDEN(403),
    @SerializedName("404") NOT_FOUND(404),
    @SerializedName("500") INTERNAL_SERVER_ERROR(500);
}