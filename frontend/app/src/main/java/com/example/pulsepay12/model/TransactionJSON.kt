package com.example.pulsepay12.model

import java.io.Serializable
import java.time.LocalDateTime

class TransactionJSON (var type: String? = null,
                       var qty: String? = null,
                       var description: String? = null,
                       var origin: String? =null,
                       var date: String? =null): Serializable {

}