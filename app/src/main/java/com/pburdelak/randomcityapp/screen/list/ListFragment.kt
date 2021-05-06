package com.pburdelak.randomcityapp.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.pburdelak.randomcityapp.databinding.FragmentListBinding
import com.pburdelak.randomcityapp.model.CityColorCombination
import com.pburdelak.randomcityapp.screen.base.BaseFragment
import com.pburdelak.randomcityapp.utils.livedata.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment: BaseFragment<FragmentListBinding>() {

    private val viewModel: ListViewModel by activityViewModels()
    private var adapter: ListRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureLayout()
        observeViewModel()
    }

    private fun configureLayout() {
        ListRVAdapter(viewModel::selectItem).let {
            adapter = it
            binding.root.adapter = it
        }
    }

    private fun observeViewModel() {
        viewModel.detailsEvent.observeEvent(viewLifecycleOwner, this::showDetails)
        viewModel.list.observe(viewLifecycleOwner) {
            adapter?.replaceData(it)
        }
    }

    private fun showDetails(item: CityColorCombination) {
        val direction = ListFragmentDirections.actionDetails(item)
        navigator?.navigateTo(direction)
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}