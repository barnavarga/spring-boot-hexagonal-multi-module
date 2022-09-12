package com.example.order.domain.model

import com.example.order.domain.model.enums.CourierProvider
import com.example.order.domain.util.CourierId

data class Courier(
	val id: CourierId,
	val provider: CourierProvider
)
