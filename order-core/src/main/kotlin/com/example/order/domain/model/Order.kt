package com.example.order.domain.model

import com.example.order.domain.model.enums.OrderStatus
import com.example.order.domain.model.enums.PaymentMethod
import com.example.order.domain.util.OrderId

data class Order(
	val id: OrderId,
	val customer: Customer,
	val items: List<Item>,
	val courierData: CourierInformation,
	val paymentMethod: PaymentMethod,
	val status: OrderStatus
)
