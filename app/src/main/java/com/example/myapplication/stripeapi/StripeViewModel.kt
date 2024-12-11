package com.example.myapplication.stripeapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.MyLog
import com.stripe.android.Stripe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class StripeViewModel: ViewModel() {

    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    var stripeLiveData = MutableLiveData<PaymentSheetData>()
    var setupIntentLiveData = MutableLiveData<PaymentSheetData>()

    private val _customerLiveData=MutableLiveData<Customer>()
    val customerLiveData:LiveData<Customer> = _customerLiveData

    private val _ephemeralKeyLiveData=MutableLiveData<CustomerEphemeralData>()
    val ephemeralKeyLiveData:LiveData<CustomerEphemeralData> = _ephemeralKeyLiveData

    private val _productLiveData=MutableLiveData<ProductResponse>()
    val productLiveData:LiveData<ProductResponse> = _productLiveData

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
        amount: Int, currency: String?
    ) {
        scope.launch {
            StripePaymentService.createPaymentIntent(
                amount = amount,
                currency = "jpy",//Default Glamo subscription payment currency
                onError = {},
                onSuccess = { stripeLiveData.postValue(it) })
        }
    }

    fun fetchApiSetupIntent(){
        scope.launch {
            StripePaymentService.createSetupIntent(
                onError = {},
                onSuccess = { setupIntentLiveData.postValue(it) })
        }
    }

    fun fetchProductData(){
        scope.launch {
            StripePaymentService.listAllProducts(
                onError ={},
                onSuccess = {
                    _productLiveData.postValue(it)
                }
            )
        }
    }
}

data class PaymentSheetData(
    var customerId: String? = null,
    var customer: String? = null,
    var ephemeralKey: String? = null,
    var publishableKey: String? = null,
    var paymentIntentClientSecret: String? = null,
    var setupIntentClientSecret:String?=null
)


data class CustomerEphemeralData(
    var customerId:String?=null,
    var ephemeralKey: String?=null
)
/*
* Stripe Customer
* */
data class Customer(
    val id: String?=null,
    val objectName: String? = "customer", // Set default value
    val address: String? = null,
    val balance: Int?=null,
    val created: Long?=null,
    val currency: String?=null,
    val default_source: String? = null,
    val delinquent: Boolean?=null,
    val description: String?=null,
    val discount: Any? = null, // Can hold different types (null allowed)
    val email: String?=null,
    val invoice_prefix: String?=null,
    val invoice_settings: InvoiceSettings?=null,
    val livemode: Boolean?=null,
    val metadata: Map<String, Any> = emptyMap(), // Empty map by default
    val name: String?=null,
    val nextInvoiceSequence: Int?=null,
    val phone: String? = null,
    val preferred_locales: List<String> = emptyList(), // Empty list by default
    val shipping: Any? = null, // Can hold different types (null allowed)
    val tax_exempt: String?=null,
    val test_clock: Any? = null // Can hold different types (null allowed)
)

data class InvoiceSettings(
    val custom_fields: Any? = null, // Can hold different types (null allowed)
    val default_payment_method: String?=null,
    val footer: String? = null,
    val rendering_options: Any? = null // Can hold different types (null allowed)
)

data class ProductResponse(
    val data: List<Product> = emptyList(),
    val hasMore: Boolean?=null,
)

data class Product(
    val id:String?=null,
    val name: String?=null,
    val default_price:String?=null
)

data class PriceResponse(
    val data: List<Price> = emptyList()
)

data class Price(
    val id:String?=null,
    val currency:String?=null,
    val lookup_key:String?=null,
    val type: String?=null,
    val unit_amount: Int?=null,
    val unit_amount_decimal: String?=null,
    val product: Product?=null,
    val recurring: Recurring?=null,
)

data class Recurring(
    val aggregate_usage: Any?= null,
    val interval: String?=null,
    val interval_count: Int?=null,
    val meter: Any?=null,
    val trial_period_days: Any?=null,
    val usage_type: String?=null
)

/**/