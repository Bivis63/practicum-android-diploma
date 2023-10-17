package ru.practicum.android.diploma.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.MainNavGraphDirections
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.utils.ErrorType
import ru.practicum.android.diploma.core.utils.adapter.VacancyAdapter
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.ui.state.SearchScreenState
import ru.practicum.android.diploma.search.ui.viewmodel.SearchViewModel


class SearchFragment : Fragment() {
    private var binding: FragmentSearchBinding? = null
    private val viewModel by viewModel<SearchViewModel>()
    private val adapter = VacancyAdapter(
        onClick = { onVacancyClick(it.id) },
        onLongClick = { true }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInput()

        initAdapter()

        viewModel.uiState.observe(viewLifecycleOwner) {
            render(it)
        }

        binding?.filterIcon?.setOnClickListener {
            navToFilter()
        }

        binding?.searchRecycler?.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos =
                        (binding!!.searchRecycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemCount = adapter.itemCount
                    if (pos >= itemCount - 1) {
                        viewModel.loadNextPage()

                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFilterSettings()
        viewModel.cancelDebounce = false
    }

    private fun initAdapter() {
        binding?.searchRecycler?.adapter = adapter
    }

    private fun initInput() {
        binding?.inputSearchForm?.setOnClickListener {
            binding?.inputSearchForm?.isCursorVisible = true
        }
        binding?.inputSearchForm?.doOnTextChanged { s: CharSequence?, _, _, _ ->
            if (s.isNullOrEmpty()) {
                binding?.editTextImage?.setImageResource(R.drawable.ic_search)
                viewModel.vacanciesList = mutableListOf()
            } else {
                binding?.editTextImage?.setImageResource(R.drawable.ic_close)
            }

            if (binding?.inputSearchForm?.hasFocus() == true && s.toString().isNotEmpty()) {

                showDefault()
            }

            viewModel.searchDebounce(binding?.inputSearchForm?.text.toString())
        }

        binding?.inputSearchForm?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search(binding?.inputSearchForm?.text.toString())
            }
            false
        }

        binding?.inputSearchForm?.requestFocus()

        binding?.editTextImage?.setOnClickListener {
            viewModel.vacanciesList = mutableListOf()
            clearSearch()
        }
    }

    private fun clearSearch() {
        binding?.inputSearchForm?.setText("")
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        showDefault()
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Success -> showVacancies(state.vacancies, state.found)
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.NothingFound -> showVacancies(state.vacancies, state.found)
            is SearchScreenState.Default -> showDefault()
            is SearchScreenState.Error -> showError(state.type)
        }
    }

    private fun showVacancies(vacancies: List<Vacancy>, found: Int) {
        adapter.setVacancies(viewModel.vacanciesList)
        binding?.placeholderImage?.isVisible = false
        binding?.progressBarForLoad?.isVisible = false
        binding?.placeholderNoInternet?.isVisible = false
        binding?.textFabSearch?.isVisible = true
        binding?.placeholderServerError?.isVisible = false

        if (vacancies.isEmpty()) {
            binding?.textFabSearch?.text = resources.getString(R.string.no_vacancies)
            binding?.placeholderError?.isVisible = true
            binding?.searchRecycler?.isVisible = false
        } else {
            binding?.textFabSearch?.text =
                resources.getQuantityString(R.plurals.vacancies, found, found)
            binding?.searchRecycler?.isVisible = true
            binding?.placeholderError?.isVisible = false
        }
    }

    private fun showError(type: ErrorType) {
        binding?.placeholderImage?.isVisible = false
        binding?.placeholderError?.isVisible = false
        binding?.searchRecycler?.isVisible = false
        binding?.progressBarForLoad?.isVisible = false
        binding?.textFabSearch?.isVisible = false
        when (type) {
            ErrorType.NO_CONNECTION -> binding?.placeholderNoInternet?.isVisible = true
            else -> binding?.placeholderServerError?.isVisible = true
        }
    }

    private fun onVacancyClick(id: String) {
        if (!viewModel.isClickable) return
        viewModel.onVacancyClick()
        findNavController().navigate(MainNavGraphDirections.actionToVacancyDetailsFragment(id))
    }

    private fun showLoading() {
        binding?.placeholderImage?.isVisible = false
        binding?.placeholderError?.isVisible = false
        binding?.searchRecycler?.isVisible = false
        binding?.progressBarForLoad?.isVisible = true
        binding?.textFabSearch?.isVisible = false
        binding?.placeholderNoInternet?.isVisible = false
        binding?.placeholderServerError?.isVisible = false
    }

    private fun showDefault() {
        binding?.searchRecycler?.isVisible = false
        binding?.progressBarForLoad?.isVisible = false
        binding?.placeholderImage?.isVisible = true
        binding?.textFabSearch?.isVisible = false
        binding?.placeholderError?.isVisible = false
        binding?.placeholderNoInternet?.isVisible = false
        binding?.placeholderServerError?.isVisible = false
    }

    private fun navToFilter() {
        findNavController().navigate(R.id.action_searchFragment_to_filteringSettingsFragment)
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelDebounce = true
    }


    /* Логика отображения активной/неактивной фильтрации

    private fun showEmptyFilterIcon() {
        binding?.filterIcon?.setImageResource(R.drawable.ic_filter)
        //TODO
    }

    private fun showNoEmptyFilterIcon() {
        binding?.filterIcon?.setImageResource(R.drawable.filter_on)
        //TODO
    }

     */
}