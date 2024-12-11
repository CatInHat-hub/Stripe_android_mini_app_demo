package com.example.myapplication.manager

import android.content.Context
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.MyLog
import com.example.myapplication.data.Customer
import com.example.myapplication.stripeapi.StripeConstants
import com.example.myapplication.stripeapi.StripeViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.PaymentIntent
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheet.CustomerConfiguration
import com.stripe.android.paymentsheet.PaymentSheetResult

class StripePaymentManager(private val context: Context,
                           private val activity: AppCompatActivity,
    private val paymentSheet:PaymentSheet) {
    //private lateinit var paymentSheet: PaymentSheet
    private var customerConfig: CustomerConfiguration?=null
    private var customer= Customer()
    interface Callback{
        fun onSuccess()
        fun onFailed()
        fun onCanceled()
    }


    fun init(){
        PaymentConfiguration.init(context, StripeConstants.PUBLISHABLE_KEY)
    }

    fun presentWithPaymentIntent(paymentIntentClientSecret:String) {
        MyLog.funcStart()
        if (!TextUtils.isEmpty(paymentIntentClientSecret)) {
            paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                PaymentSheet.Configuration(customer.name.toString(), customerConfig)
            )
        } else {
            MyLog.error("onLoading...")
        }
    }
    fun presentWithSetupIntent(setUpIntentClientSecret:String) {
        MyLog.funcStart()
        if (!TextUtils.isEmpty(setUpIntentClientSecret)) {
            paymentSheet.presentWithSetupIntent(
                setUpIntentClientSecret,
                PaymentSheet.Configuration(customer.name.toString())
            )
        } else {
            MyLog.error("onLoading...")
        }
    }

    /*private fun onPaymentSheetResult(paymentResult: PaymentSheetResult){
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                // Payment succeeded
                MyLog.debug("PaymentManager>>Payment completed successfully")
                }
            is PaymentSheetResult.Canceled -> {
                // Payment was canceled
                MyLog.debug("PaymentManager>>Payment canceled")
            }
            is PaymentSheetResult.Failed -> {
                // Payment failed
                MyLog.debug("PaymentManager>>Payment failed: ${paymentResult.error}")
            }
        }
    }*/



    fun getCustomerConfig(): CustomerConfiguration? {
        return customerConfig
    }

    fun setCustomerConfig(config:CustomerConfiguration){
        this.customerConfig=config
    }

    fun getCustomer(): Customer {
        return customer
    }

    fun setCustomer(customer: Customer){
        this.customer=customer
    }
}