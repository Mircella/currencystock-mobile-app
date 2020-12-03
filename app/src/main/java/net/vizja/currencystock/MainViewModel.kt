package net.vizja.currencystock

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.neovisionaries.i18n.CurrencyCode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class MainViewModel(private val applicationContext: Context) : BaseViewModel() {

    @Inject
    lateinit var currencyStockApiService: CurrencyStockApiService

    val login: MutableLiveData<String> = mutableLiveData()
    val accountCreationResult: MutableLiveData<AccountCreationResult> = mutableLiveData()

    val amount: MutableLiveData<String> = mutableLiveData()
    val currencyCode: MutableLiveData<String> = mutableLiveData()

    val amountToTopUp: MutableLiveData<String> = mutableLiveData()
    val amountToWithdraw: MutableLiveData<String> = mutableLiveData()
    val amountToBuy: MutableLiveData<String> = mutableLiveData()
    val currencyCodeToBuy: MutableLiveData<String> = mutableLiveData()
    val currencyCodeToCheck: MutableLiveData<String> = mutableLiveData()

    val amountToSell: MutableLiveData<String> = mutableLiveData()
    val currencyCodeToSell: MutableLiveData<String> = mutableLiveData()

    val exchangeRate: MutableLiveData<ExchangeRate> = mutableLiveData()
    val exchangeAccount: MutableLiveData<ExchangeAccount> = mutableLiveData()
    val exchangeAccountHistory: MutableLiveData<ExchangeAccountHistory> = mutableLiveData()

    private lateinit var accountCreatedDisposable: Disposable
    private lateinit var exchangeAccountCreatedDisposable: Disposable
    private lateinit var exchangeAccountUpdatedDisposable: Disposable
    private lateinit var exchangeAccountFetchedDisposable: Disposable
    private lateinit var exchangeAccountHistoryFetchedDisposable: Disposable
    private lateinit var exchangeRateFetchedDisposable: Disposable

    private var compositeDisposable: CompositeDisposable

    init {
        compositeDisposable = CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun createAccount() {
        val login = login.value
        login?.let {
            val request = CreateAccountRequest(it)
            accountCreatedDisposable = currencyStockApiService.createAccount(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Timber.d("Retrieved Account Id")
                        showToast("Account was successfully created")
                        accountCreationResult.value = AccountCreationResult(it.id)
                    },
                    { showToast("Failed to create account") }
                ).addTo(compositeDisposable)
        }
    }

    fun createExchangeAccount() {
        val accountId = accountCreationResult.value?.accountId
        val amount = amount.value?.toDouble()
        val currencyCode =
            currencyCode.value?.toUpperCase(Locale.ROOT)?.let { CurrencyCode.getByCode(it) }
        if (amount != null && currencyCode != null && accountId != null) {
            if (amount < 0) {
                showToast("Amount cannot be negative")
            } else {
                val request =
                    CreateExchangeAccountRequest(accountId, BigDecimal(amount), currencyCode)
                exchangeAccountCreatedDisposable =
                    currencyStockApiService.createExchangeAccount(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { showToast("Exchange account created") },
                            { showToast("Failed to create exchange account") }
                        ).addTo(compositeDisposable)
            }
        }
    }

    fun topUpExchangeAccount() {
        val accountId = accountCreationResult.value?.accountId
        val amount = amountToTopUp.value?.toDouble()
        if (amount != null && accountId != null) {
            if (amount < 0) {
                showToast("Amount cannot be negative")
            } else {
                val request = UpdateExchangeAccountRequest(accountId, BigDecimal(amount))
                exchangeAccountUpdatedDisposable =
                    currencyStockApiService.topUpExchangeAccount(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { showToast("Exchange account was topped up") },
                            { showToast("Failed to update exchange account") }
                        ).addTo(compositeDisposable)
            }
        }
    }

    fun withdrawExchangeAccount() {
        val accountId = accountCreationResult.value?.accountId
        val amount = amountToWithdraw.value?.toDouble()
        if (amount != null && accountId != null) {
            if (amount < 0) {
                showToast("Amount cannot be negative")
            } else {
                val request = UpdateExchangeAccountRequest(accountId, BigDecimal(amount))
                exchangeAccountUpdatedDisposable =
                    currencyStockApiService.withdrawExchangeAccount(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { showToast("Exchange account was withdrawn") },
                            { showToast("Failed to withdraw exchange account") }
                        ).addTo(compositeDisposable)
            }
        }
    }

    fun buyCurrency() {
        val accountId = accountCreationResult.value?.accountId
        val amount = amountToBuy.value?.toDouble()
        val currencyCode = currencyCode(currencyCodeToBuy.value?.toUpperCase(Locale.ROOT))
        if (amount != null && accountId != null && currencyCode != null) {
            if (amount < 0) {
                showToast("Amount cannot be negative")
            } else {
                val request = ExchangeCurrencyRequest(accountId, BigDecimal(amount), currencyCode)
                exchangeAccountUpdatedDisposable =
                    currencyStockApiService.buyCurrency(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { showToast("$amount $currencyCode was bought") },
                            { showToast("Failed to buy $amount $currencyCode") }
                        ).addTo(compositeDisposable)
            }
        }
    }

    fun sellCurrency() {
        val accountId = accountCreationResult.value?.accountId
        val amount = amountToSell.value?.toDouble()
        val currencyCode = currencyCode(currencyCodeToSell.value?.toUpperCase(Locale.ROOT))
        if (amount != null && accountId != null && currencyCode != null) {
            if (amount < 0) {
                showToast("Amount cannot be negative")
            } else {
                val request = ExchangeCurrencyRequest(accountId, BigDecimal(amount), currencyCode)
                exchangeAccountUpdatedDisposable =
                    currencyStockApiService.sellCurrency(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { showToast("$amount $currencyCode was sold") },
                            { showToast("Failed to sell $amount $currencyCode") }
                        ).addTo(compositeDisposable)
            }
        }
    }

    fun checkCurrencyExchangeRate() {
        val currencyCode = currencyCode(currencyCodeToCheck.value?.toUpperCase(Locale.ROOT))
        if (currencyCode != null) {
            exchangeRateFetchedDisposable =
                currencyStockApiService.checkCurrencyExchangeRate(currencyCode.name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Timber.d("ExchangeRate:$it")
                            exchangeRate.value = it
                        },
                        {
                            Timber.e("Error:$it")
                            showToast("Failed to check exchange rate for $currencyCode")
                        }
                    ).addTo(compositeDisposable)
        }
    }

    private fun currencyCode(currencyCode: String?): CurrencyCode? {
        return try {
            val code = currencyCode?.let { CurrencyCode.valueOf(it) }
            if (code == CurrencyCode.UNDEFINED) {
                showToast("Invalid currency code")
                null
            } else {
                code
            }
        } catch (e: Exception) {
            showToast("Invalid currency code")
            null
        }
    }

    fun checkExchangeAccount() {
        val accountId = accountCreationResult.value?.accountId
        if (accountId != null) {
            exchangeAccountCreatedDisposable =
                currencyStockApiService.getExchangeAccountById(accountId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            exchangeAccount.value = it
                        },
                        { showToast("Failed to fetch exchange account for $accountId") }
                    ).addTo(compositeDisposable)
        }
    }

    fun listExchangeAccountTransactions() {
        val accountId = accountCreationResult.value?.accountId
        if (accountId != null) {
            exchangeAccountHistoryFetchedDisposable =
                currencyStockApiService.getExchangeAccountHistory(accountId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            exchangeAccountHistory.value = it
                        },
                        { showToast("Failed to fetch transactions history for $accountId") }
                    ).addTo(compositeDisposable)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
            .show()
    }
}