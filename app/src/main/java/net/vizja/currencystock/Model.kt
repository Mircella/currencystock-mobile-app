package net.vizja.currencystock

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class AccountCreationResult(val accountId: UUID) {

    override fun toString() = accountId.toString()
}

enum class TransactionType {
    BUY, SELL, TOP_UP, WITHDRAW
}

data class AccountId(val id: UUID)

data class AccountDetails(val login: String)

class SuccessResponse(val message: String, val accountId: UUID?)

data class ExchangeRate(
    val code: String,
    val buyPrice: BigDecimal,
    val sellPrice: BigDecimal,
    val date: String
){

    override fun toString() = """For $date:
        |1 $code = $buyPrice PLN
        |1 $code = $sellPrice PLN
    """.trimMargin()
}

data class ExchangeAccount(
    val accountId: UUID,
    val amount: BigDecimal,
    val currencyCode: String
) {
    override fun toString() = """
        |$amount $currencyCode
    """.trimMargin()
}

data class ExchangeAccountHistory(
    val accountId: UUID,
    val transactions: List<ExchangeTransaction>
) {
    override fun toString() = """
        |Transactions history:
        |date:   type:    amount:    currency:   exchange rate:
        |${transactions}
    """.trimMargin()
}

data class ExchangeTransaction(
    val happenedAt: String,
    val type: TransactionType,
    val amount: BigDecimal,
    val currencyCode: String,
    val exchangeRate: BigDecimal?
) {
    override fun toString() = """
        |$happenedAt    $type   $amount     $currencyCode   ${exchangeRate ?: ""}
    """.trimMargin()
}