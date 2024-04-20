package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.private_dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.apps.trucktech.databinding.BottomSheetRequestEditorBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RequestEditorBottomSheet(

    var onClickListener:(position: Int) -> Unit = {},
    var onDismissListener:() -> Unit = {}

) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetRequestEditorBinding? = null
    private val binding get() = _binding!!

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetRequestEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomSheetRequestEditorRefuel.setOnClickListener {
            onClickListener(0)
            dismiss()
        }
        binding.bottomSheetRequestEditorCost.setOnClickListener {
            onClickListener(1)
            dismiss()
        }
        binding.bottomSheetRequestEditorWallet.setOnClickListener {
            onClickListener(2)
            dismiss()
        }

      // Irei definir meu codigo aqui, high order functions, etc...
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onDismissListener()
    }

    companion object {
        const val TAG = "RequestEditorBottomSheet"
    }

}