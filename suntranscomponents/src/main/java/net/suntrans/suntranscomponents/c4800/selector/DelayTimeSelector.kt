package net.suntrans.suntranscomponents.c4800.selector


import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView

import net.suntrans.suntranscomponents.R
import kotlin.math.max

/**
 * Created by Looney on 2018/9/19.
 * Des:
 */
class DelayTimeSelector : BottomSheetDialogFragment() {


    companion object {
        fun newInstance(maxValue: Int): DelayTimeSelector {
            val dialogFragment = DelayTimeSelector()
            val bundle = Bundle()
            bundle.putInt("maxValue", maxValue)
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }


    private var queding: TextView? = null
    private var qvxiao: TextView? = null
    private var numberPicker: NumberPicker? = null
    private var displayedValues = ArrayList<String>()
    private var maxValue = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maxValue = arguments.getInt("maxValue")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.selector_delay_time, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        numberPicker = view!!.findViewById(R.id.numberPicker)
        queding = view.findViewById(R.id.queding)
        qvxiao = view.findViewById(R.id.qvxiao)
        qvxiao!!.setOnClickListener {
            dismiss()
        }
        queding!!.setOnClickListener {

            if (listener != null) {
                listener!!.onGetValue(numberPicker!!.value)
            }
            dismiss()
        }

        val min = resources.getString(R.string.unit_min)
        for (i in 0..maxValue) {
            displayedValues!!.add(i.toString() + min)
        }
        val toArray = displayedValues.toTypedArray()

        numberPicker!!.displayedValues = toArray
        numberPicker!!.minValue = 0
        numberPicker!!.maxValue = toArray.size - 1

    }


    private var listener: numberSelectListener? = null

    public fun getNumberSelectListener(): numberSelectListener? {
        return listener
    }

    public fun setNumberSelectListener(loadListener: numberSelectListener) {
        this.listener = loadListener
    }

    interface numberSelectListener {
        fun onGetValue(value: Int)
    }
}
