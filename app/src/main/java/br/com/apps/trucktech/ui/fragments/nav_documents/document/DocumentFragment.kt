package br.com.apps.trucktech.ui.fragments.nav_documents.document

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.apps.trucktech.databinding.FragmentDocumentBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar

/**
 * A simple [Fragment] subclass.
 * Use the [DocumentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocumentFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentDocumentBinding? = null
    private val binding get() = _binding!!

    private val args: DocumentFragmentArgs by navArgs()

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
        args.document.run {
            binding.fragmentDocumentImage.loadImageThroughUrl(urlImage)
            binding.fragmentDocumentToolbar.toolbarText.text = name ?: "Documento"
        }
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentDocumentToolbar.toolbar
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}