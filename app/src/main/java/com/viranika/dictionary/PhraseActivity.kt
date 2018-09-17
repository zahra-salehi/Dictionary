package com.viranika.dictionary

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_phrase.*

class PhraseActivity : AppCompatActivity() {

    var words: ArrayList<word> = ArrayList()
    var mediaPlayer: MediaPlayer?= null
    var mAudioManager: AudioManager?= null

    private val mCompletionListener = MediaPlayer.OnCompletionListener {
        releaseMediaPlayer()
    }

    private val mOnAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            mediaPlayer!!.pause()
            mediaPlayer!!.seekTo(0)
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            mediaPlayer!!.start()
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            releaseMediaPlayer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phrase)

        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        words.add(word("His cake is dough", "دستش نمک ندارد", R.raw.his))
        words.add(word("I felt like a fool", "سنگ رو یخ شدم", R.raw.felt))
        words.add(word("I wish the best for you", "بهترینها را برای شما آرزو می کنم", R.raw.wish))
        words.add(word("Eat your word", "حرفت را پس بگیر", R.raw.eat))
        words.add(word("You were sublime", "کارت حرف نداشت", R.raw.sublime))
        words.add(word("Never say die", "به دلت بد نیار", R.raw.die))
        words.add(word("Truth hurts", "حقیقت تلخ است", R.raw.truth))
        words.add(word("Happy go lucky", "سر به هوا", R.raw.happy))
        words.add(word("I buy it", "من قبول دارم", R.raw.buy))
        words.add(word("It was brutal", "خیلی بی رحمانه بود", R.raw.brutal))

        val adapter = wordAdapter(this, words)
        val listView = list_phrase
        listView.setAdapter(adapter)

        listView.onItemClickListener = AdapterView.OnItemClickListener { AdapterView, View, position, id ->
            releaseMediaPlayer()
            var word = words.get(position)
            val result = mAudioManager!!.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mediaPlayer = MediaPlayer.create(this, word.audioResourceId)
                mediaPlayer!!.start()
                mediaPlayer!!.setOnCompletionListener(mCompletionListener)
            }
        }
    }

    class word(val englishTranslation: String, val persianTranslation: String, val audioResourceId: Int)

    class wordAdapter(context: Activity, words: java.util.ArrayList<word>)
        : ArrayAdapter<word>(context, 0, words) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var listItemView = convertView
            if (listItemView == null) {
                listItemView = LayoutInflater.from(context).inflate(
                        R.layout.list_item_phrase, parent, false)
            }

            val currentWord = getItem(position)

            val nameTextView = listItemView!!.findViewById(R.id.text_view1) as TextView
            nameTextView.setText(currentWord!!.englishTranslation)

            val numberTextView = listItemView.findViewById(R.id.text_view2) as TextView
            numberTextView.setText(currentWord!!.persianTranslation)

            return listItemView
        }

        companion object {
            private val LOG_TAG = wordAdapter::class.java.simpleName
        }
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
    }

    private fun releaseMediaPlayer() {

        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
            mAudioManager!!.abandonAudioFocus(mOnAudioFocusChangeListener)
        }
    }
}
