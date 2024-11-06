package com.example.datasetapp.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.example.datasetapp.R
import com.example.datasetapp.databinding.ActivityVerifikasiDataBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.datasetapp.utils.Resource
import com.example.datasetapp.utils.StatusEnum
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class VerifikasiDataActivity : AppCompatActivity() {
    private val binding: ActivityVerifikasiDataBinding by lazy {
        ActivityVerifikasiDataBinding.inflate(layoutInflater)
    }
    private val viewModel: VerifikasiDataViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imageUri: Uri? = intent.getParcelableExtra("image_uri")

        // Menampilkan gambar jika ada
        if (imageUri != null) {
            val imageUriParsed = Uri.parse(imageUri.toString())
            binding.ivKtp.setImageURI(imageUriParsed)
        }

        binding.btnLanjut.setOnClickListener {
            imageUri?.let { uri ->
                val nik = binding.etInputNik.text.toString()
                val nama = binding.etInputName.text.toString()
                uploadKtpKyc(uri, nama, nik)
            }
        }

    }

    private fun uploadKtpKyc(imageUri: Uri, nik: String, nama: String) {
        // Mengubah Uri menjadi File
        val originalFile = imageUri.toFile()

        // Membuat nama file baru
        val newFileName = "KTP_${nama}_$nik.${originalFile.extension}" // Menambahkan ekstensi asli
        val newFile = File(originalFile.parent, newFileName) // Lokasi dan nama file baru

        // Mengganti nama file
        if (originalFile.renameTo(newFile)) {
            Log.d("FileRename", "File renamed to: ${newFile.name}")

            // Memanggil ViewModel untuk meng-upload gambar
            viewModel.uploadKtpKyc(newFile,
                onSuccess = { response ->
                    Log.d("UploadSuccess", "Response: $response")
                    val fragment = CameraSelfieFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.blank_page, fragment)
                        .addToBackStack(null)
                        .commit()
                },
                onError = { error ->
                    Log.e("UploadError", "Error: $error")
                    Toast.makeText(this, "Upload gagal: $error", Toast.LENGTH_LONG).show()
                }
            )
        } else {
            Log.e("FileRename", "Failed to rename file.")
            // Tangani kegagalan mengganti nama file (misalnya, tampilkan pesan kesalahan)
        }
    }


}

