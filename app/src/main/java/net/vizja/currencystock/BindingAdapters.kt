package net.vizja.currencystock

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

@BindingAdapter("mutableAccountCreationResult")
fun setAccountCreationResult(view: TextView, text: MutableLiveData<AccountCreationResult>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value.toString()})
    }
}

@BindingAdapter("mutableExchangeAccount")
fun setExchangeAccount(view: TextView, text: MutableLiveData<ExchangeAccount>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value.toString()})
    }
}

@BindingAdapter("mutableExchangeAccountHistory")
fun setExchangeAccountHistory(view: TextView, text: MutableLiveData<ExchangeAccountHistory>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value.toString()})
    }
}

@BindingAdapter("mutableExchangeRate")
fun setExchangeRate(view: TextView, text: MutableLiveData<ExchangeRate>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value.toString()})
    }
}