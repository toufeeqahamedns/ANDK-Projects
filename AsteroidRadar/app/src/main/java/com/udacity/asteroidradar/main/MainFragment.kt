package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidFilter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "This can only be activated after onViewCreated"
        }

        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter =
            AsteroidAdapter(AsteroidAdapter.OnClickListener { asteroid ->
                viewModel.onAsteroidClicked(asteroid)
            })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onNavigatedToDetail()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> viewModel.setAsteroidFilter(AsteroidFilter.WEEKLY)
            R.id.show_today_menu -> viewModel.setAsteroidFilter(AsteroidFilter.TODAY)
            else -> viewModel.setAsteroidFilter(AsteroidFilter.ALL)
        }
        return true
    }
}
