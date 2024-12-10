package jp.co.glamo.g4.general.network.inet

interface HttpClientCallback<T> {
    fun onFailure(url: String, response: T) {}
    fun onSuccess(url: String, response: T) {}

    fun onFailure(htmlResponseData: HttpClient.HtmlResponseData) {}
    fun onSuccess(htmlResponseData: HttpClient.HtmlResponseData) {}
}