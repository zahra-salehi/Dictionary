package com.viranika.dictionary

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val Type = Typeface.createFromAsset(assets,"font/IRANSansWeb_Medium.ttf")
        family_edit_text.setTypeface(Type)
        color_edit_text.setTypeface(Type)
        number_edit_text.setTypeface(Type)
        phrase_edit_text.setTypeface(Type)
    }

    fun familyTranslate(view: View){
        val intent = Intent(this, FamilyActivity::class.java)
        startActivity(intent)
    }

    fun colorTranslate(view: View){
        val intent = Intent(this, ColorActivity::class.java)
        startActivity(intent)
    }

    fun numberTranslate(view: View){
        val intent = Intent(this, NumberActivity::class.java)
        startActivity(intent)
    }

    fun phraseTranslate(view: View){
        val intent = Intent(this, PhraseActivity::class.java)
        startActivity(intent)
    }
}
