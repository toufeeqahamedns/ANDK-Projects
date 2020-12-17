package com.udacity.shoestore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.models.MainActivityViewModel
import com.udacity.shoestore.models.Shoe

class ShoeDetailFragment : Fragment() {

    lateinit var binding: FragmentShoeDetailBinding
    private val viewModel: MainActivityViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_detail, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigate.observe(viewLifecycleOwner, { navigate ->
            if (navigate) {
                view?.findNavController()
                    ?.navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
                viewModel.navigationDone()
            }
        })

        return binding.root
    }
}