package com.example.zadanie.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.zadanie.R
import com.example.zadanie.databinding.FragmentBarsBinding
import com.example.zadanie.helpers.Injection
import com.example.zadanie.helpers.PreferenceData
import com.example.zadanie.ui.viewmodels.BarsViewModel
import com.example.zadanie.ui.viewmodels.Sort
import com.example.zadanie.ui.viewmodels.data.MyLocation
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class BarsFragment : Fragment() {
    private lateinit var binding: FragmentBarsBinding
    private lateinit var viewmodel: BarsViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_to_locate)
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                viewmodel.show("Only approximate location access granted.")
                // Only approximate location access granted.
            }
            else -> {
                viewmodel.show("Location access denied.")
                // No location access granted.
            }
        }
    }

    private val locationPermissionRequestSortASC = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                sortByDistance(Sort.DIST_ASC)
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                viewmodel.show("Only approximate location access granted.")
                // Only approximate location access granted.
            }
            else -> {
                viewmodel.show("Location access denied.")
                // No location access granted.
            }
        }
    }

    private val locationPermissionRequestSortDESC = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                sortByDistance(Sort.DIST_DESC)
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                viewmodel.show("Only approximate location access granted.")
                // Only approximate location access granted.
            }
            else -> {
                viewmodel.show("Location access denied.")
                // No location access granted.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel = ViewModelProvider(
            this, Injection.provideViewModelFactory(requireContext())
        ).get(BarsViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBarsBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swiperefresh.setProgressViewEndTarget(false, 0)

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.findBar -> {
                    val action = BarsFragmentDirections.actionToLocate()
                    view.findNavController().navigate(action)
                    true
                }
                R.id.bars -> {
                    val action = BarsFragmentDirections.actionToBars()
                    view.findNavController().navigate(action)
                    true
                }
                R.id.friends -> {
                    val action = BarsFragmentDirections.actionToFriends()
                    view.findNavController().navigate(action)
                    true
                }
                else -> {
                    false
                }
            }
        }


        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    PreferenceData.getInstance().clearData(requireContext())
                    view.findNavController().navigate(R.id.action_to_login)
                    true
                }
                R.id.usersASC -> {
                    viewmodel.sortList(Sort.USERS_ASC)
                    true
                }
                R.id.usersDESC -> {
                    viewmodel.sortList(Sort.USERS_DESC)
                    true
                }
                R.id.nameASC -> {
                    viewmodel.sortList(Sort.NAME_ASC)
                    true
                }
                R.id.nameDESC -> {
                    viewmodel.sortList(Sort.NAME_DESC)
                    true
                }
                R.id.distASC -> {
                    if (checkPermissions()) {
                        sortByDistance(Sort.DIST_ASC)
                    } else {
                        locationPermissionRequestSortASC.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                        viewmodel.show("No access for location")
                    }
                    true
                }
                R.id.distDESC -> {
                    if (checkPermissions()) {
                        sortByDistance(Sort.DIST_DESC)
                    } else {
                        locationPermissionRequestSortDESC.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                        viewmodel.show("No access for location")
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }


        val x = PreferenceData.getInstance().getUserItem(requireContext())
        if ((x?.uid ?: "").isBlank()) {
            Navigation.findNavController(view).navigate(R.id.action_to_login)
            return
        }



        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewmodel
        }.also { bnd ->

            bnd.swiperefresh.setOnRefreshListener {
                viewmodel.refreshData()
            }

        }

        viewmodel.loading.observe(viewLifecycleOwner) {
            binding.swiperefresh.isRefreshing = it
        }

        viewmodel.message.observe(viewLifecycleOwner) {
            if (PreferenceData.getInstance().getUserItem(requireContext()) == null) {
                Navigation.findNavController(requireView()).navigate(R.id.action_to_login)
            }
        }

    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun sortByDistance(sort: Sort) {
        if (checkPermissions()) {
            viewmodel.loading.postValue(true)
            fusedLocationClient.getCurrentLocation(
                CurrentLocationRequest.Builder().setDurationMillis(30000)
                    .setMaxUpdateAgeMillis(60000).build(), null
            ).addOnSuccessListener {
                it?.let {
                    viewmodel.myLocation.postValue(MyLocation(it.latitude, it.longitude))
                    viewmodel.sortList(sort)
                } ?: viewmodel.loading.postValue(false)
            }
        }
    }


}