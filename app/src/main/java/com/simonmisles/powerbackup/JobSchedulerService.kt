package com.simonmisles.powerbackup

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.simonmisles.powerbackup.Helper.PreferenceHelper
import com.simonmisles.powerbackup.Helper.SMSHelper
import com.simonmisles.powerbackup.Http.HttpRequest
import com.simonmisles.powerbackup.Http.HttpRequestMethod
import com.simonmisles.powerbackup.Http.HttpURLFetcher
import com.simonmisles.powerbackup.Pojo.SMS
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection

class JobSchedulerService : JobService() {
    companion object {
        val TAG = "JobSchedulerService"
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job has Started")
        UpdateSMSToServer(applicationContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job has killed")

        return false
    }

    class UpdateSMSToServer(val mContext: Context) : AsyncTask<Void, Long, Boolean>() {
        lateinit var mConfiguredAPI: String
        lateinit var mSecurityKey: String
        lateinit var mPreferences: SharedPreferences
        var mLastUploadSMSTime: Long = 0

        override fun onPreExecute() {
            super.onPreExecute()
            mPreferences = PreferenceHelper(mContext).getPreference()
            mConfiguredAPI = mPreferences.getString(PreferenceHelper.CONFIGURED_API, "")
            mSecurityKey = mPreferences.getString(PreferenceHelper.SECURITY_KEY, "")
            mLastUploadSMSTime = mPreferences.getLong(PreferenceHelper.LAST_UPDATED_SMS_TIMESTAMP, 0)
        }

        override fun doInBackground(vararg params: Void?): Boolean {

            if (mConfiguredAPI.isNotEmpty() && mSecurityKey.isNotEmpty()) {

                var lAllSMSList: MutableList<SMS> =
                    SMSHelper().getAllMessages(mContext, mLastMessageTime = mLastUploadSMSTime)

                var lHeader = mutableMapOf<String, String>()
                val lMapper = ObjectMapper()
                val lURLFetcher = HttpURLFetcher()
                lHeader["Authorization"] = "Bearer $mSecurityKey"
                var lRequest = HttpRequest(
                    mURL = mConfiguredAPI,
                    mHeader = lHeader,
                    mHttpRequestMethod = HttpRequestMethod.POST,
                    mTimeOut = 120
                )
                do {

                    Log.d(TAG, "The SMS list size is : ${lAllSMSList.size}")
                    var lDataArray: JSONArray
                    if (lAllSMSList.size > 50) {
                        var lList = lAllSMSList.subList((lAllSMSList.lastIndex.minus(50)), lAllSMSList.lastIndex)
                        lDataArray = JSONArray(lMapper.writeValueAsString(lList))
                        mLastUploadSMSTime = lList[0].timestamp
                        lAllSMSList.removeAll(lList)
                    } else {
                        lDataArray = JSONArray(lMapper.writeValueAsString(lAllSMSList))
                        mLastUploadSMSTime = lAllSMSList[0].timestamp
                        lAllSMSList.clear()
                    }
                    var lPayloadJson = JSONObject()
                    var lSMSJson = JSONObject()
                    lSMSJson.put("data", lDataArray)
                    lPayloadJson.put("sms", lSMSJson)

                    Log.d(TAG, "The SMS payload is : $lPayloadJson")

                    lRequest.mPayload = lPayloadJson.toString().toByteArray()

                    val lResponse = lURLFetcher.doFetch(lRequest)

                    if (lResponse.mResponseStatusCode == HttpURLConnection.HTTP_OK) {
                        publishProgress(mLastUploadSMSTime)
                    }else{
                        return false
                    }
                    Log.d(TAG, "The SMS request is success ")

                } while (lAllSMSList.isNotEmpty())
                return true
            }
            return false
        }

        override fun onProgressUpdate(vararg values: Long?) {
            super.onProgressUpdate(*values)
            mPreferences.edit().putLong(PreferenceHelper.LAST_UPDATED_SMS_TIMESTAMP, values[0]!!).apply()
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            mPreferences.edit().putLong(PreferenceHelper.LAST_UPDATED_SMS_TIMESTAMP, mLastUploadSMSTime).apply()

        }

    }

//    companion object {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            for (i in 0..800012)
//        }
//    }

}