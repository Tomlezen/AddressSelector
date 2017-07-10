package com.tlz.addressselector.model

/**
 *
 * Created by Tomlezen.
 * Date: 2017/7/10.
 * Time: 12:03.
 */
class Province(name: String, var cityList: MutableList<City> = mutableListOf<City>()): Name(name) {

    fun hasCity() = cityList.isNotEmpty()

}