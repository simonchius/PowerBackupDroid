package com.simonmisles.powerbackup.Http

data class HttpRequest(
    val mURL : String = "",
    var mPayload: ByteArray = byteArrayOf(),
    val mHttpRequestMethod: HttpRequestMethod = HttpRequestMethod.GET,
    val mContentType: String = "application/json",
    val mHeader : Map<String, String> = mapOf(),
    val mTimeOut : Int = 30
)