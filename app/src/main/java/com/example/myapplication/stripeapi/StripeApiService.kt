package com.example.myapplication.stripeapi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.POST

interface StripeApiService {
    @POST("/payment-sheet")
    suspend fun createPaymentSheet(
        //@Body
        //paymentSheetRequest: PaymentSheetRequest
    ):Response<PaymentSheetResponse>
}

data class PaymentSheetResponse(
    @SerializedName("paymentIntent")
    @Expose
    val paymentIntent: String,
    @SerializedName("ephemeralKey")
    @Expose
    val ephemeralKey: String,
    @SerializedName("customer")
    @Expose
    val customer: String,
    @SerializedName("publishableKey")
    @Expose
    val publishableKey: String
)