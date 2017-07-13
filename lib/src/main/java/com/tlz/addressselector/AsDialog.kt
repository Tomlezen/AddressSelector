package com.tlz.addressselector

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import com.tlz.addressselector.adapter.CityAdapter
import com.tlz.addressselector.adapter.DataAdapter
import com.tlz.addressselector.adapter.DistrictAdapter
import com.tlz.addressselector.adapter.ProvinceAdapter
import com.tlz.addressselector.model.City
import com.tlz.addressselector.model.District
import com.tlz.addressselector.model.Province
import kotlinx.android.synthetic.main.dialog_address_selector.*
import javax.xml.parsers.SAXParserFactory


/**
 *
 * Created by Tomlezen.
 * Date: 2017/7/10.
 * Time: 14:09.
 */
class AsDialog : BottomSheetDialogFragment(), DialogInterface.OnDismissListener, AdapterView.OnItemClickListener {

    /** 城市选择回调  */
    private var callBack: AsActionCallback? = null

    /** ViewPager适配器  */
    private val pagerAdapter: AsViewPagerAdapter by lazy {
        AsViewPagerAdapter(as_vp)
    }

    private val initContentData: String? = null

    internal fun show(manager: FragmentManager){
        super.show(manager, "AsDialog")
    }

    override fun show(manager: FragmentManager?, tag: String?) {

    }

    override fun show(transaction: FragmentTransaction?, tag: String?): Int {
        return -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setCancelable(false)
    }

    override fun setCancelable(cancelable: Boolean) {

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.dialog_address_selector, container, false) ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        as_vp.adapter = pagerAdapter
        as_indiacators.setupViewPager(as_vp)

        confirm.setOnClickListener {
            callBack?.onSelected(as_indiacators.getContent())
            val items = mutableListOf<String>()
            items.addAll(as_indiacators.getItems())
            if (items.size < 3) {
                for (i in 0..(3 - items.size)) {
                    items.add("")
                }
            }
            callBack?.onSelectedDetails(items[0], items[1], items[2])
            dismiss()
        }
        close.setOnClickListener { dismiss() }

        loadData()
    }

    private fun showConfirmBtn() {
        confirm?.visibility = View.VISIBLE
    }

    private fun hideConfirmBtn() {
        confirm?.visibility = View.GONE
    }

    private fun createListView(type: Int): ListView {
        val view = ListView(context)
        view.isVerticalScrollBarEnabled = false
        view.divider = ColorDrawable(Color.TRANSPARENT)
        val adapter: BaseAdapter
        if (type == 0)
            adapter = ProvinceAdapter()
        else if (type == 1)
            adapter = CityAdapter()
        else
            adapter = DistrictAdapter()
        view.adapter = adapter
        view.onItemClickListener = this
        return view
    }

    private fun loadProvinceData() {
        Thread(Runnable {
            val provinceList = parseXml()
            as_vp.post({
                (pagerAdapter.getDatas()[0].adapter as DataAdapter<Province>).setData(provinceList)
            })
        }).start()
    }

    private fun loadCityData(province: Province) {
        if (province.hasCity()) {
            hideConfirmBtn()
            val name = province.name
            if (name == "北京市" || name == "天津市" || name == "上海市" || name == "重庆市") {
                loadDistrict(province.cityList[0])
                return
            } else {
                pagerAdapter.addData(createListView(1))
                (pagerAdapter.getDatas()[as_vp.childCount - 1].adapter as DataAdapter<City>).setData(province.cityList)
            }
        } else if (TextUtils.isEmpty(as_indiacators.getContent())) {
            showConfirmBtn()
        }

        as_indiacators.changeItem(as_vp.currentItem, province.name, province.hasCity())
    }

    /**
     * 加载区数据
     */
    private fun loadDistrict(city: City) {
        if (city.hasDistrictList()) {
            hideConfirmBtn()
            pagerAdapter.addData(createListView(2))
            (pagerAdapter.getDatas()[as_vp.childCount - 1].adapter as DataAdapter<District>).setData(city.districtList)
        } else {
            showConfirmBtn()
        }

        as_indiacators.changeItem(as_vp.currentItem, city.name, city.hasDistrictList())
    }

    /**
     * 解析xml数据
     * @return
     */
    private fun parseXml(): List<Province> {
        val asset = context.assets
        try {
            val input = asset.open("province_data.xml")
            val spf = SAXParserFactory.newInstance()
            val parser = spf.newSAXParser()
            val handler = XmlParserHandler()
            parser.parse(input, handler)
            input.close()
            return handler.getDataList()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return arrayListOf()
    }

    private fun loadData() {
        pagerAdapter.addData(createListView(0))
        loadProvinceData()
        if (TextUtils.isEmpty(initContentData)) {
            as_indiacators.changeItem(0, "", true)
        } else {
            val items = initContentData?.split(",")
            for (i in 0..2) {
                if (i == 0) {

                }
            }
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (as_indiacators.isAnimatorRunning)
            return
        val currentItem = as_vp.currentItem
        val adapter = p0?.adapter
        when (adapter) {
            is ProvinceAdapter -> {
                if (as_indiacators.isSelectedItem(as_vp.currentItem)) {
                    pagerAdapter.removeData(currentItem)
                }
                loadCityData(adapter.getItem(p2) as Province)
            }
            is CityAdapter -> {
                if (as_indiacators.isSelectedItem(currentItem)) {
                    pagerAdapter.removeData(currentItem)
                }
                loadDistrict(adapter.getItem(p2) as City)
            }
            is DistrictAdapter -> {
                showConfirmBtn()
                as_indiacators.changeItem(currentItem, (adapter.getItem(p2) as District).name, false)
            }
            else -> {
            }
        }
        (adapter as? DataAdapter<*>)?.setSelectedPosition(p2)
    }

    internal fun setCallback(onSelected: (address: String) -> Unit, onSelectedDetails: (province: String, city: String, district: String) -> Unit) {
        callBack = object : AsActionCallback {
            override fun onSelectedDetails(province: String, city: String, district: String) {
                onSelectedDetails(province, city, district)
            }

            override fun onSelected(address: String) {
                onSelected(address)
            }
        }
    }

    companion object {
        fun show(fragment: Fragment, onSelected: (address: String) -> Unit, onSelectedDetails: (province: String, city: String, district: String) -> Unit): AsDialog {
            return show(fragment.fragmentManager, onSelected, onSelectedDetails)
        }

        fun show(activity: FragmentActivity, onSelected: (address: String) -> Unit, onSelectedDetails: (province: String, city: String, district: String) -> Unit): AsDialog {
            return show(activity.supportFragmentManager, onSelected, onSelectedDetails)
        }

        fun show(fragmentManager: FragmentManager, onSelected: (address: String) -> Unit, onSelectedDetails: (province: String, city: String, district: String) -> Unit): AsDialog {
            val asDialog = AsDialog()
            asDialog.setCallback(onSelected, onSelectedDetails)
            asDialog.show(fragmentManager)
            return asDialog
        }
    }

}