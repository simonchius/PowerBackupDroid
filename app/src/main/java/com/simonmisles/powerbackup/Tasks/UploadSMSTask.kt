package com.simonmisles.powerbackup.Tasks

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.simonmisles.powerbackup.Helper.Logger
import com.simonmisles.powerbackup.Helper.PreferenceHelper
import com.simonmisles.powerbackup.Helper.SMSHelper
import com.simonmisles.powerbackup.Http.HttpRequest
import com.simonmisles.powerbackup.Http.HttpRequestMethod
import com.simonmisles.powerbackup.Http.HttpURLFetcher
import com.simonmisles.powerbackup.JobSchedulerService
import com.simonmisles.powerbackup.Pojo.SMS
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection

/**
 * This is a Upload SMS AsyncTask which sends the non backed up SMS to server
 * @param mContext is used to access to preference value (API, Security Key, Last Sync Time)
 * @param mSyncFromTime We can request this class to sync from a timestamp which will not modify the last sync time in preference.
 * It is an optional param, if you don't send it, it syncs the value from the last sync point
 */
class UploadSMSTask(val mContext: Context, val mSyncFromTime: Long = -1L) : AsyncTask<Void, Long, Boolean>() {
    lateinit var mConfiguredAPI: String
    lateinit var mSecurityKey: String
    lateinit var mPreferences: SharedPreferences
    var mLastUploadSMSTime: Long = 0
    var mUpdateSMSSyncPoint: Boolean = false
    var mIsConfigured: Boolean = false

    companion object {
        val MAX_SMS_TO_SERVER_IN_A_REQUEST = 50
    }

    override fun onPreExecute() {
        super.onPreExecute()
        mPreferences = PreferenceHelper(mContext).getPreference()
        mConfiguredAPI = mPreferences.getString(PreferenceHelper.CONFIGURED_API, "")
        mSecurityKey = mPreferences.getString(PreferenceHelper.SECURITY_KEY, "")
        mIsConfigured = mPreferences.getBoolean(PreferenceHelper.IS_CONFIGURED, false)
        if (mSyncFromTime == -1L) {
            mLastUploadSMSTime = mPreferences.getLong(PreferenceHelper.LAST_UPDATED_SMS_TIMESTAMP, 0)
            mUpdateSMSSyncPoint = true
        } else {
            mLastUploadSMSTime = mSyncFromTime
            mUpdateSMSSyncPoint = false
        }
    }

    override fun doInBackground(vararg params: Void?): Boolean {

        if (mIsConfigured) {

            var lAllSMSList: MutableList<SMS> =
                SMSHelper().getAllMessages(mContext, mLastMessageTime = mLastUploadSMSTime)

            if (lAllSMSList.isEmpty()) return false

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

                Logger.debug(JobSchedulerService.TAG, "The SMS list size is : ${lAllSMSList.size}")
                var lDataArray: JSONArray

                if (lAllSMSList.size > MAX_SMS_TO_SERVER_IN_A_REQUEST) {
                    var lList = lAllSMSList.subList(
                        (lAllSMSList.lastIndex.minus(MAX_SMS_TO_SERVER_IN_A_REQUEST)),
                        lAllSMSList.lastIndex
                    )
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

                Logger.debug(JobSchedulerService.TAG, "The SMS payload is : $lPayloadJson")

                lRequest.mPayload = lPayloadJson.toString().toByteArray()

                val lResponse = lURLFetcher.doFetch(lRequest)

                if (lResponse.mResponseStatusCode == HttpURLConnection.HTTP_OK) {
                    publishProgress(mLastUploadSMSTime)
                } else {
                    return false
                }
                Logger.debug(JobSchedulerService.TAG, "The SMS request is success ")

            } while (lAllSMSList.isNotEmpty())
            return true
        }
        return false
    }

    override fun onProgressUpdate(vararg values: Long?) {
        super.onProgressUpdate(*values)
        if (mUpdateSMSSyncPoint) {
            mPreferences.edit().putLong(PreferenceHelper.LAST_UPDATED_SMS_TIMESTAMP, values[0]!!).apply()
        }
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (mUpdateSMSSyncPoint) {
            mPreferences.edit().putLong(PreferenceHelper.LAST_UPDATED_SMS_TIMESTAMP, mLastUploadSMSTime).apply()
        }

    }

}