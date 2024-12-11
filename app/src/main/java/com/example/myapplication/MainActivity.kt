package com.example.myapplication

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.manager.StripePaymentManager
import com.google.android.material.snackbar.Snackbar
import com.stripe.android.paymentsheet.PaymentSheet
import com.example.myapplication.stripeapi.StripeViewModel
import com.stripe.android.paymentsheet.PaymentSheetResult

class MainActivity : AppCompatActivity() {
    lateinit var stripePaymentManager:StripePaymentManager

    private lateinit var stripeViewModel: StripeViewModel

    private lateinit var paymentIntentClientSecret: String

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var paymentSheet: PaymentSheet

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        MyLog.funcStart()
        super.onCreate(null)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        //Initialize stripeviewmodel,PaymentConfiguration and paymentSheet at very first\
        paymentSheet=PaymentSheet(this,::onPaymentSheetResult)
        stripePaymentManager=StripePaymentManager(this,this,paymentSheet)
        stripePaymentManager.init()

        stripeViewModel=ViewModelProvider(this).get(StripeViewModel::class.java)


        //stripeViewModel.fetchProductData()
        stripeViewModel.fetchCustomerData()
        stripeViewModel.fetchEphemeralKeyData()
        //Done observer setup card intent
        /*stripeViewModel.setupIntentLiveData.observe(this){setupIntentPaymentSheetData->
            MyLog.info(">Retrieved SetupIntenet client secret key ")
            setupIntentPaymentSheetData.let {
                if (it != null) {
                    setUpIntentClientSecret= it.setupIntentClientSecret.toString()
                }
            }
            *//*Logic to check client secret of SetupIntent, if has i will assign to setupclientsecret, if no
            * what should i do*//*
        }*/
        //Done observe payment intent
        stripeViewModel.stripeLiveData.observe(this){paymentIntentPaymentSheetData->
            MyLog.info(">Retrieved PaymentIntent client secret key ")
            paymentIntentPaymentSheetData.let {
                if (it!=null){
                    paymentIntentClientSecret=it.paymentIntentClientSecret.toString()
                }
            }
        }
        //Done retrieve customer data
        stripeViewModel.customerLiveData.observe(this){customer->
            if (customer!=null){
                stripePaymentManager.setCustomer(customer)
                MyLog.info(">>observe customer object")
            }
        }
        //Done create customer config
        stripeViewModel.ephemeralKeyLiveData.observe(this) { data ->
            val (customerId, ephemeralKey) = data
            if (customerId != null && ephemeralKey != null) {
                //customerConfig=PaymentSheet.CustomerConfiguration(customerId, ephemeralKey)
                stripePaymentManager.setCustomerConfig(PaymentSheet.CustomerConfiguration(customerId,ephemeralKey))
                //MyLog.info(">Done create customerConfig with $customerId and $ephemeralKey")
            }
        }


    }

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult){
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                // Payment succeeded
                MyLog.debug("PaymentManager>>Payment completed successfully")
                MyLog.info(stripeViewModel.paymentIntentLiveData.value?.clientSecret)
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

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        MyLog.funcStart()
        return super.onCreateView(name, context, attrs)
    }

    override fun onResume() {
        MyLog.funcStart()
        super.onResume()

    }

    override fun onPause() {
        MyLog.funcStart()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MyLog.funcStart()
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}