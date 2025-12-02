package com.example.donshy.ViewModel.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donshy.data.model.Word
import com.example.donshy.data.repository.HomeRepositoryImpl
import com.example.donshy.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepositoryImpl): ViewModel() {
    private val _getWordsState = MutableLiveData<Result<List<Word>>>()
    val getWordState get() = _getWordsState
    private val _addWordState = MutableLiveData<Result<Boolean>>()
    val addWordState = _addWordState

    fun getWords() {
        _getWordsState.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getWord()
            _getWordsState.value = result
        }
    }

    fun addWords(word: String, meaning: String) {
        _addWordState.value = Result.Loading
        val word = Word(word, meaning)
        viewModelScope.launch {
            val result = repository.addWord(word)
            _addWordState.value = result
        }
    }
}