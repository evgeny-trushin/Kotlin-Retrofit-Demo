package com.example.assignment.i002.view.model


import android.content.Context
import android.content.Intent
import android.databinding.ObservableField
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.widget.ArrayAdapter
import com.example.assignment.helpers.network.ErrorCode
import com.example.assignment.i002.model.api.ExpressNetworkModel
import com.example.assignment.i002.model.api.dto.SamplePojo
import com.example.assignment.i002.view.activity.AssignmentActivity
import com.example.assignment.i002.view.operation.ModelViewLoadingOps
import rx.android.schedulers.AndroidSchedulers
import java.io.Serializable

data class AssignmentViewModel(
        var data: List<SamplePojo>? = null,
        @Transient var context: AssignmentActivity? = null,
        override var isLoading: Boolean = false,
        override var error: Boolean = false,
        override var errorCode: ErrorCode = ErrorCode.ERROR_IGNORE) : Serializable, ModelViewLoadingOps {

    var text: String? = ""
    private var position: Int? = -1

    fun getData() {
        try {
            ExpressNetworkModel.getData().observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        context?.bindViewModel(AssignmentViewModel(data = it, error = false, isLoading = false))
                    }, context!!::bindThrowableToViewModel
            )
        } catch (error: Exception) {
            context?.bindThrowableToViewModel(error)
        }
    }

    override fun onRetry() {
        error = false
        isLoading = true
        context?.bindViewModel(this)
        getData()
    }

    var proxyPosition: Int
        get() = ObservableField<Int>(position).get()!!
        set(newSelectedPosition) {
            if (-1 == position || position != newSelectedPosition) {
                val it = data?.get(newSelectedPosition)
                text = "Car: " + it?.fromcentral?.car
                if (null != it?.fromcentral?.train) {
                    text += "\nTrain: " + it.fromcentral.train
                }
                position = newSelectedPosition
                context!!.bindViewModel(this)
            }
        }

    fun getSpinnerAdapter(context: Context): ArrayAdapter<String> {
        val array = ArrayList<String>()
        data?.let {
            for (datum in it) {
                array.add(datum.name)
            }
        }
        return ArrayAdapter(context, android.R.layout.simple_spinner_item, array)
    }

    fun clickOnButton(context: Context) {
        val location = (data?.get(proxyPosition) as SamplePojo).location
        val gmmIntentUri = Uri.parse("geo:" + location.latitude + "," + location.longitude)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.`package` = "com.google.android.apps.maps"
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(context, mapIntent, null)
        }
    }
}