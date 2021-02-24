package com.babblingbrookdev.azuriteplanner.ui.list

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.babblingbrookdev.azuriteplanner.R
import com.babblingbrookdev.azuriteplanner.databinding.FragmentEntryBinding
import com.babblingbrookdev.azuriteplanner.databinding.FragmentListBinding
import com.babblingbrookdev.azuriteplanner.model.Entry
import com.babblingbrookdev.azuriteplanner.ui.FragmentListener
import com.babblingbrookdev.azuriteplanner.ui.entry.EntryFragment
import dagger.android.support.AndroidSupportInjection
import java.io.Serializable
import java.lang.RuntimeException

import javax.inject.Inject

class ListFragment : Fragment(), ListAdapter.OnClickListener {

    @Inject
    lateinit var viewModel: ListViewModel
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var _listener: FragmentListener? = null
    private val listener get() = _listener!!

    private var entryList: List<Entry> = listOf()

    private lateinit var listAdapter: ListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        if (context is FragmentListener) {
            _listener = context
        } else {
            throw RuntimeException("$context must implement ChartFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener.getActivityFab().setOnClickListener {
            val entryFragment = EntryFragment()
            entryFragment.show(requireActivity().supportFragmentManager, entryFragment.tag)
        }

        listAdapter = ListAdapter(arrayListOf(), this)
        binding.rvEntry.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEntry.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        binding.rvEntry.adapter = listAdapter


        viewModel.getEntries().observe(viewLifecycleOwner, Observer {
            binding.emptyLayout.visibility= if (it.isEmpty()) View.VISIBLE else View.GONE
            listAdapter.replaceData(it)
        })
    }

    override fun onEditClicked(entry: Entry) {
        val bundle = Bundle()
        bundle.putLong("entry", entry.entryId)
        val editFragment = EntryFragment()
        editFragment.arguments = bundle
        editFragment.show(requireActivity().supportFragmentManager, editFragment.tag)
    }

    override fun onDeleteClicked(entry: Entry) {
        viewModel.deleteEntry(entry)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _listener = null
        _binding = null
    }
}