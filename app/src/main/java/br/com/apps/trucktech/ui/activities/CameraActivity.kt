package br.com.apps.trucktech.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.com.apps.repository.util.RESULT_KEY
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ActivityCameraBinding
import br.com.apps.trucktech.expressions.getByteArray
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val IMAGE_ORIGIN = "Origem da imagem"

private const val IMAGE_ORIGIN_MESSAGE = "Selecione a origem da imagem"

private const val CAMERA = "Câmera"

private const val GALLERY = "Galeria"

class CameraActivity : AppCompatActivity() {

    private var _binding: ActivityCameraBinding? = null
    private val binding get() = _binding!!

    private val cameraActivityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) processCameraResult(result)
            else finish()
        }

    private val galleryActivityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) processGalleryResult(result)
            else finish()
        }

    //---------------------------------------------------------------------------------------------//
    // ON CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showAlertDialog()
    }

    /**
     * Shows an alert dialog with options to choose the image origin.
     *
     * - Camera
     * - Gallery
     */
    private fun showAlertDialog() {
        var hasNoInteraction = true

        MaterialAlertDialogBuilder(this)
            .setIcon(R.drawable.icon_camera)
            .setTitle(IMAGE_ORIGIN)
            .setMessage(IMAGE_ORIGIN_MESSAGE)
            .setPositiveButton(CAMERA) { _, _ ->
                hasNoInteraction = false
                launchCamera()
            }
            .setNegativeButton(GALLERY) { _, _ ->
                hasNoInteraction = false
                launchGallery()
            }
            .setOnDismissListener { if (hasNoInteraction) finish() }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
            }
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraActivityResultLauncher.launch(intent)
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    /**
     * Process the result of a camera.
     * @param result The activity result containing the data.
     */
    private fun processCameraResult(result: ActivityResult) {
        try {

            val bitmapImage = extractBitmapResultFromCamera(result)
            val byteArray = bitmapImage.getByteArray()
            setActivityResult(byteArray)

        } catch (e: Exception) {
            Log.e(TAG_DEBUG, "CameraActivity, processCameraResult: ${e.message.toString()}")
        }
    }

    private fun extractBitmapResultFromCamera(result: ActivityResult): Bitmap {
        val bitmap =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.extras?.getParcelable("data", Bitmap::class.java)
            } else {
                result.data?.extras?.get("data") as Bitmap
            }

        if (bitmap == null) throw NullPointerException("Bitmap could not be extracted")

        return bitmap
    }

    private fun setActivityResult(ba: ByteArray) {
        val intent = Intent()
        intent.putExtra(RESULT_KEY, ba)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * Process the result of a gallery.
     * @param result The activity result containing the data.
     */
    private fun processGalleryResult(result: ActivityResult) {
        try {

            val bitmap = extractBitmapResultFromGallery(result)
            val byteArray = bitmap.getByteArray()
            setActivityResult(byteArray)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG_DEBUG, "CameraActivity, processGalleryResult: ${e.message.toString()}")
        }
    }

    private fun extractBitmapResultFromGallery(result: ActivityResult): Bitmap {
        val uriImage = result.data!!.data
        return MediaStore.Images.Media.getBitmap(contentResolver, uriImage)
    }

}
