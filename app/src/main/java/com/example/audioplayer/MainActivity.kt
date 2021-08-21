package com.example.audioplayer

import android.media.AudioAttributes
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var mMediaPlayer: MediaPlayer? = null
    var mCurrPos: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uriString = intent.getStringExtra("songUri")
        val currentPosition = intent.getStringExtra("currentPosition")
        if (currentPosition != null) {
            mCurrPos = currentPosition.toInt()
        }
        Log.d("dispAudioMA","$uriString")

        var songUri: Uri = Uri.parse(uriString)
        if(songUri != null) {
            playSong(songUri)
        }

        var play = findViewById<ImageButton>(R.id.play)
        play.setOnClickListener {
            playMusic()
        }

        var pause = findViewById<ImageButton>(R.id.pause)
        pause.setOnClickListener {
            pauseMusic()
        }

        var playNext = findViewById<ImageButton>(R.id.playnext)
        playNext.setOnClickListener {
            playNextSong()
        }

        var playBefore = findViewById<ImageButton>(R.id.playbefore)
        playBefore.setOnClickListener {
            playPrevSong()
        }

    }

    fun playNextSong() {
        mMediaPlayer!!.stop()
        mCurrPos++
        if(mCurrPos > songsInfo.size) {
            Toast.makeText(this,"No more songs available",Toast.LENGTH_LONG).show()
        }
        else {
            val nextSongUri:Uri = songsInfo[mCurrPos].songUri
            playSong(nextSongUri)
        }
    }

    fun playPrevSong() {
        mMediaPlayer!!.stop()
        mCurrPos--
        if(mCurrPos < 0) {
            Toast.makeText(this,"No more songs available",Toast.LENGTH_LONG).show()
        }
        else {
            val nextSongUri:Uri = songsInfo[mCurrPos].songUri
            playSong(nextSongUri)
        }
    }

    fun playSong(uri: Uri) {
        mMediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, uri)
            setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            )
            prepare()
        }
        mMediaPlayer?.start()
        Toast.makeText(this,"Current song is-${mCurrPos}",Toast.LENGTH_SHORT).show()
    }

    fun playMusic() {
        if(mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(applicationContext,R.raw.pingponghit)
            mMediaPlayer!!.isLooping =  true
            mMediaPlayer!!.start()
        }
        else {
            mMediaPlayer!!.start()
        }
    }

    fun pauseMusic() {
        if(mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
        }
    }
}