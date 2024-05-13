package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.IdHolder
import br.com.apps.repository.FAILED_TO_LOAD_DATA
import br.com.apps.repository.Response
import br.com.apps.trucktech.databinding.FragmentDocumentsListBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.expressions.snackBarRed
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_documents.documents_list.private_adapters.DocumentsListFragmentAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TOOLBAR_TITLE = "Documentos"

/**
 * A simple [Fragment] subclass.
 * Use the [DocumentsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocumentsListFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentDocumentsListBinding? = null
    private val binding get() = _binding!!

    private val idHolder by lazy {
        IdHolder(
            masterUid = sharedViewModel.userData.value?.user?.masterUid,
            truckId = sharedViewModel.userData.value?.truck?.id
        )
    }
    private val viewModel : DocumentsListFragmentViewModel by viewModel{ parametersOf(idHolder) }
    private var adapter: DocumentsListFragmentAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDocumentsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initStateManager()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentDocumentsListToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentDocumentsListToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(true)
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes documentData for update the recyclerView
     */
    private fun initStateManager() {
        viewModel.documentData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Error -> requireView().snackBarRed(FAILED_TO_LOAD_DATA)
                is Response.Success -> response.data?.let { adapter?.update(it) }
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.freightFragmentRecycler
        adapter = DocumentsListFragmentAdapter(
            requireContext(),
            emptyList(),
            itemCLickListener = { document ->
                requireView().navigateTo(DocumentsListFragmentDirections.actionDocumentsListFragmentToDocumentFragment(document))
            }
        )
        recyclerView.adapter = adapter
        val divider = DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

}