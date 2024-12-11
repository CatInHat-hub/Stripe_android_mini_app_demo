package com.example.myapplication.manager

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ComponentActivity
import com.example.myapplication.MyLog
import com.example.myapplication.stripeapi.Customer
import com.example.myapplication.stripeapi.StripeConstants
import com.stripe.android.PaymentConfiguration
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheet.CustomerConfiguration
import com.stripe.android.paymentsheet.PaymentSheetResult

class StripePaymentManager(private val context: Context,private val activity: AppCompatActivity) {
    private lateinit var paymentSheet: PaymentSheet
    private var customerConfig: CustomerConfiguration?=null
    private var customer=Customer()

    fun init(){
        PaymentConfiguration.init(context, StripeConstants.PUBLISHABLE_KEY)
        paymentSheet= PaymentSheet(activity,::onPaymentSheetResult)
        MyLog.debug(">>Initialize StripePaymentManager successfully")
    }


    fun initPaymentIntent(){

    }

    fun presentWithPaymentIntent(paymentIntentClientSecret:String) {
        MyLog.funcStart()
        if (!TextUtils.isEmpty(paymentIntentClientSecret)) {
            paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                PaymentSheet.Configuration(customer.id.toString(), customerConfig)
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
                PaymentSheet.Configuration(customer.id.toString())
            )
        } else {
            MyLog.error("onLoading...")
        }
    }

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult){
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
    }

    //getter  setter customerconfig and customer
    fun getCustomerConfig(): CustomerConfiguration? {
        return customerConfig
    }

    fun setCustomerConfig(config:CustomerConfiguration){
        this.customerConfig=config
    }

    fun getCustomer():Customer{
        return customer
    }

    fun setCustomer(customer: Customer){
        this.customer=customer
    }
}



/*fun presentPaymentFlow(
    stripeCustomer: Customer, paymentIntentClientSecret: String,
    setupIntentClientSecret: String
) {
    if (stripeCustomer.invoice_settings?.default_payment_method == null) {
        // Present with Setup Intent
        setupIntentClientSecret.let {
            paymentSheet.presentWithSetupIntent(
                it,
                PaymentSheet.Configuration("Your Merchant Name")
            )
        } ?: run {
            // Handle case where setupIntentClientSecret is null
            MyLog.error("PaymentManager>>>setupIntentClientSecret is null")
        }
    } else {
        // Present with Payment
        paymentIntentClientSecret.let {
            paymentSheet.presentWithPaymentIntent(
                it,
                PaymentSheet.Configuration("Your Merchant Name")
            )
        } ?: run {
            // Handle case where paymentIntentClientSecret is null
            MyLog.error("PaymentManager>>paymentIntentClientSecret is null")
        }
    }
}*/
