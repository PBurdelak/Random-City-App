package com.pburdelak.randomcityapp.screen.details;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.pburdelak.randomcityapp.R;
import com.pburdelak.randomcityapp.databinding.FragmentDetailsBinding;
import com.pburdelak.randomcityapp.model.CityColorCombination;
import com.pburdelak.randomcityapp.screen.base.BaseFragment;
import com.pburdelak.randomcityapp.screen.base.BaseNavigator;

import timber.log.Timber;

public class DetailsFragment extends BaseFragment<FragmentDetailsBinding> implements OnMapReadyCallback {

    private DetailsViewModel viewModel;

    private final ViewTreeObserver.OnPreDrawListener listener = () -> {
        startPostponedEnterTransition();
        return true;
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                BaseNavigator navigator = getNavigator();
                if (navigator != null) {
                    navigator.navigateUp();
                }
                remove();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setBinding(FragmentDetailsBinding.inflate(inflater, container, false));
        getBinding().getRoot().onCreate(savedInstanceState);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postponeEnterTransition();
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        final Observer<CityColorCombination> observer = item -> getBinding().getRoot().getMapAsync(this);
        viewModel.getCurrentItem().observe(getViewLifecycleOwner(), observer);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        startPostponedEnterTrans();
        CityColorCombination item = viewModel.getCurrentItem().getValue();
        if (item == null) return;
        configureToolbar(item);
        try {
            Address address = new Geocoder(requireContext()).getFromLocationName(item.getCity(), 1).get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 10f);
            googleMap.animateCamera(update);
        } catch (Exception exception) {
            Timber.e(exception);
            Toast.makeText(requireContext(), exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void startPostponedEnterTrans() {
        getBinding().getRoot().getViewTreeObserver().addOnPreDrawListener(listener);
    }

    private void configureToolbar(CityColorCombination item) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity == null) return;
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(item.getCity());
            int color = Color.parseColor(item.getColor());
            ColorDrawable drawable = new ColorDrawable(color);
            actionBar.setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getBinding().getRoot().onSaveInstanceState(outState);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        getBinding().getRoot().onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBinding().getRoot().onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        getBinding().getRoot().onStart();
    }

    @Override
    public void onPause() {
        getBinding().getRoot().onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        getBinding().getRoot().onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        getBinding().getRoot().getViewTreeObserver().removeOnPreDrawListener(listener);
        setDefaultToolbarConfiguration();
        getBinding().getRoot().onDestroy();
        super.onDestroyView();
    }

    private void setDefaultToolbarConfiguration() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity == null) return;
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            int color = ContextCompat.getColor(requireContext(), R.color.toolbar_background);
            ColorDrawable drawable = new ColorDrawable(color);
            actionBar.setBackgroundDrawable(drawable);
        }
    }
}