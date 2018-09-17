package com.viranika.dictionary

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_number.*

class NumberActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_number)

        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        words.add(word("one", "یک", R.drawable.one, R.raw.one))
        words.add(word("two", "دو", R.drawable.two, R.raw.two))
        words.add(word("three", "سه", R.drawable.three, R.raw.three))
        words.add(word("four", "چهار", R.drawable.four, R.raw.four))
        words.add(word("five", "پنج", R.drawable.five, R.raw.five))
        words.add(word("six", "شش", R.drawable.six, R.raw.six))
        words.add(word("seven", "هفت", R.drawable.seven, R.raw.seven))
        words.add(word("eight", "هشت", R.drawable.eight, R.raw.eight))
        words.add(word("nine", "نه", R.drawable.nine,  R.raw.nine))
        words.add(word("ten", "ده", R.drawable.ten, R.raw.ten))

        val adapter = wordAdapter(this, words)
        val listView = list_number
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
