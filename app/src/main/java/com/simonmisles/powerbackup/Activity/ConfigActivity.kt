package com.simonmisles.powerbackup.Activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.widget.Toast
import com.simonmisles.powerbackup.Constants.IntentConstants
import com.simonmisles.powerbackup.R
import com.simonmisles.powerbackup.Helper.JobScheduleHelper
import com.simonmisles.powerbackup.Helper.PreferenceHelper
import com.simonmisles.powerbackup.Helper.SMSHelper
import kotlinx.android.synthetic.main.activity_config.*

class ConfigActivity : AppCompatActivity() {
    val TAG = "ConfigActivity"
    lateinit var mPreferences: SharedPreferences
    var mApiString: String = ""
    var mSecurityKey: String = ""
    var mIdentifier: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        mPreferences = PreferenceHelper(this).getPreference()

        api_et.setText(mPreferences.getString(PreferenceHelper.CONFIGURED_API, ""))
        security_key_et.setText(mPreferences.getString(PreferenceHelper.SECURITY_KEY, ""))
        user_identifier.setText(mPreferences.getString(PreferenceHelper.USER_IDENTIFIER, ""))

        save_btn.setOnClickListener {
            mApiString = api_et.text.toString()
            if (TextUtils.isEmpty(mApiString)) {
                api_et.setError("API cannot be empty")
                return@setOnClickListener
            }
            mSecurityKey = security_key_et.text.toString()
            if (TextUtils.isEmpty(mSecurityKey)) {
                security_key_et.setError("Security key be empty")
                return@setOnClickListener
            }
            mIdentifier = user_identifier.text.toString()
            if (TextUtils.isEmpty(mIdentifier)) {
                api_et.setError("User Identifier cannot be empty")
                return@setOnClickListener
            }
            mPreferences.edit().putString(PreferenceHelper.CONFIGURED_API, mApiString).commit()
            mPreferences.edit().putString(PreferenceHelper.SECURITY_KEY, mSecurityKey).commit()
            mPreferences.edit().putString(PreferenceHelper.USER_IDENTIFIER, mIdentifier).commit()
            mPreferences.edit().putBoolean(PreferenceHelper.IS_CONFIGURED, true).commit()
            mPreferences.edit().putBoolean(PreferenceHelper.CAN_BACKUP, true).commit()
            Toast.makeText(this@ConfigActivity, "Configuration saved successfully", Toast.LENGTH_SHORT).show()
            JobScheduleHelper(this@ConfigActivity).schedule()
            SMSHelper().getAllMessages(this@ConfigActivity)
            setResult(
                Activity.RESULT_OK,
                Intent().putExtra(IntentConstants.IS_CONFIGURED, true).putExtra(IntentConstants.CAN_BACKUP, true)
            )
            finish()
        }

        close_btn.setOnClickListener { finish() }

        if (!checkSMSPermission()) {
            ActivityCompat.requestPermissions(
                this@ConfigActivity,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS),
                1000
            )
        }

    }

    fun checkSMSPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@ConfigActivity,
            Manifest.permission.RECEIVE_SMS
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this@ConfigActivity,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this@ConfigActivity,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}
