package com.example.order.application.port.out

import com.example.order.domain.model.ErrorReason
import com.example.order.domain.model.Order
import com.example.order.domain.model.enums.OrderStatus
import com.example.order.domain.util.OrderId

interface OrderPersistencePort {
	fun persist(order: Order) : Result<Unit>
	fun updateOrderStatus(id: OrderId, pending: OrderStatus): Result<Unit>
	fun updateOrderStatus(id: OrderId, pending: OrderStatus, error: ErrorReason): Result<Unit>
}
