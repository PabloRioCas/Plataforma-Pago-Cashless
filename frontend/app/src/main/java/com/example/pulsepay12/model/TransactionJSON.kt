package com.example.pulsepay12.model

import java.io.Serializable
import java.util.Date

class TransactionJSON (var type: String? = null,
                       var qty: String? = null,
                       var description: String? = null,
                       var origin: String? =null,
                       var date: Date? =null): Serializable {

}