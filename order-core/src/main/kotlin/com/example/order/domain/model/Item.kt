package com.example.order.domain.model

import com.example.order.domain.util.ItemId

data class Item(
	val id: ItemId,
	val name: String,
	val quantity: Number
)
