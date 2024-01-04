package com.example.contentproviderdemo

import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import coil.compose.AsyncImage
import com.example.contentproviderdemo.model.MyImage
import com.example.contentproviderdemo.ui.theme.ContentProviderDemoTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.Calendar

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
            viewModel.updateShouldInvoke(true)
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 0)
            }else{
                viewModel.updateShouldInvoke(true)
            }
        }

        if (viewModel.shouldInvoke){
            val millisAfter = Calendar.getInstance().apply {
                this.add(Calendar.DAY_OF_YEAR, -1)
            }.timeInMillis
            val projection = arrayOf( MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DISPLAY_NAME )
            val selection = "${MediaStore.Images.Media.DATE_TAKEN} > ?"
            val selectionArgs = arrayOf(millisAfter.toString())
            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val images = mutableListOf<MyImage>()
                val idColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                val nameColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                while (cursor.moveToNext()){
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    images.add(MyImage(id, name, uri))
                }
                viewModel.updateImages(images)
            }
        }


        setContent {
            ContentProviderDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                        items(viewModel.images.size, key = { i -> viewModel.images[i].id}){ index ->
                            AsyncImage(model = viewModel.images[index].uri, contentDescription = "My Image", modifier = Modifier.fillMaxWidth())
                        }
                    })
                }
            }
        }
    }
}

