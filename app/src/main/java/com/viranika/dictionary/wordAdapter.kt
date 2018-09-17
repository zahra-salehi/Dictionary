package com.viranika.dictionary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList

class wordAdapter
(context: Context, words: ArrayList<word>) : ArrayAdapter<word>(context, 0, words) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.list_item, parent, false)
        }

        val currentWord = getItem(position)

        val wordTextView = listItemView!!.findViewById(R.id.text_view1) as TextView
        wordTextView.setText(currentWord!!.englishTranslationId)

        val translateTextView = listItemView.findViewById(R.id.text_view2) as TextView
        translateTextView.setText(currentWord.persianTranslationId)

        val imageView = listItemView.findViewById(R.id.imageView) as ImageView
            imageView.setImageResource(currentWord.imageResourceId)

        return listItemView
    }
}

