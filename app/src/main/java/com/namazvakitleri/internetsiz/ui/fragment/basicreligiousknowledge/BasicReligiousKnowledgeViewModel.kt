package com.namazvakitleri.internetsiz.ui.fragment.basicreligiousknowledge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.namazvakitleri.internetsiz.modal.BasicReligiousKnowledge
import com.namazvakitleri.internetsiz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasicReligiousKnowledgeViewModel @Inject constructor(
    private val basicReligiousKnowledgeRepository: BasicReligiousKnowledgeRepository
): BaseViewModel() {

    var basicReligiousKnowledge = MutableLiveData<ArrayList<BasicReligiousKnowledge>>()

    fun prepareListData() {

        viewModelScope.launch {
            basicReligiousKnowledgeRepository.getKnowledge().collect { knowledge ->

                basicReligiousKnowledge.value = knowledge
            }
        }
    }
}