package com.example.datasetapp.view

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datasetapp.data.model.SelfiePhoto
import com.example.datasetapp.data.repository.DatasetRepository
import com.example.datasetapp.data.source.network.model.DatasetResponse
import com.example.datasetapp.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File

class VerifikasiDataViewModel(private val repository : DatasetRepository) : ViewModel() {
    var nik: String? = null
    var name: String? = null
    var currentPhotoIndex: Int = 0

    // Menyimpan daftar selfie yang diambil
    private val _selfies = MutableLiveData<MutableList<SelfiePhoto>>()
    val selfies: LiveData<MutableList<SelfiePhoto>> = _selfies

    private val defaultSelfies = listOf(
        SelfiePhoto(1, null, "Verifikasi Selfie dengan Masker"),
        SelfiePhoto(2, null, "Verifikasi Selfie dengan Topi"),
        SelfiePhoto(3, null, "Verifikasi Selfie dengan Kacamata"),
        SelfiePhoto(4, null, "Verifikasi Selfie Tanpa Atribut Kepala"),
        SelfiePhoto(5, null, "Verifikasi Selfie Memejamkan Mata")
    )

    init {
        // Inisialisasi data saat ViewModel dibuat
        _selfies.value = defaultSelfies.toMutableList()
        Log.d("ViewModel", "ViewModel initialized with ${_selfies.value?.size} selfies")
    }

    fun addSelfie(photo: SelfiePhoto) {
        val currentList = _selfies.value ?: mutableListOf()

        val index = currentList.indexOfFirst { it.id == photo.id }
        if (index != -1) {
            currentList[index] = photo.copy()
            Log.d(TAG, "Updated existing selfie at index $index with uri : ${photo.imageUri}")
        } else {
            currentList.add(photo.copy())
            Log.d(TAG, "Added new selfie with URI: ${photo.imageUri}")
        }

        _selfies.postValue(currentList)
        Log.d(TAG, "Current selfies size: ${currentList.size}")
    }


    fun uploadKtpKyc(file: File, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Memanggil metode upload dari repository
                val response = repository.uploadImage(file)
                if (response.isSuccessful) {
                    onSuccess(response.body()?.toString() ?: "Upload successful")
                } else {
                    onError("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Exception: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "VerifikasiDataViewModel"
    }
}