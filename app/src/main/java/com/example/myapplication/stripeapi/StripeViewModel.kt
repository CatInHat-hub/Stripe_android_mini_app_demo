package com.example.myapplication.stripeapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.MyLog
import com.example.myapplication.data.Customer
import com.example.myapplication.data.CustomerEphemeralData
import com.example.myapplication.data.PaymentSheetData
import com.example.myapplication.data.Price
import com.example.myapplication.data.ProductResponse
import com.stripe.android.model.PaymentIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class StripeViewModel: ViewModel() {

    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _stripeLiveData=MutableLiveData<PaymentSheetData>()
    val stripeLiveData:LiveData<PaymentSheetData> = _stripeLiveData

    private val _setupIntentLiveData=MutableLiveData<PaymentSheetData>()
    val setupIntentLiveData:LiveData<PaymentSheetData> = _setupIntentLiveData

    private val _paymentIntentLiveData=MutableLiveData<PaymentIntent>()
    val paymentIntentLiveData:LiveData<PaymentIntent> = _paymentIntentLiveData

    private val _customerLiveData=MutableLiveData<Customer>()
    val customerLiveData:LiveData<Customer> = _customerLiveData

    private val _ephemeralKeyLiveData=MutableLiveData<CustomerEphemeralData>()
    val ephemeralKeyLiveData:LiveData<CustomerEphemeralData> = _ephemeralKeyLiveData

    private val _productLiveData=MutableLiveData<ProductResponse>()
    val productLiveData:LiveData<ProductResponse> = _productLiveData

    //private val _paymentResult

    /*fun fetchApi() {
        scope.launch {
            StripePaymentService.fetchApi(onError = {}, onSuccess = { stripeLiveData.postValue(it) })
        }
    }*/

    fun fetchCustomerData(){
        scope.launch {
            StripePaymentService.retrieveCustomer(onError = {},
                onSuccess = { data: Customer ->
                _customerLiveData.postValue(data)
                    MyLog.info("Success fetch Customer Object")
            })
        }
    }

    fun fetchEphemeralKeyData() {
        scope.launch {
            StripePaymentService.getEphemeralKey(
                onError = {},
                onSuccess = { ephemeralKey -> _ephemeralKeyLiveData.postValue(ephemeralKey) })
        }
    }

    fun fetchApiPaymentIntent(
        amount: Int, currency: String?, price: String
    ) {

        /*scope.launch {
            StripePaymentService.createPaymentIntent(
                amount = amount,
                currency = "jpy",//Default Glamo subscription payment currency
                onError = {},
                //onSuccess = { stripeLiveData.postValue(it) })
                onSuccess = {
                    //paymentIntentLiveData.postValue(it)
                    _paymentIntentLiveData.postValue(it)
                })
        }*/
        scope.launch {
            StripePaymentService.createSubscription(
                priceId = " ",
                onError = {},
                onSuccess = {
                    data->_paymentIntentLiveData.postValue(data)
                }
            )
        }
    }

    fun fetchApiSetupIntent(){
        scope.launch {
            StripePaymentService.createSetupIntent(
                onError = {},
                onSuccess = {
                    _setupIntentLiveData.postValue(it)
                    //setupIntentLiveData.postValue(it)
                })
        }
    }

    fun fetchProductData(){
        scope.launch {
            StripePaymentService.listAllProducts(
                onError = {},
                onSuccess = {
                    _productLiveData.postValue(it)
                }
            )
        }
    }
}