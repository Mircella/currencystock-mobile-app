package net.vizja.currencystock

import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T : Any> LifecycleOwner.observe(liveData: LiveData<T>, body: (T) -> Unit) {
    liveData.observe(this, Observer { body(it!!) })
}

fun <T> mutableLiveData() = MutableLiveData<T>()

fun <T> mutableLiveData(onSetAction: (newValue: T?) -> Unit) =
    object : MutableLiveData<T>() {
        override fun setValue(value: T?) {
            super.setValue(value)
            onSetAction(value)
        }
    }

fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}