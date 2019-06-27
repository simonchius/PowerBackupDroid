package com.simonmisles.powerbackup.Helper

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import com.simonmisles.powerbackup.JobSchedulerService

class JobScheduleHelper(val mContext: Context) {
    fun schedule() {
        val lPreference = PreferenceHelper(mContext).getPreference()
        if (lPreference.getBoolean(PreferenceHelper.IS_CONFIGURED, false)) {
            val lJobScheduler: JobScheduler = mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            lJobScheduler.cancelAll()
            val lJobInfoBuilder: JobInfo.Builder =
                JobInfo.Builder(1313, ComponentName(mContext, JobSchedulerService::class.java))
            lJobInfoBuilder.setPeriodic(900000) // 15 minutes in milliseconds is 900000.
            lJobInfoBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            if (lJobScheduler.schedule(lJobInfoBuilder.build()) < 1) {
                Log.d(TAG, "onCreate: Some error while scheduling the job")
            }
        }
    }
}