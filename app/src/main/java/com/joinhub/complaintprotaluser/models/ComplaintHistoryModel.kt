package com.joinhub.complaintprotaluser.models

data class ComplaintHistoryModel(val ticketNo:String,
                                 val areaCode:String,
                                 val issueType: String,
                                 val serviceRepresentativeName:String,
                                 val userName:String )
