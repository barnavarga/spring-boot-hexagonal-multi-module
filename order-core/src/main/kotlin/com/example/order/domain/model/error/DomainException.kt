package com.example.order.domain.model.error

/**
 * This is a base class for core-level exceptions. Preferably all business-logic exceptions should
 * derive from this class, it allows adapter layer to do some additional handling. For example, Web layer
 * can map these exceptions to corresponding HTTP response.
 */
open class DomainException(
	val errorType: String, val title: String, val detail: String? = null, val throwable: Throwable? = null
) : RuntimeException(detail, throwable)