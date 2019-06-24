package com.simonmisles.powerbackup.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.simonmisles.powerbackup.Helper.JobScheduleHelper

class BootCompletedReceiver : BroadcastReceiver(){
    val TAG: String = "BootCompletedReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG,"Boot completed in .receiver.BootCompletedReceiver")
        JobScheduleHelper(context).schedule()
    }

}