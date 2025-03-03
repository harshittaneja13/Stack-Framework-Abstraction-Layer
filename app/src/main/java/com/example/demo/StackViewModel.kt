package com.example.demo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.api.RetrofitInstance
import com.example.demo.models.StackItem
import kotlinx.coroutines.launch


class StackViewModel : ViewModel() {
    private val _allItems = mutableListOf<StackItem>()
    private val _displayedItems = mutableStateOf<List<StackItem>>(emptyList())
    val displayedItems: State<List<StackItem>> = _displayedItems

    private val _expandedIndex = mutableStateOf<Int?>(null)

    private val _selectedOptions = mutableStateOf<Map<Int, String>>(emptyMap())
    val selectedOptions: State<Map<Int, String>> = _selectedOptions

    var inputValue = mutableStateOf<Int?>(150000)

    private val _loading = mutableStateOf<Boolean?>(false)
    val loading: State<Boolean?> = _loading

    init {
        fetchStackItems()
    }

    private fun fetchStackItems() {
        viewModelScope.launch {
            _loading.value=true
            try {
                val response = RetrofitInstance.api.getStackItems("test_mint")
                _allItems.addAll( response.body()?.items!!)
                _displayedItems.value = _allItems.take(1)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
                // Select the first option by default
                _selectedOptions.value = mapOf(
                    1 to (_allItems.getOrNull(1)?.open_state?.body?.items?.getOrNull(0)?.title ?: ""),
                    2 to (_allItems.getOrNull(2)?.open_state?.body?.items?.getOrNull(0)?.title ?: "")
                )
            }
        }
    }

    fun addNextItem() {
        val nextIndex = _displayedItems.value.size
        if (nextIndex < _allItems.size && nextIndex < 4) {
            _displayedItems.value = _allItems.take(nextIndex + 1)
        }
    }

    fun expandAndRemoveAfter(index: Int) {
        _displayedItems.value = _displayedItems.value.take(index + 1)
        _expandedIndex.value = index
    }

    fun selectOption(itemIndex: Int, option: String) {
        _selectedOptions.value = _selectedOptions.value.toMutableMap().apply {
            put(itemIndex, option)
        }
    }

    fun updateInputValue(newValue: Int) {
        inputValue.value = newValue
    }

    fun getInputValue(): Int? {
        return inputValue.value
    }
}
