package com.example.zadanie.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.zadanie.R
import com.example.zadanie.databinding.FragmentAddFriendBinding
import com.example.zadanie.databinding.FragmentDeleteFriendBinding
import com.example.zadanie.helpers.Injection
import com.example.zadanie.helpers.PreferenceData
import com.example.zadanie.ui.viewmodels.AddFriendViewModel
import com.example.zadanie.ui.viewmodels.DeleteFriendViewModel

// TODO: Rename parameter arguments, choose names that match

class DeleteFriendFragment : Fragment() {

    private lateinit var binding: FragmentDeleteFriendBinding
    private lateinit var deleteFriendViewModel: DeleteFriendViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deleteFriendViewModel = ViewModelProvider(
            this, Injection.provideViewModelFactory(requireContext())
        )[DeleteFriendViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteFriendBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val x = PreferenceData.getInstance().getUserItem(requireContext())
        if ((x?.uid ?: "").isBlank()) {
            view.findNavController().navigate(R.id.action_to_login)
            return
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.back -> {
                    val action = BarsFragmentDirections.actionToFriends()
                    view.findNavController().navigate(action)
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.deleteFriend.setOnClickListener {
            deleteFriend()
        }

        deleteFriendViewModel.isDeleted.observe(viewLifecycleOwner) {
            if (it) {
                view.findNavController().navigateUp()
            }
        }

    }

    private fun deleteFriend() {
        val friendUsername = binding.deleteFriendName.text.toString()

        if (friendUsername.isBlank()) {
            deleteFriendViewModel.show("Fill username")
            return
        }
        deleteFriendViewModel.deleteFriend(friendUsername)
    }
}