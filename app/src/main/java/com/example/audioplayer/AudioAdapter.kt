package com.example.audioplayer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.audio_item.view.*

class AudioAdapter(private val songList: MutableList<Songs> ,
                   private val onItemClicked: (position: Int) -> Unit
                    ) : RecyclerView.Adapter<AudioAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.audio_item,parent,false)
        return AudioAdapter.ViewHolder(view,songList,onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindSongsInfo(songList[position])
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    class ViewHolder(view: View, private val songList: MutableList<Songs>,private val onItemClicked: (position: Int) -> Unit):
            RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }


        fun bindSongsInfo(songItem: Songs) {
            if(songItem.songTitle.isNotEmpty()) {
                itemView.title.text = songItem.songTitle
                itemView.duration.text = convertTime(songItem.songDuration)
                itemView.artist.text = songItem.songDisplayName
            }

        }

        fun convertTime(duration:String) :String {
            var songTime = duration.toInt()
            var mins  = songTime/60
             var sec = songTime%60
            return "$mins : $sec"

        }

        override fun onClick(v: View?) {
            Log.d("dispAudio3","calling onClick within adapter")
            val position = adapterPosition
            onItemClicked(position)

        }


    }
}