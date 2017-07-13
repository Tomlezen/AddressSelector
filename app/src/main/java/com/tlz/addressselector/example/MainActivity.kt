package com.tlz.addressselector.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tlz.addressselector.AsDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    btn_show.setOnClickListener {
      AsDialog.show(supportFragmentManager, {address ->
        Log.e("AsDialog", "selected： $address")
      }, {province, city, district ->
        Log.e("AsDialog", "selected： province=$province; city=$city; district=$district")
      })
    }

  }
}
