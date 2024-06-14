package br.com.apps.trucktech.ui.fragments.nav_travel.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import br.com.apps.trucktech.databinding.FragmentRecordsBinding
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.ui.KEY_ID
import br.com.apps.trucktech.ui.PAGE_COST
import br.com.apps.trucktech.ui.PAGE_FREIGHT
import br.com.apps.trucktech.ui.PAGE_REFUEL
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_travel.records.private_adapters.RecordsFragmentViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val NUMBER_OF_PAGES = 3
private const val TOOLBAR_TITLE = "Registros"

/**
 * A simple [Fragment] subclass.
 * Use the [RecordsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecordsFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    private val args: RecordsFragmentArgs by navArgs()
    private val viewModel: RecordsViewModel by viewModel()

    private var viewPager2: ViewPager2? = null
    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.recordsFragmentFab.setImageLevel(position)
            viewModel.newPageHasBeenSelected(position)
        }
    }
    private val freightResultListener = FragmentResultListener { requestKey, result ->
        if (requestKey == PAGE_FREIGHT) {
            val freightId = result.getString(KEY_ID)
            requireView().navigateTo(
                RecordsFragmentDirections.actionRecordsFragmentToFreightPreviewFragment(freightId!!, args.travelId)
            )
        }
    }
    private val refuelResultListener = FragmentResultListener { requestKey, result ->
        if (requestKey == PAGE_REFUEL) {
            val refuelId = result.getString(KEY_ID)
            requireView().navigateTo(
                RecordsFragmentDirections.actionRecordsFragmentToRefuelPreviewFragment(refuelId!!, args.travelId)
            )
        }
    }
    private val expendResultListener = FragmentResultListener { requestKey, result ->
        if (requestKey == PAGE_COST) {
            val expendId = result.getString(KEY_ID)
            requireView().navigateTo(
                RecordsFragmentDirections.actionRecordsFragmentToCostPreviewFragment(expendId!!, args.travelId)
            )
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.travelId = args.travelId
        viewModel.masterUid = mainActVM.loggedUser.masterUid
        viewModel.loadData()
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFab()
        initViewPager()
        initTabLayout()
        initFragmentManagerResultListener()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragmentRecordsToolbar.toolbar,
            menuId = null,
            toolbarTextView = binding.fragmentRecordsToolbar.toolbarText,
            title = TOOLBAR_TITLE
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    private fun initFab() {
        binding.recordsFragmentFab.setOnClickListener { view ->
            when (viewModel.viewPagerPosition.value) {

                0 -> {
                    view.navigateTo(RecordsFragmentDirections.actionRecordsFragmentToFreightEditorFragment(null, args.travelId))
                }

                1 -> {
                    view.navigateTo(RecordsFragmentDirections.actionRecordsFragmentToRefuelEditorFragment(null, args.travelId))
                }

                2 -> {
                    view.navigateTo(RecordsFragmentDirections.actionRecordsFragmentToCostEditorFragment(null, args.travelId))
                }

                else -> throw IllegalArgumentException("Position does not represents any valid fragment")
            }
        }
    }

    private fun initViewPager() {
        viewPager2 = binding.recordsFragmentViewPager
        val adapter = RecordsFragmentViewPagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
            NUMBER_OF_PAGES
        )
        viewPager2?.adapter = adapter
        viewPager2?.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private fun initTabLayout() {
        val tabLayout = binding.recordsFragmentTabLayout

        TabLayoutMediator(tabLayout, viewPager2!!) { tab, position ->
            when (position) {
                0 -> tab.text = PAGE_FREIGHT
                1 -> tab.text = PAGE_REFUEL
                2 -> tab.text = PAGE_COST
                else -> throw IllegalArgumentException("Invalid argument for Tab layout position")
            }
        }.attach()
    }

    private fun initFragmentManagerResultListener() {
        childFragmentManager.setFragmentResultListener(
            PAGE_FREIGHT,
            viewLifecycleOwner,
            freightResultListener
        )
        childFragmentManager.setFragmentResultListener(
            PAGE_REFUEL,
            viewLifecycleOwner,
            refuelResultListener
        )
        childFragmentManager.setFragmentResultListener(
            PAGE_COST,
            viewLifecycleOwner,
            expendResultListener
        )
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        viewPager2?.unregisterOnPageChangeCallback(onPageChangeCallback)
        viewPager2 = null
        super.onDestroyView()
    }

}