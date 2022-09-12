package com.example.order.domain.model

import com.example.order.domain.util.CustomerId

data class Customer(
	val id: CustomerId,
	val name: String,
	val address: Address
)
