package com.simonmisles.powerbackup.Helper

import android.util.Log
import com.simonmisles.powerbackup.BuildConfig
import java.lang.Exception

class Logger {
    companion object {
        fun info(pTAG: String, pMessage: String): Unit {
            if (BuildConfig.DEBUG) {
                Log.i(pTAG, pMessage)
            }
        }

        fun warning(pTAG: String, pMessage: String): Unit {
            if (BuildConfig.DEBUG) {
                Log.w(pTAG, pMessage)
            }
        }

        fun debug(pTAG: String, pMessage: String): Unit {
            if (BuildConfig.DEBUG) {
                Log.d(pTAG, pMessage)
            }
        }

        fun error(pTAG: String, pMessage: String, pException: Exception): Unit {
            if (BuildConfig.DEBUG) {
                Log.e(pTAG, pMessage, pException)
            }
        }
    }
}