package net.suntrans.haipopeiwang.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qmuiteam.qmui.widget.QMUITabSegment
import kotlinx.android.synthetic.main.fragment_tab.*
import net.suntrans.haipopeiwang.BaseFragment
import net.suntrans.haipopeiwang.R
import net.suntrans.haipopeiwang.adapter.FragmentAdapter
import net.suntrans.haipopeiwang.energy.EnergyFragment
import net.suntrans.haipopeiwang.home.HomeFragment
import net.suntrans.haipopeiwang.home.HomeFragmentPresenter
import net.suntrans.haipopeiwang.me.MeFragment
import net.suntrans.haipopeiwang.room.RoomFragment
import net.suntrans.haipopeiwang.room.RoomPresenter

/**
 * Created by Looney on 2018/8/15.
 * Des:
 */
class TabFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTabs()
        initPagers()
    }


    private fun initTabs() {
        val normalColor = context!!.resources.getColor(R.color.black)
        val selectColor = context!!.resources.getColor(R.color.colorPrimary)
        mTabSegment!!.setDefaultNormalColor(normalColor)
        mTabSegment!!.setDefaultSelectedColor(selectColor)
        val component = QMUITabSegment.Tab(
                ContextCompat.getDrawable(this.context!!, R.drawable.logo),
                ContextCompat.getDrawable(this.context!!, R.drawable.logo_selected),
                "三川智家", false
        )
        val util = QMUITabSegment.Tab(
                ContextCompat.getDrawable(this.context!!, R.drawable.home),
                ContextCompat.getDrawable(this.context!!, R.drawable.home_select),
                "房间", false
        )
        val lab = QMUITabSegment.Tab(
                ContextCompat.getDrawable(this.context!!, R.drawable.energy),
                ContextCompat.getDrawable(this.context!!, R.drawable.energy_selected),
                "能耗", false
        )

        val mine = QMUITabSegment.Tab(
                ContextCompat.getDrawable(this.context!!, R.drawable.mine_un_selected),
                ContextCompat.getDrawable(this.context!!, R.drawable.mine_selected),
                "我的", false
        )
        mTabSegment!!.addTab(component)
                .addTab(util)
                .addTab(lab)
                .addTab(mine)
        mTabSegment!!.notifyDataChanged()
    }

    private fun initPagers() {
        val pagerAdapter = FragmentAdapter(childFragmentManager)

        //主页fragment
        val homeFragment = HomeFragment.newInstance()
        val homeFragmentPresenter = HomeFragmentPresenter(homeFragment)
        homeFragmentPresenter.onStart()

        //房间
        val roomFragment = RoomFragment.newInstance()
        val roomFragmentPresenter = RoomPresenter(roomFragment)
        roomFragmentPresenter.onStart()

        pagerAdapter.addFragment(homeFragment, "Home")
        pagerAdapter.addFragment(roomFragment, "Room")
        pagerAdapter.addFragment(EnergyFragment.newInstance(), "Energy")
        pagerAdapter.addFragment(MeFragment.newInstance(), "Me")
        mViewPager.offscreenPageLimit = 3
        mViewPager.adapter = pagerAdapter
        mTabSegment!!.setupWithViewPager(mViewPager,false)
    }



    override fun onDestroyView() {

        super.onDestroyView()
    }


}