package br.com.apps.trucktech.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import br.com.apps.repository.util.RESULT_KEY
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ActivityCameraBinding
import br.com.apps.trucktech.expressions.getByteArray
import br.com.apps.trucktech.expressions.toast
import br.com.apps.trucktech.util.TempFile
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


private const val IMAGE_ORIGIN = "Origem da imagem"

private const val IMAGE_ORIGIN_MESSAGE = "Selecione a origem da imagem"

private const val CAMERA = "Câmera"

private const val GALLERY = "Galeria"

private const val REQUEST_CAMERA_PERMISSION = 1

class CameraActivity : AppCompatActivity() {

    private var _binding: ActivityCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var photoUri: Uri
    private lateinit var currentPhotoPath: String

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) processCameraResult()
            else finish()
        }

    private val galleryActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                checkPermission()
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

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            launchCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                toast("Permissão negada")
                finish()
            }
        }
    }

    private fun launchCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                photoFile?.also {
                    photoUri = FileProvider.getUriForFile(
                        this,
                        "br.com.apps.trucktech",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    cameraActivityResultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    /**
     * Process the result when it came from a camera.
     */
    private fun processCameraResult() {
        try {

            val bitmap = extractBitmapFromUri()
            val byteArray = bitmap.getByteArray()
            setActivityResult(byteArray)

        } catch (e: Exception) {
            Log.e(TAG_DEBUG, "CameraActivity, processCameraResult: ${e.message.toString()}")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath

        }
    }

    private fun extractBitmapFromUri(): Bitmap {
        return contentResolver.openInputStream(photoUri).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }

    private fun setActivityResult(ba: ByteArray) {
        val filePath = TempFile.create(this, ba, RESULT_KEY)

        val intent = Intent()
        intent.putExtra(RESULT_KEY, filePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * Process the result when it came from gallery.
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
