package com.example.datasetapp.data.repository

import com.example.datasetapp.data.source.network.model.DatasetResponse
import com.example.datasetapp.data.source.network.service.ApiConfig
import com.example.datasetapp.data.source.network.service.DatasetApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import kotlin.concurrent.thread


class DatasetRepository (private val apiService: DatasetApiService) {

    suspend fun uploadImage(file: File) : Response<DatasetResponse> {
        val mediaType = "application/octet-stream".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, file)
        val multipartBodyPart = MultipartBody.Part.createFormData("file", file.name, requestBody)

        // Memanggil API untuk upload dan mengembalikan respons
        return apiService.uploadImage(multipartBodyPart)

    }
}
    /*fun uploadImage(url: MultipartBody.Part) =
        liveData<Resource<DatasetResponse>> {
            emit(Resource.loading(null))
            try {
                val response = contactAPI.uploadImage(url)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.success(it))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UploadError", "Response error: $errorBody")
                    val jsonObj = JSONObject(errorBody ?: "{}")
                    emit(Resource.error(jsonObj.getString("message"), null))
                }
            } catch (e: Exception) {
                Log.e("UploadError", "Exception: ${e.message}")
                emit(Resource.error(e.message, null))
            }
        }*/