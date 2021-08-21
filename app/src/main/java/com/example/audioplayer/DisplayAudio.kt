package com.example.audioplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_display_audio.*
import kotlinx.android.synthetic.main.audio_item.*
import java.text.FieldPosition

var songsInfo = mutableListOf<Songs>()

class DisplayAudio : AppCompatActivity() {

    val STORAGE_REQUEST = 100
    val REQUESTCODE = 101
    var bStorageAccess = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_audio)

        audioList.layoutManager = LinearLayoutManager(this)
        checkStoragePermission()

        if(bStorageAccess) {
            getListOfSongs()
         }
        val onItemClicked =  { position: Int -> onListItemClicked(position)}
        audioList.adapter = AudioAdapter(songsInfo, onItemClicked)


    }

    fun onListItemClicked(position: Int) {
        Log.d("dispAudio1","calling onListItemClicked")
        if(songsInfo[position].songTitle.isNotEmpty()) {
            Log.d("dispAudio2","${songsInfo[position].songUri}")
            val intent =Intent(this, MainActivity::class.java)
            intent.putExtra("songUri","${songsInfo[position].songUri}")
            intent.putExtra("currentPosition","${position}")
            startActivity(intent)
        }
        else {
            Toast.makeText(this,"No song available",Toast.LENGTH_SHORT).show()
        }
    }

    fun getListOfSongs() {
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0" + " AND " +
                        MediaStore.Audio.Media.IS_RINGTONE + "!=1" + " AND " +
                        MediaStore.Audio.Media.IS_ALARM + "!=1" + " AND " +
                        MediaStore.Audio.Media.IS_AUDIOBOOK +"!=1"

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            "_data"
        )

        val cursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.INTERNAL_CONTENT_URI,

            projection,
            selection,
            null,
            null
        )

            Log.d("index4","${cursor!!.count}")

        if (cursor != null && cursor.count > 0) {

            var count =0
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndex("_id")
                val songArtistIndex = cursor.getColumnIndex("artist")
                val songTitleIndex = cursor.getColumnIndex("title")
                val songDispNameIndex = cursor.getColumnIndex("_display_name")
                val songDurationIndex = cursor.getColumnIndex("duration")
                val songUriIndex = cursor.getColumnIndex("_data")
                val songUri = Uri.parse("file:///" + cursor.getString(songUriIndex))
                Log.d("index3","${cursor.getString(songUriIndex)}")

                var objectSongs : Songs = Songs(

                    cursor.getString(idIndex),
                    cursor.getString(songArtistIndex),
                    cursor.getString(songTitleIndex),
                    cursor.getString(songDispNameIndex),
                    cursor.getString(songDurationIndex),
                    songUri

                )
                songsInfo.add(objectSongs)
                //songsInfo[count].songId = cursor.getString(idIndex)
                Log.d("index1","${songsInfo[count].songId}," +
                        "${songsInfo[count].songArtist}," +
                        "${songsInfo[count].songDisplayName}," +
                        "${songsInfo[count].songUri},"+
                        "${songsInfo[count].songTitle},"+
                        "${ songsInfo[count].songDuration}")
                count++
            }


        }

    }

    fun checkStoragePermission(){
        val storagePerm = ContextCompat.checkSelfPermission(this,
                                      Manifest.permission.READ_EXTERNAL_STORAGE)

        if(storagePerm != PackageManager.PERMISSION_GRANTED) {
            makeStoragePermRequest()
        }
        else {
            bStorageAccess = true
        }
    }


    fun makeStoragePermRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),STORAGE_REQUEST)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == STORAGE_REQUEST) {
            bStorageAccess = !(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
        }
    }
}