package br.com.apps.trucktech.ui.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import br.com.apps.trucktech.R
import br.com.apps.trucktech.expressions.loadImageThroughUrl

class FullScreenImage(
    private val urlImage: Any?
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.FullScreen)
        val dialogView = layoutInflater.inflate(R.layout.full_screen_image_dialog, null)

        val img = dialogView.findViewById<ImageView>(R.id.dialog_full_screen_image)
        img.loadImageThroughUrl(urlImage)

        dialogView.findViewById<FrameLayout>(R.id.dialog_full_screen_layout_back).setOnClickListener {
            dismiss()
        }

        builder.setView(dialogView)
        return builder.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreen)
    }

}