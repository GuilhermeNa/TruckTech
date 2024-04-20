package br.com.apps.trucktech.ui.fragments.nav_documents.document

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import br.com.apps.model.model.Document
import br.com.apps.repository.Response
import br.com.apps.trucktech.databinding.FragmentDocumentBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import coil.load
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 * Use the [DocumentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocumentFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentDocumentBinding? = null
    private val binding get() = _binding!!

    private val args: DocumentFragmentArgs by navArgs()
    private val viewModel: DocumentViewModel by viewModel { parametersOf(args.documentId) }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()

        val instance = FirebaseStorage.getInstance()
        lifecycleScope.launch {
            val result = instance.reference.listAll().await()
            result.items.forEach {
                val url = it.downloadUrl.await()
                url.path
                Log.d("teste", "onViewCreated: $url")
                binding.fragmentDocumentImage.load(url)
            }
        }

        val teste = instance.reference.child("teste/123.jpg")
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentDocumentToolbar.toolbar
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    fun getImage(ref: StorageReference) {
        /*  //val image = binding.fragmentRequestEditorPayment.panelAddPixImage
         // val bitmap = image.drawable.toBitmap()
          val baos = ByteArrayOutputStream()
          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
          val data = baos.toByteArray()

          ref.putBytes(data)
              .addOnSuccessListener {

              }.addOnFailureListener {

              }*/
    }

    private fun initStateManager() {
        viewModel.document.observe(viewLifecycleOwner) {
            it?.let { response ->
                when(response) {
                    is Response.Success -> bind(response.data)
                    is Response.Error -> requireView().snackBarRed("Document not found")
                }
            }
        }
    }

    private fun bind(data: Document?) {
        data?.let { document ->
            binding.fragmentDocumentImage.loadImageThroughUrl(document.urlImage, requireContext())
            binding.fragmentDocumentToolbar.toolbarText.text = "Nome do documento"
        }
    }


    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}