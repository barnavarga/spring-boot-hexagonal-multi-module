package com.example.order.domain.model.error

open class ShipmentProviderException(
	title: String, detail: String
) : DomainException(errorType = ERROR_TYPE, title = title, detail = detail) {
	companion object {
		const val ERROR_TYPE = "shipment-error"
	}
}