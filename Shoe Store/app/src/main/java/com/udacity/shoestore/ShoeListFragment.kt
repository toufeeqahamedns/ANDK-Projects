package com.udacity.shoestore

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.databinding.ShoeItemBinding
import com.udacity.shoestore.models.MainActivityViewModel

class ShoeListFragment : Fragment() {

    lateinit var binding: FragmentShoeListBinding

    private val viewModel: MainActivityViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_list, container, false)

        setHasOptionsMenu(true)

        binding.floatingActionButton.setOnClickListener {
            view?.findNavController()
                ?.navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailFragment())
        }

        val parentLayout = binding.llInScrollview
        val layoutInflater = layoutInflater
        var shoeBinding: ShoeItemBinding

        viewModel.shoes.observe(viewLifecycleOwner, { shoes ->
            for (shoe in shoes) {
                shoeBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.shoe_item, parentLayout, false)

                shoeBinding.shoe = shoe

                parentLayout.addView(shoeBinding.root)
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.logout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}