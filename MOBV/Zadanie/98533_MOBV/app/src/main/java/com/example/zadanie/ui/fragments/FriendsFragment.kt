package com.example.zadanie.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.zadanie.R
import com.example.zadanie.databinding.FragmentBarsBinding
import com.example.zadanie.databinding.FragmentFriendsBinding
import com.example.zadanie.helpers.Injection
import com.example.zadanie.helpers.PreferenceData
import com.example.zadanie.ui.viewmodels.BarsViewModel
import com.example.zadanie.ui.viewmodels.FriendsViewModel


class FriendsFragment : Fragment() {
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var viewmodel: FriendsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel = ViewModelProvider(
            this, Injection.provideViewModelFactory(requireContext())
        ).get(FriendsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swiperefresh.setProgressViewEndTarget(false, 0)

        val x = PreferenceData.getInstance().getUserItem(requireContext())
        if ((x?.uid ?: "").isBlank()) {
            Navigation.findNavController(view).navigate(R.id.action_to_login)
            return
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.back -> {
                    val action = BarsFragmentDirections.actionToBars()
                    view.findNavController().navigate(action)
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewmodel

        }.also { bnd ->
            bnd.addFriend.setOnClickListener {
                val action = FriendsFragmentDirections.actionToAddFriend()
                view.findNavController().navigate(action)
            }
            bnd.deleteFriend.setOnClickListener {
                val action = FriendsFragmentDirections.actionToDeleteFriend()
                view.findNavController().navigate(action)
            }
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

}