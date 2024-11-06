package com.example.datasetapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datasetapp.R
import com.example.datasetapp.adapter.SelfiePhotoAdapter
import com.example.datasetapp.data.model.SelfiePhoto
import com.example.datasetapp.databinding.ActivityVerifikasiFotoSelfieBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifikasiFotoSelfieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifikasiFotoSelfieBinding
    private val viewModel: VerifikasiDataViewModel by viewModel()

    private lateinit var recyclerView: RecyclerView
    private lateinit var selfiePhotoAdapter: SelfiePhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifikasiFotoSelfieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeSelfies()
    }


    private fun setupRecyclerView() {
        recyclerView = binding.rvSelfie
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)  // Add this for better performance

        selfiePhotoAdapter = SelfiePhotoAdapter(mutableListOf()) { position ->
            navigateToCameraActivity(position)
        }

        recyclerView.adapter = selfiePhotoAdapter
    }

    private fun observeSelfies() {
        viewModel.selfies.observe(this) { selfieList ->
            Log.d("Activity", "Received selfies update: ${selfieList.size} items")
            selfieList.forEach { selfie ->
                Log.d("Activity", "Selfie: id=${selfie.id}, imageUri=${(selfie.imageUri).toString()}, title=${selfie.title}")
            }
            selfiePhotoAdapter.updateSelfies(selfieList)
        }
    }

    private fun navigateToCameraActivity(position: Int) {
        viewModel.currentPhotoIndex = position

        val cameraSelfieFragment = CameraSelfieFragment()
        this.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_camera_selfie, cameraSelfieFragment) // Ganti dengan ID container fragment Anda
            .addToBackStack(null)
            .commit()
    }
}
