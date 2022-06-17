package com.exsample.catapp.fragment

import android.Manifest
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.exsample.catapp.R
import com.exsample.catapp.adapter.CatListAdapter
import com.exsample.catapp.util.OnClickEvent
import com.exsample.catapp.databinding.FragmentCatBinding
import com.exsample.catapp.models.CatResponse
import com.exsample.catapp.util.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * In this fragment, all data will be printed
 */
@AndroidEntryPoint
class CatFragment : Fragment(R.layout.fragment_cat) {

    private val viewModel by viewModels<CatFragmentViewModel>()
    lateinit var binding: FragmentCatBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCatBinding.bind(view)
        initViews()
    }

    /**
     * in this function, we call the data
     */
    private fun initViews() {
        viewModel.getCatList()
        setupObservers()
    }

    /**
     * in this function, we receive the data
     */
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.catList.collect {
                when (it) {
                    UiStateObject.LOADING -> {
                        //show progress
                    }

                    is UiStateObject.SUCCESS -> {
                        Log.d("TAG", "setupObservers: ${it.data}")
                        refreshAdapter(it.data)
                    }
                    is UiStateObject.ERROR -> {
                        Log.d("TAG", "setupObservers: ${it.message}")
                    }
                    else -> {
                    }
                }
            }
        }
    }

    /**
     * in this function, we upload the data to recyclerView
     */
    private fun refreshAdapter(data: CatResponse) {
        val adapter = CatListAdapter(data, object : OnClickEvent {
            override fun setOnButtomClick(imageView: ImageView) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

                photoDownload(imageView)
            }
        })
        binding.recyclerView.adapter = adapter
    }

    /**
     * in this function, we upload the image from imageView
     */
    fun photoDownload(imageView: ImageView){
        val bitmap = getScreenShotFromView(imageView)
        if (bitmap != null) {
            saveMediaToStorage(bitmap)
        }
    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {

        }
        return screenshot
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"

        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requireActivity().contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {

                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!

                fos = imageUri.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(requireContext() , "Rasm Galereyaga saqlandi" , Toast.LENGTH_SHORT).show()
        }
    }


}