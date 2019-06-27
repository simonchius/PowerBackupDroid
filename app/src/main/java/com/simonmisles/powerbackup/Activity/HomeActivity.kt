package com.simonmisles.powerbackup.Activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.simonmisles.powerbackup.Constants.IntentConstants
import com.simonmisles.powerbackup.Helper.JobScheduleHelper
import com.simonmisles.powerbackup.Helper.PreferenceHelper
import com.simonmisles.powerbackup.R
import kotlinx.android.synthetic.main.activity_home_activity.*

class HomeActivity : AppCompatActivity() {

    companion object {
        val CONFIGURATION_REQUEST_CODE = 10101
    }

    lateinit var mPreference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_activity)

        mPreference = PreferenceHelper(this@HomeActivity).getPreference()

        if (mPreference.getBoolean(PreferenceHelper.IS_CONFIGURED, false)) {
            backup_enable_switch.visibility = View.VISIBLE
        } else {
            backup_enable_switch.visibility = View.GONE
        }

        backup_enable_switch.isChecked = mPreference.getBoolean(PreferenceHelper.CAN_BACKUP, false)

        backup_enable_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPreference.edit().putBoolean(PreferenceHelper.CAN_BACKUP, isChecked).apply()
        }
        backup_now_tv.setOnClickListener {
            if(mPreference.getBoolean(PreferenceHelper.IS_CONFIGURED, false)) {
                JobScheduleHelper(this@HomeActivity).schedule()
                Toast.makeText(this@HomeActivity,"Backing up...", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@HomeActivity,"Set Configuration to back up", Toast.LENGTH_SHORT).show()
            }
        }
        set_config_tv.setOnClickListener {
            startActivityForResult(
                Intent(this@HomeActivity, ConfigActivity::class.java),
                CONFIGURATION_REQUEST_CODE
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONFIGURATION_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val lCanBackUp = data.extras.getBoolean(IntentConstants.CAN_BACKUP, false)
            val lIsConfigured = data.extras.getBoolean(IntentConstants.IS_CONFIGURED, false)

            if (lIsConfigured) {
                backup_enable_switch.visibility = View.VISIBLE
            }

            backup_enable_switch.isChecked= lCanBackUp

        }
    }


}
