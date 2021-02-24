package com.babblingbrookdev.azuriteplanner.ui.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.babblingbrookdev.azuriteplanner.databinding.FragmentEntryBinding
import com.babblingbrookdev.azuriteplanner.model.Entry
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryFragment : BottomSheetDialogFragment() {

    private val viewModel: EntryViewModel by viewModels()
    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!

    private var isEditing = false
    private lateinit var editEntry: Entry

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        // enter edit mode if arguments exist
        bundle?.let {
            viewModel.getEntryById(it.getLong("entry"))
            viewModel.editEntry.observe(viewLifecycleOwner, Observer { entry ->
                editEntry = entry
                isEditing = true
                binding.entryCurrentAzurite.setText(entry.currentAzurite.toString())
                binding.entryAzuriteGoal.setText(entry.azuriteGoal.toString())
                binding.entryBaseProduction.setText(entry.baseProduction.toString())
                binding.entryRcAmount.setText(entry.rcAmount.toString())
            })
        }
        // save or update depending on current mode and dismiss screen
        binding.entrySave.setOnClickListener {
            if (isEditing) {
                viewModel.editEntry(
                    editEntry,
                    binding.entryCurrentAzurite.text.toString().toInt(),
                    binding.entryAzuriteGoal.text.toString().toInt(),
                    binding.entryBaseProduction.text.toString().toDouble(),
                    binding.entryRcAmount.text.toString().toInt()
                )
            } else {
                viewModel.addEntry(
                    binding.entryCurrentAzurite.text.toString().toInt(),
                    binding.entryAzuriteGoal.text.toString().toInt(),
                    binding.entryBaseProduction.text.toString().toDouble(),
                    binding.entryRcAmount.text.toString().toInt()
                )
            }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}