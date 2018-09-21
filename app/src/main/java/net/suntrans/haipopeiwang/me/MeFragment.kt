package net.suntrans.haipopeiwang.me

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_me.*

import net.suntrans.haipopeiwang.BaseFragment
import net.suntrans.haipopeiwang.R

/**
 * Created by Looney on 2018/9/6.
 * Des:
 */
class MeFragment : BaseFragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_me, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        fixStatusBar()
        val toolbar = view!!.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.title_me)
        Glide.with(context)
                .load(tempAvatar)
                .into(avatar)
        initListener()
    }

    private fun initListener() {
        userInfo.setOnClickListener(this)
        deviceState.setOnClickListener(this)
        messageCenter.setOnClickListener(this)
        logs.setOnClickListener(this)
        accountManager.setOnClickListener(this)
        signFamily.setOnClickListener(this)
        setting.setOnClickListener(this)
    }


    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.userInfo -> {
            }
            R.id.deviceState -> {
            }
            R.id.messageCenter -> {
            }
            R.id.logs -> {
            }
            R.id.accountManager -> {
            }
            R.id.signFamily -> {
            }
            R.id.setting -> {
            }
        }
    }

    companion object {
        val tempAvatar = "https://wx.qlogo.cn/mmopen/vi_32/HVkws6luSHL4D9slrT5rrGVnKxBTsagyViamZ7seAnyjNDXjxsrQp4jwq8otyTH9hMB1qLCbCL1GorEvso0mHxQ/132"

        fun newInstance(): MeFragment {
            val args = Bundle()

            val fragment = MeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
