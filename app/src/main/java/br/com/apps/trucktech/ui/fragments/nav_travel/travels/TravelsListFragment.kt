package br.com.apps.trucktech.ui.fragments.nav_travel.travels

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.travel.Travel
import br.com.apps.repository.FAILED_TO_REMOVE
import br.com.apps.repository.Response
import br.com.apps.repository.SUCCESSFULLY_REMOVED
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentTravelsBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.navigateWithSafeArgs
import br.com.apps.trucktech.expressions.snackBarGreen
import br.com.apps.trucktech.expressions.snackBarOrange
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_travel.travels.private_adapters.TravelsListRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.DateRecyclerAdapter
import br.com.apps.usecase.TravelIdsData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.security.InvalidParameterException

private const val TOOLBAR_TITLE = "Viagens"

/**
 * A simple [Fragment] subclass.
 * Use the [TravelsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TravelsListFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentTravelsBinding? = null
    private val binding get() = _binding!!

    private val employeeId by lazy {
        sharedViewModel.userData.value?.user?.employeeId
    }
    private val viewModel: TravelsListViewModel by viewModel { parametersOf(employeeId) }

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
        _binding = FragmentTravelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()

    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            toolbar = binding.fragmentTravelsToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentTravelsToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    /**
     * State manager
     */
    private fun initStateManager() {
        viewModel.travelData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Response.Success -> {
                    response.data?.let { initRecyclerView(it) }
                }
                is Response.Error -> {}
            }
        }
    }

    /**
     * Init recycler view
     */
    private fun initRecyclerView(dataSet: List<Travel>) {
        initConcatAdapter(initAdaptersData(dataSet))
    }

    private fun initConcatAdapter(adapters: List<RecyclerView.Adapter<out RecyclerView.ViewHolder>>) {
        val concatAdapter = ConcatAdapter(adapters)
        val recyclerView = binding.travelsFragmentRecycler
        recyclerView.adapter = concatAdapter
    }

    private fun initAdaptersData(dataSet: List<Travel>): List<RecyclerView.Adapter<out RecyclerView.ViewHolder>> {
        return dataSet
            .sortedBy { it.initialDate }
            .reversed()
            .groupBy {
                it.initialDate?.getMonthAndYearInPtBr() ?: throw InvalidParameterException("Date is null")
            }
            .map { createAdapters(it) }
            .flatten()
    }

    private fun createAdapters(itemsMap: Map.Entry<String, List<Travel>>) = listOf(
        DateRecyclerAdapter(requireContext(), listOf(itemsMap.key)),
        TravelsListRecyclerAdapter(
            requireContext(),
            itemsMap.value,
            itemCLickListener = {
                clearMenu()
                requireView().navigateWithSafeArgs(TravelsListFragmentDirections.actionTravelsFragmentToRecordsFragment(it))
            },
            deleteClickListener = ::showAlertDialog
        )
    )

    private fun showAlertDialog(idsData: TravelIdsData) {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.icon_delete)
            .setTitle("Removendo viagem")
            .setMessage("Você realmente deseja remover esta viagem e todos os seus itens?")
            .setPositiveButton("Ok") { _, _ ->
                lifecycleScope.launch {
                    viewModel.delete(idsData).observe(viewLifecycleOwner) { response ->
                        when(response) {
                            is Response.Success -> {
                                requireView().snackBarOrange(SUCCESSFULLY_REMOVED)
                            }
                            is Response.Error -> {
                                requireView().snackBarGreen(FAILED_TO_REMOVE)
                            }
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .create().apply {
                window?.setGravity(Gravity.CENTER)
                show()
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