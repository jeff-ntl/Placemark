package org.wit.placemark.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.placemark.R
import org.wit.placemark.helpers.readImage
import org.wit.placemark.helpers.readImageFromPath
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel


class MainActivity : AppCompatActivity(), AnkoLogger {

    var placemark = PlacemarkModel()
    lateinit var app: MainApp
    var edit = false
    //for image picker function
    val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app = application as MainApp

        if (intent.hasExtra("placemark_edit")) {
            edit = true
            placemark = intent.extras.getParcelable<PlacemarkModel>("placemark_edit")
            placemarkTitle.setText(placemark.title)
            description.setText(placemark.description)
            btnAdd.setText(R.string.button_savePlaceMark)
            placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))
            if (placemark.image != null) {
                chooseImage.setText(R.string.button_changeImage)
            }
        }

        btnAdd.setOnClickListener {
            placemark.title = placemarkTitle.text.toString()
            placemark.description = description.text.toString()
            if (placemark.title.isNotEmpty()) {
                //app.placemarks.add(placemark.copy())


                //exercise 3 solution

                if (edit) {
                    info("checkString works!")
                    app.placemarks.update(placemark.copy())
                } else {
                    app.placemarks.create(placemark.copy())
                }

                //app.placemarks.forEach { info("add Button Pressed: ${it}")}
                //app.placemarks.findAll().forEach { info("add Button Pressed: ${it}")}
                info("add Button Pressed: $placemarkTitle")
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            } else {
                toast(getString(R.string.toast_message))
            }
        }
        //Add action bar and set title
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        chooseImage.setOnClickListener {
            info("Select image")
            showImagePicker(this, IMAGE_REQUEST)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    placemark.image = data.getData().toString()
                    placemarkImage.setImageBitmap(readImage(this,resultCode,data))
                    chooseImage.setText(R.string.button_changeImage)
                }
            }
        }

    }
}