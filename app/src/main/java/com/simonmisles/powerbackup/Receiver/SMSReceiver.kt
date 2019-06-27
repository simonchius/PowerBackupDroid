package com.simonmisles.powerbackup.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.simonmisles.powerbackup.Helper.JobScheduleHelper
import com.simonmisles.powerbackup.Helper.PreferenceHelper

class SMSReceiver : BroadcastReceiver(){
    val TAG: String = "SMSReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null && PreferenceHelper(context).getPreference().getBoolean(PreferenceHelper.CAN_BACKUP, false)) {
            JobScheduleHelper(context).schedule()
        }
    }

}