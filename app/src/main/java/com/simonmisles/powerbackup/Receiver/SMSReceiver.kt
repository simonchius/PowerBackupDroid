package com.simonmisles.powerbackup.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.simonmisles.powerbackup.Helper.JobScheduleHelper

class SMSReceiver : BroadcastReceiver(){
    val TAG: String = "SMSReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
//        Toast.makeText(context,"New SMS Received", Toast.LENGTH_LONG).show()
        Log.d(TAG,"New SMS received")
        JobScheduleHelper(context!!).schedule()
    }

}