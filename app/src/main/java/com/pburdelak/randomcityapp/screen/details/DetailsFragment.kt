package com.pburdelak.randomcityapp.screen.details

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.pburdelak.randomcityapp.databinding.FragmentDetailsBinding
import com.pburdelak.randomcityapp.screen.base.BaseFragment
import com.pburdelak.randomcityapp.utils.log
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.*

@AndroidEntryPoint
class DetailsFragment: BaseFragment<FragmentDetailsBinding>(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigator?.navigateUp()
            remove()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.root.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.root.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.root.onLowMemory()
    }

    override fun onResume() {
        super.onResume()
        binding.root.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.root.onStart()
    }

    override fun onPause() {
        binding.root.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.root.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        binding.root.onDestroy()
        super.onDestroyView()
    }

    override fun onMapReady(map: GoogleMap) {
        try {
            val address = Geocoder(requireContext()).getFromLocationName("Poznan", 1)[0]
            val latLng = LatLng(address.latitude, address.longitude)
            val update = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
            map.moveCamera(update)
        } catch (exception: Exception) {
            exception.log()
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}