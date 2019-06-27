package com.simonmisles.powerbackup.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.simonmisles.powerbackup.Helper.JobScheduleHelper
import com.simonmisles.powerbackup.Helper.PreferenceHelper

class BootCompletedReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        if(PreferenceHelper(context).getPreference().getBoolean(PreferenceHelper.CAN_BACKUP, false)){
            JobScheduleHelper(context).schedule()
        }
    }

}