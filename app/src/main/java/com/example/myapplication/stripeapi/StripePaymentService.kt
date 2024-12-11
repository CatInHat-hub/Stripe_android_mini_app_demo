package com.example.myapplication.stripeapi

import com.example.myapplication.MyLog
import jp.co.glamo.g4.general.network.inet.HttpClient
import com.example.myapplication.stripeapi.StripeConstants.AUTHORIZATION
import com.example.myapplication.stripeapi.StripeConstants.BASE_URL
import com.example.myapplication.stripeapi.StripeConstants.CUSTOMER
import com.example.myapplication.stripeapi.StripeConstants.ENDPOINT_CREATE_CUSTOMER
import com.example.myapplication.stripeapi.StripeConstants.ENDPOINT_CREATE_EPHEMERAL_KEY
import com.example.myapplication.stripeapi.StripeConstants.ENDPOINT_CREATE_PAYMENT_INTENT
import com.example.myapplication.stripeapi.StripeConstants.ENDPOINT_CREATE_SETUP_INTENT
import com.example.myapplication.stripeapi.StripeConstants.PASSWORD
import com.example.myapplication.stripeapi.StripeConstants.PUBLISHABLE_KEY
import com.example.myapplication.stripeapi.StripeConstants.SECRET_KEY
import com.example.myapplication.stripeapi.StripeConstants.STRIPE_VERSION
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Credentials
import org.json.JSONObject

object StripePaymentService {


    /*
    * Gson().fromJson<>() using Gson
    * */

    /*fun fetchApi(onError: () -> Unit, onSuccess: (data: PaymentSheetData) -> Unit) {
        val data = PaymentSheetData()
        CoroutineScope(Dispatchers.IO).launch { createCustomer(data, onError, onSuccess) }
    }*/

    /*private fun createCustomer(
        data: PaymentSheetData,
        onError: () -> Unit,
        onSuccess: (data: PaymentSheetData) -> Unit) {
        MyLog.funcStart()
        val url = BASE_URL + ENDPOINT_CREATE_CUSTOMER
        CoroutineScope(Dispatchers.IO).launch {
            val headers = mutableMapOf<String, String>()
            val credential = Credentials.basic(SECRET_KEY, PASSWORD)
            headers[AUTHORIZATION] = credential
            val jsonResponse = HttpClient.post(url = url, headers = headers)
            val json = JSONObject(jsonResponse)
            data.customerId = json.getString("id")
            createEphemeralKeys(data, onError, onSuccess)
        }
    }*/

    /*private fun createEphemeralKeys(
        data: PaymentSheetData,
        onError: () -> Unit,
        onSuccess: (data: PaymentSheetData) -> Unit) {
        MyLog.funcStart()
        val url = BASE_URL + ENDPOINT_CREATE_EPHEMERAL_KEY
        CoroutineScope(Dispatchers.IO).launch {
            val headers = mutableMapOf<String, String>()
            val params = mutableMapOf<String, String>()
            val credential = Credentials.basic(SECRET_KEY, PASSWORD)
            headers[AUTHORIZATION] = credential
            headers[STRIPE_VERSION] = "2024-10-28.acacia"
            params[CUSTOMER] = data.customerId ?: ""
            val jsonResponse = HttpClient.post(url = url, headers = headers, params = params)
            val json = JSONObject(jsonResponse)

            data.ephemeralKey = json.getString("secret")
            createPaymentIntent(data, onError, onSuccess)
        }
    }*/

    fun createPaymentIntent(
        amount: Int,
        currency: String,
        onError: () -> Unit,
        onSuccess: (data: PaymentSheetData) -> Unit
    ) {
        MyLog.funcStart()
        val url = BASE_URL + ENDPOINT_CREATE_PAYMENT_INTENT
        val data = PaymentSheetData()
        CoroutineScope(Dispatchers.IO).launch {
            val headers = mutableMapOf<String, String>()
            val params = mutableMapOf<String, String>()
            val credential = Credentials.basic(SECRET_KEY, PASSWORD)
            headers[AUTHORIZATION] = credential
            params[CUSTOMER] = "cus_RHsmCWrKvhQPU2"
            //data.customerId ?: ""
            params["amount"] = amount.toString()
            params["currency"] = currency
            val jsonResponse = HttpClient.post(url = url, headers = headers, params = params)
            val json = JSONObject(jsonResponse)
            data.customer = json.getString("customer")
            data.paymentIntentClientSecret = json.getString("client_secret")
            data.publishableKey = PUBLISHABLE_KEY
            onSuccess(data)
        }
    }


