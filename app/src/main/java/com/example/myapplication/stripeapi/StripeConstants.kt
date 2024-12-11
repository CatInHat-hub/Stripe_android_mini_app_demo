package com.example.myapplication.stripeapi

object StripeConstants {

    const val BASE_URL = "https://api.stripe.com"
    const val PUBLISHABLE_KEY = "pk_test_51QIPPs1mw51K9M5VXlF18y1BK2145jJIY6ms4hLdmwleMDVYYjASFwreBnHWqJA9oxvMN7krP52joiQxPca7iBaE00YNAb6Uqp"
    /*const val SECRET_KEY = "sk_test_51QKBXnLAchkPnoGHptlxu6YA5vRToeWmSN4BDhx15fxZrRZThPEygjipzBCXS60GZYBIxMYj9us8ME3ofmB5lwJ000nBQmDYUZ"*/
    const val SECRET_KEY = "sk_test_51QIPPs1mw51K9M5VQtzEUcuSNE8WJoch7wogSHUfMtgqIhLnlUwBwGgjoyY8ds9IyvIvWHVd7CKLgrMMHp8B37zo00bnIcfWLN"
    const val PASSWORD = ""
    const val AUTHORIZATION = "Authorization"
    const val STRIPE_VERSION = "stripe-version"
    const val CUSTOMER = "customer"
    const val SECRET = "ek_test_YWNjdF8xMDMyRDgyZVp2S1lsbzJDLDZiTjJHME1hMGZrcVhkbGlFdk1wVFczNWFDVHdpT0M_00O4MPczFO"

    //method POST
    const val ENDPOINT_CREATE_CUSTOMER = "/v1/customers"

    //method POST
    const val ENDPOINT_CREATE_EPHEMERAL_KEY = "/v1/ephemeral_keys"

    //method POST
    const val ENDPOINT_CREATE_PAYMENT_INTENT = "/v1/payment_intents"

    const val ENDPOINT_CREATE_SETUP_INTENT = "/v1/setup_intents"

    const val CUSTOMER_JANE="cus_RHsmCWrKvhQPU2"
}