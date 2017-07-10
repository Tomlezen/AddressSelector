package com.tlz.addressselector.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tlz.addressselector.AsDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    btn_show.setOnClickListener {
      AsDialog().show(supportFragmentManager, "test")
    }

  }
}
