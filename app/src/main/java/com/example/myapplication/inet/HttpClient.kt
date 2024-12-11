package jp.co.glamo.g4.general.network.inet

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.myapplication.MyLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/*
同期的にGET
val response = HttpClient.get("https://hoge/api/fuga")
Log.i(TAG, "同期的にGET：${response}")

非同期でGET
HttpClient.getAsync("https://hoge/api/fuga"){
    Log.i(TAG, "非同期でGET：${it}")
}

同期的にPOST
val params = mapOf(
    "name" to "ryota",
    "job" to "neet"
)
val response = HttpClient.post("https://hoge/api/fuga", params)
Log.i(TAG, "同期的にPOST：${response}")

非同期でPOST
val params = mapOf(
    "name" to "ryota",
    "job" to "neet"
)
HttpClient.postAsync("https://hoge/api/fuga", params) {
    Log.i(TAG, "非同期でPOST：${it}")
}

非同期でPOST + エラーハンドリング
val handleError: (Exception) -> Unit = {
    e -> Log.e(TAG, "エラー：${e}")
}
HttpClient.postAsync("https://hoge/api/fuga", params, onFailed = handleError) {
    Log.i(TAG, "非同期でPOST + エラーハンドリング：${it}")
}
 */

/**
 * https://qiita.com/Ryota101/items/13aac25b33dba3bbbbe6
 */