    /*
*
* */

    fun retrieveCustomer(
        onError: () -> Unit,
        onSuccess: (data: Customer) -> Unit
    ) {
        MyLog.funcStart()
        val url = "$BASE_URL/v1/customers/cus_RHsmCWrKvhQPU2"
        var data: Customer
        CoroutineScope(Dispatchers.IO).launch {
            val headers = mutableMapOf<String, String>()
            val credential = Credentials.basic(SECRET_KEY, PASSWORD)
            headers[AUTHORIZATION] = credential
            val jsonResponse = HttpClient.get(url, headers)
            data = Gson().fromJson(jsonResponse, Customer::class.java)
            MyLog.temp(">>>Retrieve Customer successfully: ${data.id}")
            onSuccess(data)
        }
    }

    fun getEphemeralKey(
        onError: () -> Unit,
        onSuccess: (ephemeralKey: CustomerEphemeralData) -> Unit
    ) {
        MyLog.funcStart()
        val url = BASE_URL + ENDPOINT_CREATE_EPHEMERAL_KEY
        var data: CustomerEphemeralData = CustomerEphemeralData()
        CoroutineScope(Dispatchers.IO).launch {
            val headers = mutableMapOf<String, String>()
            val params = mutableMapOf<String, String>()
            val credential = Credentials.basic(SECRET_KEY, PASSWORD)
            headers[AUTHORIZATION] = credential
            headers[STRIPE_VERSION] = "2024-10-28.acacia"
            params[CUSTOMER] = "cus_RHsmCWrKvhQPU2"
            val jsonResponse = HttpClient.post(url = url, headers = headers, params = params)
            val json = JSONObject(jsonResponse)
            data.ephemeralKey = json.getString("secret")
            data.customerId = StripeConstants.CUSTOMER_JANE
            MyLog.temp(">>>Get Ephemeral key successfully: ${data.ephemeralKey} >>Get customerId successfully: ${data.customerId}")
            onSuccess(data)
        }
    }

    fun createSetupIntent(
        onError: () -> Unit,
        onSuccess: (data: PaymentSheetData) -> Unit
    ) {
        MyLog.funcStart()
        val data = PaymentSheetData()
        val url = BASE_URL + ENDPOINT_CREATE_SETUP_INTENT
        CoroutineScope(Dispatchers.IO).launch {
            val headers = mutableMapOf<String, String>()
            val params = mutableMapOf<String, String>()
            val credential = Credentials.basic(SECRET_KEY, PASSWORD)
            headers[AUTHORIZATION] = credential
            params[CUSTOMER] = StripeConstants.CUSTOMER_JANE
            val jsonResponse = HttpClient.post(url = url, headers = headers, params = params)
            val json = JSONObject(jsonResponse)
            //data.customer=json.getString("customer")
            data.setupIntentClientSecret = json.getString("client_secret")
            onSuccess(data)
        }
    }

    fun listAllProducts(
        onError: () -> Unit,
        onSuccess: (data:ProductResponse) -> Unit
    ) {
        MyLog.funcStart()
        var data: ProductResponse
        val url = "$BASE_URL/v1/products"
        CoroutineScope(Dispatchers.IO).launch {
            val headers = mutableMapOf<String, String>()
            val credential = Credentials.basic(SECRET_KEY, PASSWORD)
            headers[AUTHORIZATION] = credential
            val jsonResponse = HttpClient.get(url, headers)
            data = Gson().fromJson(jsonResponse, ProductResponse::class.java)
            onSuccess(data)
        }
    }

    fun listAllPrices(
        onError: () -> Unit,
        onSuccess: () -> Unit
    ){
        MyLog.funcStart()
        var data:ProductResponse
        val url="$BASE_URL/v1/prices"
        CoroutineScope(Dispatchers.IO).launch {
            val headers= mutableMapOf<String,String>()
            val credential=Credentials.basic(SECRET_KEY, PASSWORD)
            headers[AUTHORIZATION]=credential
            val jsonResponse=HttpClient.get(url,headers)
            data=Gson().fromJson(jsonResponse,ProductResponse::class.java)
            onSuccess()
        }
    }
}