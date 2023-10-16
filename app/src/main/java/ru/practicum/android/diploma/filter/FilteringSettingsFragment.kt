package ru.practicum.android.diploma.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout.END_ICON_CUSTOM
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilteringSettingsBinding
import ru.practicum.android.diploma.filter.domain.models.FilterParameters


class FilteringSettingsFragment : Fragment() {

    private var _binding : FragmentFilteringSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FilteringSettingsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilteringSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateFilterSettings().observe(viewLifecycleOwner) {
            Log.i("FILTERUODATE", "Обновил настройки фильтра в фрагменте, фильтр $it")
            render(it)
        }

        binding?.salary?.doOnTextChanged { s: CharSequence?, _, _, _ ->
            if (s.isNullOrEmpty()) {
                binding?.amountTextLayout?.endIconMode = END_ICON_NONE
            } else {
                binding?.amountTextLayout?.endIconMode = END_ICON_CUSTOM
                binding?.amountTextLayout?.setEndIconOnClickListener {
                    binding?.salary?.setText("")
                }
            }
        }

        binding.workPlaceText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                findNavController().navigate(R.id.action_filteringSettingsFragment_to_selectWorkplaceFragment)
            }
        }
        binding.industryText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                findNavController().navigate(R.id.action_filteringSettingsFragment_to_selectIndustryFragment)
            }
        }

        binding.settingsFiltrationToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.applyButton.setOnClickListener {
            val salary = if (binding.salary.text.isNullOrBlank()) null else binding.salary.text.toString()
            viewModel.saveSalarySettings(salary, binding.checkbox.isChecked)
            findNavController().popBackStack()
        }

        binding.clearButton.setOnClickListener {
            viewModel.clearFilterSettings()
            findNavController().popBackStack()
        }

        binding.checkbox.setOnClickListener {
            if(binding.checkbox.isChecked) {
                viewModel.saveSalaryFlag(true)
            } else {
                viewModel.saveSalaryFlag(false)
            }
            viewModel.getFilterSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFilterSettings()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun render(filterParameters: FilterParameters?) {
        if (filterParameters == null) {
            binding.btnGroup.visibility = View.GONE
            binding.checkbox.isChecked = false
        } else {
            binding.btnGroup.visibility = View.VISIBLE
            if (filterParameters.area != null) {
                binding.workPlaceText.setText(filterParameters.area?.name)
                binding.workPlace.setEndIconDrawable(R.drawable.ic_close)

                binding.workPlace.setEndIconOnClickListener {
                    viewModel.clearAreaField()
                    binding.workPlaceText.setText("")
                    binding.workPlace.setEndIconDrawable(R.drawable.arrow_forward)
                }
            }
            if (filterParameters.industry != null) {
                binding.industryText.setText(filterParameters.industry?.name)
                binding.industry.setEndIconDrawable(R.drawable.ic_close)

                binding.industry.setEndIconOnClickListener {
                    viewModel.clearIndustryField()
                    binding.industryText.setText("")
                    binding.industry.setEndIconDrawable(R.drawable.arrow_forward)
                }

            }
            if (filterParameters.salary != null) binding.salary.setText(filterParameters.salary?.toString())
            if (filterParameters.onlyWithSalary == true) binding.checkbox.isChecked =
                true else binding.checkbox.isChecked = false
        }
    }

}