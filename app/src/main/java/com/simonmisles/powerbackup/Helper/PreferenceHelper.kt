package com.simonmisles.powerbackup.Helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.simonmisles.powerbackup.BuildConfig

class PreferenceHelper(val mContext: Context) {

    companion object{
        val CONFIGURED_API                  = "configured_api"
        val SECURITY_KEY                    = "security_key"
        val USER_IDENTIFIER                 = "user_identifier"
        val LAST_UPDATED_SMS_TIMESTAMP      = "last_updated_timestamp"
        val IS_CONFIGURED                   = "is_configured"
        val CAN_BACKUP                      = "can_backup"
    }

    fun getPreference() : SharedPreferences{
        return mContext.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
    }

}