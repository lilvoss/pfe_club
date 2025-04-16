package tn.esprit.module.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.esprit.module.repository.TarifRepository

class TarifViewModel(private val tarifRepository: TarifRepository) : ViewModel() {
    private val _tarifId = MutableLiveData<Int?>()
    val tarifId: LiveData<Int?> get() = _tarifId

    fun fetchTarifId() {
        tarifRepository.getTarifId { tarifId, error ->
            if (tarifId != null) {
                _tarifId.postValue(tarifId)
            } else {
                _tarifId.postValue(null)
            }
        }
    }
}
