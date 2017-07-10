package com.tlz.addressselector

import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ListView


/**
 *
 * Created by Tomlezen.
 * Date: 2017/7/10.
 * Time: 14:18.
 */
class AsViewPagerAdapter(val vp: ViewPager) : PagerAdapter() {

    private var views: MutableList<ListView> = ArrayList()

    fun setDatas(views: MutableList<ListView>) {
        this.views = views
        notifyDataSetChanged()
    }

    fun addData(view: ListView) {
        views.add(view)
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun removeData(position: Int) {
        val lists = arrayListOf<ListView>()
        for (i in position + 1..views.size - 1) {
            lists.add(views[i])
            vp!!.removeView(views[i])
        }
        views.removeAll(lists)
        notifyDataSetChanged()
    }

    fun getDatas(): List<ListView> {
        return views
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(views[position])
        return views[position]
    }

}