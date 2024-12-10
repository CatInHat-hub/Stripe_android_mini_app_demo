package jp.co.glamo.g4.general.network.inet

class HttpClientResponse(
    /**
     * @link(java.net.HttpURLConnection)
     * */
    val statusCode: Int,
    /**
     * Returns true if the code is in [200..300), which means the request was successfully received,
     * understood, and accepted.
     */
    val isSuccessFul: Boolean,
    val data: String? = null,
    val dataBytes: ByteArray? = null
)