package com.simonmisles.powerbackup.Pojo

import java.io.Serializable

data class SMS(

        var id: String,

        var msg: String,

        var sourceNumber: String,

        var timestamp: Long,

        val userIdentifier: String
) : Serializable,
    Comparable<SMS> {
    override fun compareTo(other: SMS): Int {
        return other.timestamp.compareTo(this.timestamp)
    }

}