package com.tlz.addressselector

/**
 *
 * Created by Tomlezen.
 * Date: 2017/7/10.
 * Time: 14:21.
 */
interface AsActionCallback {

    fun onSelected(address: String)

    fun onSelectedDetails(province: String, city: String, district: String)

}