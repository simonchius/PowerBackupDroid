package com.simonmisles.powerbackup

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.AsyncTask
import android.util.Log
import com.simonmisles.powerbackup.Helper.Logger
import com.simonmisles.powerbackup.Tasks.UploadSMSTask


class JobSchedulerService : JobService() {
    companion object {
        val TAG = "JobSchedulerService"
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Logger.debug(TAG, "Job has Started")
        UploadSMSTask(applicationContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Logger.debug(TAG, "Job has killed")

        return false
    }

}