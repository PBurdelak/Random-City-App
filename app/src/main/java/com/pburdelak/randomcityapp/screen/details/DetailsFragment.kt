package com.pburdelak.randomcityapp.screen.details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.pburdelak.randomcityapp.R
import com.pburdelak.randomcityapp.databinding.FragmentDetailsBinding
import com.pburdelak.randomcityapp.model.CityColorCombination
import com.pburdelak.randomcityapp.screen.base.BaseFragment
import com.pburdelak.randomcityapp.utils.log
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(), OnMapReadyCallback {

    private val viewModel: DetailsViewModel by activityViewModels()

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
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.currentItem.observe(viewLifecycleOwner) {
            configureToolbar(it)
            binding.root.getMapAsync(this)
        }
    }

    private fun configureToolbar(item: CityColorCombination) {
        (activity as? AppCompatActivity)?.supportActionBar?.run {
            title = item.city
            val color = Color.parseColor(item.color)
            setBackgroundDrawable(ColorDrawable(color))
        }
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
        setDefaultToolbarConfiguration()
        binding.root.onDestroy()
        super.onDestroyView()
    }

    private fun setDefaultToolbarConfiguration() {
        (activity as? AppCompatActivity)?.supportActionBar?.run {
            val color = ContextCompat.getColor(requireContext(), R.color.toolbar_background)
            setBackgroundDrawable(ColorDrawable(color))
        }
    }

    override fun onMapReady(map: GoogleMap) {
        val item = viewModel.currentItem.value ?: return
        try {
            val address = Geocoder(requireContext()).getFromLocationName(item.city, 1).first()
            val latLng = LatLng(address.latitude, address.longitude)
            val update = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
            map.animateCamera(update)
        } catch (exception: Exception) {
            exception.log()
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}