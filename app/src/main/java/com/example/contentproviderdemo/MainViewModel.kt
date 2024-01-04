package com.example.contentproviderdemo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.contentproviderdemo.model.MyImage


class MainViewModel : ViewModel() {
    var images by mutableStateOf(emptyList<MyImage>())
        private set

    fun updateImages(images: List<MyImage>){
        this.images = images
    }

    var shouldInvoke by  mutableStateOf(false)
        private set
    fun updateShouldInvoke(shouldInvoke: Boolean){
        this.shouldInvoke = shouldInvoke
    }
}