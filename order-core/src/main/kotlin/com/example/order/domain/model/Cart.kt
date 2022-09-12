package com.example.order.domain.model

import com.example.order.domain.util.CartId
import com.example.order.domain.util.CustomerId

data class Cart(
	val id: CartId,
	val customerId: CustomerId,
	val items: List<Item>
)
