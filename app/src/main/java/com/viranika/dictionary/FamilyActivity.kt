package com.viranika.dictionary

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_family.*

class FamilyActivity : AppCompatActivity() {

    var words : ArrayList<word> = ArrayList()
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
        setContentView(R.layout.activity_family)

        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        words.add(word("father", "پدر", R.drawable.father, R.raw.father))
        words.add(word("mother", "مادر", R.drawable.mother, R.raw.mother))
        words.add(word("brother", "برادر", R.drawable.brother, R.raw.brother))
        words.add(word("sister", "خواهر", R.drawable.sister, R.raw.sister))
        words.add(word("grandfather", "پدربزرگ", R.drawable.grandfather, R.raw.grandfather))
        words.add(word("grandmother", "مادربزرگ", R.drawable.grandmother, R.raw.grandmother))
        words.add(word("uncle", "عمو", R.drawable.uncle, R.raw.uncle))
        words.add(word("aunt", "خاله", R.drawable.aunt, R.raw.aunt))

        val adapter = wordAdapter(this, words)
        val listView = list_family
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
