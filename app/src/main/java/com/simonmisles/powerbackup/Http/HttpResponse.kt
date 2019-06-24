package com.simonmisles.powerbackup.Http

data class HttpResponse(val mResponseStatusCode : Int = 0, val mResponseContent: String = "", val mResponseHeader: Map<String, List<String>> = mapOf())