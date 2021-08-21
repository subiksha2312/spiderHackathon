package com.example.audioplayer

import android.net.Uri

data class Songs(
    var songId: String,
    var songArtist: String,
    var songTitle: String,
    var songDisplayName: String,
    var songDuration:String,
    var songUri: Uri
    )
