package com.simonmisles.powerbackup.Helper

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.simonmisles.powerbackup.Pojo.SMS

class SMSHelper {
    val TAG = "SMSHelper"
    fun getAllMessages(mContext: Context, mLastMessageTime: Long = 0): MutableList<SMS>{
        val lUserIdentifier = PreferenceHelper(mContext).getPreference().getString(PreferenceHelper.USER_IDENTIFIER,"")
        var lSmsList = mutableListOf<SMS>()
        val lSMSUri : Uri = Uri.parse("content://sms/inbox")
        val lCursor: Cursor = mContext.contentResolver.query(lSMSUri, arrayOf("_id","body","address","date","sim_slot"), "date > $mLastMessageTime", null, "date DESC")
        if(lCursor.moveToFirst()){
            do{
                lSmsList.add(
                    SMS(
                        lCursor.getString(0),
                        lCursor.getString(1),
                        lCursor.getString(2),
                        lCursor.getLong(3),
                        lUserIdentifier,
                        "SIM ${lCursor.getInt(4)+1}"
                    )
                )

            }while (lCursor.moveToNext())
        }
        lCursor.close()
        return lSmsList
    }
}