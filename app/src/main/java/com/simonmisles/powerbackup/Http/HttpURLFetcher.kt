package com.simonmisles.powerbackup.Http

import com.simonmisles.powerbackup.BuildConfig
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL

class HttpURLFetcher(val mUserAgent: String = "Android : ${BuildConfig.APPLICATION_ID} ${BuildConfig.VERSION_NAME} Version ${BuildConfig.VERSION_CODE}") {
    val TAG = "HttpURLFetcher"
    fun doFetch(mRequest: HttpRequest): HttpResponse {

        val lTimeOut = if (mRequest.mTimeOut > 0) mRequest.mTimeOut else 30

        val lHttpURLConnection: HttpURLConnection = URL(mRequest.mURL).openConnection() as HttpURLConnection
        lHttpURLConnection.requestMethod = mRequest.mHttpRequestMethod.name
        lHttpURLConnection.connectTimeout = lTimeOut.times(1000)
        lHttpURLConnection.instanceFollowRedirects = false
        lHttpURLConnection.addRequestProperty("User-Agent", mUserAgent)

        if (mRequest.mHeader.isNotEmpty()) {
            mRequest.mHeader.keys.forEach {
                lHttpURLConnection.addRequestProperty(it, mRequest.mHeader[it])
            }
        }

        if (mRequest.mHttpRequestMethod != HttpRequestMethod.GET) {

            lHttpURLConnection.addRequestProperty("Content-Type", mRequest.mContentType)

            if (mRequest.mPayload.isNotEmpty()) {
                lHttpURLConnection.doInput = true
                val lOutputStream: OutputStream = lHttpURLConnection.outputStream
                lOutputStream.write(mRequest.mPayload)
                lOutputStream.flush()
            }
        }

        var lResponseStringBuffer: StringBuilder? = null
        var lInputStream: InputStream? = null
        val lResponseCode = lHttpURLConnection.responseCode

        try {
            lInputStream = lHttpURLConnection.inputStream
        } catch (ioe: IOException) {
            if (lResponseCode != HTTP_OK && lResponseCode != HTTP_CREATED) {
                lInputStream = lHttpURLConnection.errorStream
            }
        }

        if (lInputStream != null) {
            lResponseStringBuffer = StringBuilder()
            val mReader = BufferedReader(InputStreamReader(lInputStream))
            mReader.forEachLine { lResponseStringBuffer.append(it) }
            lInputStream.close()
        }

        val lHttpResponse =HttpResponse(lResponseCode, lResponseStringBuffer.toString(), lHttpURLConnection.headerFields)
        lHttpURLConnection.disconnect()

        return lHttpResponse

    }

}