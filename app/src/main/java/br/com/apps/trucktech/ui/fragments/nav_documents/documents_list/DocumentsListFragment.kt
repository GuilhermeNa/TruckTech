package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.IdHolder
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentDocumentsListBinding
import br.com.apps.trucktech.expressions.loadGif
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_documents.documents_list.private_adapters.DocumentsListFragmentAdapter
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
            masterUid = mainActVM.loggedUser.masterUid,
            truckId = mainActVM.loggedUser.truckId
        )
    }
    private val viewModel: DocumentsListFragmentViewModel by viewModel { parametersOf(idHolder) }
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
        initStateManager()
        initRecyclerView()
        initSwipeRefresh()
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

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadData()
        }
    }

    /**
     * Initializes the state manager and observes [viewModel] data.
     *
     *   - Observes documentData for update the recyclerView
     */
    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            data?.let { adapter?.update(it) }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (viewModel.state.value != State.Loading) showAfterLoading()
            if (binding.swipeRefresh.isRefreshing) binding.swipeRefresh.isRefreshing = false

            when (state) {
                is State.Loading -> {
                    binding.apply {
                        boxGif.loadingGif.loadGif(R.drawable.gif_document, requireContext())
                        fragDocumentBoxEmpty.layout.visibility = GONE
                        freightFragmentRecycler.visibility = GONE
                        fragmentDocumentsListToolbar.toolbar.visibility = GONE
                    }
                }

                is State.Loaded -> {
                    binding.apply {
                        freightFragmentRecycler.apply {
                            if (visibility == GONE) {
                                val layoutAnim = AnimationUtils.loadLayoutAnimation(
                                    requireContext(),
                                    R.anim.layout_controller_animation_slide_in_left
                                )
                                lifecycleScope.launch {
                                    delay(500)
                                    visibility = VISIBLE
                                    layoutAnimation = layoutAnim
                                }
                            }
                        }
                        fragDocumentBoxEmpty.apply {
                            layout.visibility = GONE
                            error.visibility = GONE
                            empty.visibility = GONE
                        }
                    }
                }

                is State.Empty -> {
                    binding.apply {
                        freightFragmentRecycler.visibility = GONE
                        lifecycleScope.launch {
                            fragDocumentBoxEmpty.apply {
                                if (layout.visibility == GONE) {
                                    delay(250)
                                    error.visibility = GONE
                                    empty.visibility = VISIBLE
                                    layout.apply {
                                        visibility = VISIBLE
                                        animation = AnimationUtils.loadAnimation(
                                            requireContext(),
                                            R.anim.fade_in_and_shrink
                                        )
                                    }
                                } else {
                                    error.visibility = GONE
                                    empty.visibility = VISIBLE
                                }
                            }
                        }

                    }
                }

                is State.Error -> {
                    binding.apply {
                        freightFragmentRecycler.visibility = GONE

                        lifecycleScope.launch {
                            fragDocumentBoxEmpty.apply {
                                if (layout.visibility == GONE) {
                                    delay(250)
                                    error.visibility = VISIBLE
                                    empty.visibility = GONE
                                    layout.apply {
                                        visibility = VISIBLE
                                        animation = AnimationUtils.loadAnimation(
                                            binding.root.context,
                                            R.anim.fade_in_and_shrink
                                        )
                                    }
                                } else {
                                    error.visibility = VISIBLE
                                    empty.visibility = GONE
                                }
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }

    private fun showAfterLoading() {
        binding.apply {
            boxGif.layout.apply {
                if (this.visibility == VISIBLE) {
                    visibility = GONE
                    animation =
                        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out_and_shrink)
                }
            }
            fragmentDocumentsListToolbar.toolbar.apply {
                if (this.visibility == GONE) {
                    val anim =
                        AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_top)
                    visibility = VISIBLE
                    animation = anim
                }
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.freightFragmentRecycler
        adapter = DocumentsListFragmentAdapter(
            requireContext(),
            emptyList(),
            itemCLickListener = { document ->
                requireView().navigateTo(
                    DocumentsListFragmentDirections.actionDocumentsListFragmentToDocumentFragment(
                        document
                    )
                )
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
        binding.swipeRefresh.isRefreshing = false
        _binding = null
        adapter = null
    }

}