class HttpClient {
    companion object {
        val instance = OkHttpClient().newBuilder()
            .connectTimeout(1 * 60, TimeUnit.SECONDS)
            .writeTimeout(3 * 60, TimeUnit.SECONDS)
            .readTimeout(3 * 60, TimeUnit.SECONDS)
            .build()

        private fun getUnsafeOkHttpClient(): OkHttpClient.Builder =
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts: Array<TrustManager> = arrayOf(
                    @SuppressLint("CustomX509TrustManager")
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) = Unit

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) = Unit

                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
                )
                // Install the all-trusting trust manager
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { _, _ -> true }
                builder
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        fun getUnsafe(url: String): String {
            val request = Request.Builder()
                .url(url)
                .build()
            return try {
                val response = getUnsafeOkHttpClient().build().newCall(request).execute()
                response.body?.string().orEmpty()
            } catch (e: Exception) {
                MyLog.error(e)
                ""
            }
        }

        fun get(url: String): String {
            val request = Request.Builder()
                .url(url)
                .build()
            return try {
                val response = instance.newCall(request).execute()
                response.body?.string().orEmpty()
            } catch (e: Exception) {
                MyLog.error(e)
                ""
            }
        }

        fun get(url: String,headers: Map<String, String>): String {
            val buider = Request.Builder()
            headers.forEach { (name: String, value: String) ->
                run {
                    buider.header(name, value)
                    //MyLog.temp(name + " " + value)
                }
            }
            val request=buider.url(url).build()
            return try {
                val response = instance.newCall(request).execute()
                response.body?.string().orEmpty()
            } catch (e: Exception) {
                MyLog.error(e)
                ""
            }
        }

        fun getBytes(url: String): ByteArray? {
            val request = Request.Builder()
                .url(url)
                .build()
            return try {
                val response = instance.newCall(request).execute()
                response.body?.bytes()
            } catch (e: Exception) {
                MyLog.error(e)
                null
            }
        }

        fun getBytesHtml(url: String): HttpClientResponse {
            val request = Request.Builder()
                .url(url)
                .build()
            return try {
                val response = instance.newCall(request).execute()
                HttpClientResponse(
                    statusCode = response.code,
                    isSuccessFul = response.isSuccessful,
                    dataBytes = response.body?.bytes()
                )
            } catch (e: Exception) {
                MyLog.error(e)
                HttpClientResponse(statusCode = -1, isSuccessFul = false, data = e.message)
            }
        }

        fun getHtml(url: String): HttpClientResponse {
            val request = Request.Builder()
                .url(url)
                .build()
            return try {
                val response = instance.newCall(request).execute()
                HttpClientResponse(
                    statusCode = response.code,
                    response.isSuccessful,
                    data = response.body?.string().orEmpty()
                )
            } catch (e: Exception) {
                MyLog.error(e)
                HttpClientResponse(statusCode = -1, isSuccessFul = false, data = e.message)
            }
        }

        fun getHtmlAsync(
            id: String,
            url: String,
            typeRequest: HtmlTypeRequest,
            session: Long,
            callback: CallbackHtml
        ) {
            MyLog.temp("url $url")
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    coroutineScope {
                        withContext(Dispatchers.Default) {
                            getHtml(url)
                        }.let {
                            callback.onFinish(
                                HtmlResponseData(
                                    id = id,
                                    urlRequest = url,
                                    isSuccess = true,
                                    htmlTypeRequest = typeRequest,
                                    responseSession = session,
                                    httpClientResponse = it
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    callback.onFinish(
                        HtmlResponseData(
                            id = id,
                            urlRequest = url,
                            isSuccess = false,
                            htmlTypeRequest = typeRequest,
                            responseSession = session,
                            httpClientResponse = null
                        )
                    )
                }
            }
        }

        fun getVideoHtmlAsync(
            id: String,
            url: String,
            typeRequest: HtmlTypeRequest,
            session: Long,
            callback: CallbackHtml
        ) {
            MyLog.temp("url $url")
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    coroutineScope {
                        withContext(Dispatchers.Default) {
                            getBytesHtml(url)
                        }.let {
                            callback.onFinish(HtmlResponseData(
                                id = id,
                                urlRequest = url,
                                isSuccess = true,
                                htmlTypeRequest = typeRequest,
                                responseSession = session,
                                httpClientResponse = it
                            ))
                        }
                    }
                } catch (e: Exception) {
                    callback.onFinish(
                        HtmlResponseData(
                            id = id,
                            urlRequest = url,
                            isSuccess = false,
                            htmlTypeRequest = typeRequest,
                            responseSession = session,
                            httpClientResponse = null
                        ))
                }
            }
        }

        fun getAsync(
            lifecycleScope: LifecycleCoroutineScope,
            url: String,
            onFailed: (Exception) -> Unit = {},
            onSuccess: (String) -> Unit = {}
        ) {
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    coroutineScope {
                        onSuccess(withContext(Dispatchers.Default) { get(url) })
                    }
                } catch (e: Exception) {
                    onFailed(e)
                }
            }
        }

        fun getAsync(url: String, callback: Callback) {
            MyLog.temp("url $url")
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    coroutineScope {
                        withContext(Dispatchers.Default) {
                            get(url)
                        }.let {
                            callback.onFinish(true, it)
                        }
                    }
                } catch (e: Exception) {
                    callback.onFinish(false, e.message)
                }
            }
        }

        fun getUnsafeAsync(url: String, callback: Callback) {
            MyLog.temp("url $url")
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    coroutineScope {
                        withContext(Dispatchers.Default) {
                            getUnsafe(url)
                        }.let {
                            callback.onFinish(true, it)
                        }
                    }
                } catch (e: Exception) {
                    callback.onFinish(false, e.message)
                }
            }
        }

        fun post(
            url: String,
            headers: Map<String, String>? = null,
            params: Map<String, String>? = null
        ): String {
            val formBuilder = FormBody.Builder()
            params?.forEach { (name: String, value: String) ->
                run {
                    formBuilder.add(name, value)
                    //MyLog.temp(name + " " + value)
                }
            }
            val requestBody: RequestBody = formBuilder.build()
            val builder = Request.Builder()
            builder.url(url)
            builder.post(requestBody)
            headers?.forEach { (name: String, value: String) ->
                run {
                    builder.header(name, value)
                    //MyLog.temp(name + " " + value)
                }
            }
            val request = builder.build()
            val response = instance.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.string().orEmpty()
        }

        fun post(url: String, params: Map<String, String>? = null): String {
            val formBuilder = FormBody.Builder()
            params?.forEach { (name: String, value: String) -> formBuilder.add(name, value) }
            val requestBody: RequestBody = formBuilder.build()
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            val response = instance.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.string().orEmpty()
        }

        fun postAsync(url: String, params: Map<String, String>? = null, callback: Callback) {

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    coroutineScope {
                        withContext(Dispatchers.Default) { post(url, params) }.let {
                            callback.onFinish(true, it)
                        }
                    }
                } catch (e: Exception) {
                    callback.onFinish(false, e.message)
                }
            }
        }

        fun postAsync(
            lifecycleScope: LifecycleCoroutineScope,
            url: String,
            params: Map<String, String>,
            onFailed: (java.lang.Exception) -> Unit,
            onSuccess: (String) -> Unit
        ) {
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    coroutineScope {
                        onSuccess(withContext(Dispatchers.Default) { post(url, params) })
                    }
                } catch (e: Exception) {
                    onFailed(e)
                }
            }
        }

        fun postAsync(
            lifecycleScope: LifecycleCoroutineScope,
            url: String,
            headers: Map<String, String>,
            params: Map<String, String>,
            onFailed: (java.lang.Exception) -> Unit,
            onSuccess: (String) -> Unit
        ) {
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    coroutineScope {
                        onSuccess(withContext(Dispatchers.Default) { post(url, headers, params) })
                    }
                } catch (e: Exception) {
                    onFailed(e)
                }
            }
        }
    }

    interface Callback {
        fun onFinish(isSuccess: Boolean, response: String?) {}
    }

    enum class HtmlTypeRequest {
        NONE, NOTICE, SERVICE
    }

    data class HtmlResponseData(
        val id: String = "",
        val urlRequest: String = "",
        val isSuccess: Boolean = false,
        val htmlTypeRequest: HtmlTypeRequest = HtmlTypeRequest.NONE,
        val responseSession: Long = -1,
        val httpClientResponse: HttpClientResponse? = null
    )

    interface CallbackHtml {
        fun onFinish(htmlResponseData: HtmlResponseData)
    }
}