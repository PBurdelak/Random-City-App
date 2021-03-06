package com.pburdelak.randomcityapp.screen.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import com.pburdelak.randomcityapp.R
import com.pburdelak.randomcityapp.databinding.FragmentListBinding
import com.pburdelak.randomcityapp.model.CityColorCombination
import com.pburdelak.randomcityapp.model.Error
import com.pburdelak.randomcityapp.screen.activity.CombinationProducerViewModel
import com.pburdelak.randomcityapp.screen.activity.MainActivity
import com.pburdelak.randomcityapp.screen.base.BaseFragment
import com.pburdelak.randomcityapp.screen.details.DetailsViewModel
import com.pburdelak.randomcityapp.utils.livedata.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>() {

    private val viewModel: CombinationProducerViewModel by activityViewModels()
    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private var adapter: ListRVAdapter? = null

    private val mainActivity: MainActivity
        get() = activity as MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        configureLayout()
        observeViewModel()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        } ?: run {
            startPostponedEnterTransition()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_toolbar, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.clearData()
        return true
    }

    private fun configureLayout() {
        ListRVAdapter(viewModel::selectItem).let {
            adapter = it
            binding.root.adapter = it
        }
    }

    private fun observeViewModel() {
        viewModel.errorMessageEvent.observeEvent(viewLifecycleOwner, this::showError)
        viewModel.detailsEvent.observeEvent(viewLifecycleOwner, this::showDetails)
        viewModel.list.observe(viewLifecycleOwner) {
            adapter?.replaceData(it)
        }
    }

    private fun showError(error: Error) =
        Toast.makeText(context, error.messageRes, Toast.LENGTH_LONG).show()

    private fun showDetails(item: CityColorCombination) {
        detailsViewModel.setCurrentItem(item)
        if (!mainActivity.isTabletLandscape) {
            val direction = ListFragmentDirections.actionDetails()
            navigator?.navigateTo(direction)
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}