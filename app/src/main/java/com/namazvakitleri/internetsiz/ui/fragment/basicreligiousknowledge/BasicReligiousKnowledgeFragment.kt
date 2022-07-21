package com.namazvakitleri.internetsiz.ui.fragment.basicreligiousknowledge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.namazvakitleri.internetsiz.R
import com.namazvakitleri.internetsiz.adapter.ExpandableListAdapter
import com.namazvakitleri.internetsiz.databinding.FragmentBasicReligiousKnowledgeBinding
import com.namazvakitleri.internetsiz.modal.BasicReligiousKnowledge
import com.namazvakitleri.internetsiz.ui.base.BaseFragment
import com.namazvakitleri.internetsiz.utils.extension.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicReligiousKnowledgeFragment : BaseFragment<FragmentBasicReligiousKnowledgeBinding>(){

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBasicReligiousKnowledgeBinding
        = FragmentBasicReligiousKnowledgeBinding::inflate

    override fun observeViewModel() {

        observe(viewModel.basicReligiousKnowledge, ::getBasicReligiousKnowledge)
    }

    private val viewModel: BasicReligiousKnowledgeViewModel by viewModels()

    private lateinit var listAdapter: ExpandableListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.prepareListData()

        binding?.imageButtonBackHome?.setOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun getBasicReligiousKnowledge(knowledge: ArrayList<BasicReligiousKnowledge>) {

        listAdapter = ExpandableListAdapter(requireContext(), knowledge[0].dataHeader, knowledge[0].dataChild)
        binding?.expandableListView?.setAdapter(listAdapter)
    }
}