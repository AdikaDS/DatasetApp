package com.example.datasetapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datasetapp.R
import com.example.datasetapp.adapter.SelfiePhotoAdapter
import com.example.datasetapp.data.model.SelfiePhoto
import com.example.datasetapp.databinding.FragmentVerifikasiFotoSelfieBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifikasiFotoSelfieFragment : Fragment() {

    private var _binding: FragmentVerifikasiFotoSelfieBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VerifikasiDataViewModel by viewModel()

    private lateinit var recyclerView: RecyclerView
    private lateinit var selfiePhotoAdapter: SelfiePhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerifikasiFotoSelfieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView
        recyclerView = binding.rvSelfie // Ganti dengan ID RecyclerView di layout Anda
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi Adapter
        selfiePhotoAdapter = SelfiePhotoAdapter(mutableListOf()) { position ->
            navigateToCameraFragment(position) // Mengambil foto ulang
        }

        recyclerView.adapter = selfiePhotoAdapter

        // Observe changes in the selfies LiveData
        viewModel.selfies.observe(viewLifecycleOwner) { selfiePhotos ->
            selfiePhotoAdapter.updateSelfies(selfiePhotos) // Memperbarui adapter dengan selfie yang baru
            Log.d("Fragment", "Selfies observed: ${selfiePhotos.size}")
        }
    }

    private fun navigateToCameraFragment(position: Int) {
        // Simpan indeks foto yang akan diambil ulang
        viewModel.currentPhotoIndex = position

        // Pindah ke CameraSelfieFragment
        val cameraSelfieFragment = CameraSelfieFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_camera_selfie, cameraSelfieFragment) // Ganti dengan ID container fragment Anda
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Menghindari memory leaks
    }
}
