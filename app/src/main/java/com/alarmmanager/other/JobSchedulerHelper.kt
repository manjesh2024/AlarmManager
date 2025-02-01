package com.alarmmanager.other

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import com.alarmmanager.Service.MyJobService

class JobSchedulerHelper(private val context: Context) {

    fun scheduleJob() {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(context, MyJobService::class.java)

        val jobInfo = JobInfo.Builder(123, componentName)
            .setPersisted(true) // Ensures job survives device reboots
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // Runs on any network
            .setPeriodic(15 * 60 * 1000).setPersisted(true) // Runs every 15 minutes
            .build()

        val result = jobScheduler.schedule(jobInfo)
        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d("JobScheduler", "Job Scheduled Successfully!")
        } else {
            Log.e("JobScheduler", "Job Scheduling Failed!")
        }
    }
}
