package com.viranika.dictionary

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_color.*

class ColorActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_color)

        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        words.add(word("white", "سفید", R.drawable.white, R.raw.white))
        words.add(word("yellow", "زرد", R.drawable.yellow, R.raw.yellow))
        words.add(word("orange", "نارنجی", R.drawable.orange, R.raw.orange))
        words.add(word("red", "قرمز", R.drawable.red, R.raw.red))
        words.add(word("pink", "صورتی", R.drawable.pink, R.raw.pink))
        words.add(word("purple", "بنفش", R.drawable.purple, R.raw.purple))
        words.add(word("blue", "آبی", R.drawable.blue, R.raw.blue))
        words.add(word("green", "سبز", R.drawable.green, R.raw.green))
        words.add(word("gray", "خاکستری", R.drawable.gray, R.raw.gray))
        words.add(word("black", "سیاه", R.drawable.black, R.raw.black))

        val adapter = wordAdapter(this, words)
        val listView = list_color
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
