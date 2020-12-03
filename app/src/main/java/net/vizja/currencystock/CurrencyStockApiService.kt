package net.vizja.currencystock

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*

interface CurrencyStockApiService {

    @POST("accounts")
    fun createAccount(@Body request: CreateAccountRequest): Single<AccountId>

    @GET("accounts/{id}")
    fun getAccountById(@Path("id") id: UUID): Single<AccountDetails>

    @POST("exchangeAccounts")
    fun createExchangeAccount(@Body request: CreateExchangeAccountRequest): Single<SuccessResponse>

    @PUT("exchangeAccounts/topUp")
    fun topUpExchangeAccount(@Body request: UpdateExchangeAccountRequest): Single<SuccessResponse>

    @PUT("exchangeAccounts/withdraw")
    fun withdrawExchangeAccount(@Body request: UpdateExchangeAccountRequest): Single<SuccessResponse>

    @GET("exchangeAccounts/{accountId}")
    fun getExchangeAccountById(@Path("accountId") accountId: UUID): Single<ExchangeAccount>

    @GET("exchangeAccounts/{accountId}/history")
    fun getExchangeAccountHistory(@Path("accountId") accountId: UUID): Single<ExchangeAccountHistory>

    @POST("currency-stock/buy")
    fun buyCurrency(@Body request: ExchangeCurrencyRequest): Single<SuccessResponse>

    @POST("currency-stock/sell")
    fun sellCurrency(@Body request: ExchangeCurrencyRequest): Single<SuccessResponse>

    @GET("exchangeRates/{currencyCode}")
    fun checkCurrencyExchangeRate(@Path("currencyCode") currencyCode: String): Single<ExchangeRate>

}