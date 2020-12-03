package net.vizja.currencystock

import com.neovisionaries.i18n.CurrencyCode
import java.math.BigDecimal
import java.util.*

data class CreateAccountRequest(val login: String)

data class CreateExchangeAccountRequest(
    val accountId: UUID,
    val amount: BigDecimal,
    val currencyCode: CurrencyCode
)

data class UpdateExchangeAccountRequest (
    val accountId: UUID,
    val amount: BigDecimal
)

data class ExchangeCurrencyRequest(
    val accountId: UUID,
    val amount: BigDecimal,
    val currencyCode: CurrencyCode
)