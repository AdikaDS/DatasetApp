package com.example.datasetapp.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datasetapp.R
import com.example.datasetapp.data.model.SelfiePhoto

class SelfiePhotoAdapter(
    private var selfiePhotos: MutableList<SelfiePhoto>,
    private val onTakePhotoClick: (Int) -> Unit // Callback untuk tombol ambil foto baru
) : RecyclerView.Adapter<SelfiePhotoAdapter.SelfiePhotoViewHolder>() {

    inner class SelfiePhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSelfieType: TextView = itemView.findViewById(R.id.tv_title_contoh)
        val ivSelfiePhoto: ImageView = itemView.findViewById(R.id.iv_ktp)
        val btnTakeNewPhoto: Button = itemView.findViewById(R.id.btn_take_picture_again)

        fun bind(photo: SelfiePhoto, position: Int) {
            tvSelfieType.text = photo.title
            if (photo.imageUri != null) {
                try {
                    Log.d("Adapter", "Mencoba memuat gambar untuk selfie ${photo.id} dengan URI: ${photo.imageUri}")
                    // Bersihkan gambar sebelumnya
                    ivSelfiePhoto.setImageURI(null)
                    // Muat gambar baru
                    ivSelfiePhoto.setImageURI(photo.imageUri)
                    Log.d("Adapter", "Berhasil memuat gambar untuk selfie ${photo.id}")
                } catch (e: Exception) {
                    Log.e("Adapter", "Error loading image: ${e.message}")
                    ivSelfiePhoto.setImageResource(R.drawable.ic_photo_placeholder)
                }
            } else {
                ivSelfiePhoto.setImageResource(R.drawable.ic_photo_placeholder)
                Log.d("Adapter", "Image URI is null for id: ${photo.id}")
            }
            // Set listener untuk tombol ambil foto baru
            btnTakeNewPhoto.setOnClickListener {
                onTakePhotoClick(position) // Memanggil callback dengan posisi item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfiePhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selfie_preview, parent, false)
        return SelfiePhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelfiePhotoViewHolder, position: Int) {
        holder.bind(selfiePhotos[position], position)
    }

    override fun getItemCount(): Int = selfiePhotos.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelfies(newSelfies: List<SelfiePhoto>) {
        Log.d("Adapter", "Updating selfies. Old size: ${selfiePhotos.size}, New size: ${newSelfies.size}")
        selfiePhotos.clear()
        selfiePhotos.addAll(newSelfies.map { it.copy() })
        notifyDataSetChanged()
        Log.d("Adapter", "After update: ${selfiePhotos.size} items")
    }
}
