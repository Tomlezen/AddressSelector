package com.tlz.addressselector.model

/**
 *
 * Created by Tomlezen.
 * Date: 2017/7/10.
 * Time: 11:53.
 */
class City(name: String, var districtList: MutableList<District> = mutableListOf<District>()): Name(name){

    fun hasDistrictList() = districtList.isNotEmpty()

}