package br.com.apps.trucktech.ui.fragments.nav_home.home.frag_performance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import br.com.apps.trucktech.databinding.FragmentPerformanceBinding
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.private_adapters.HomeFragmentPerformanceViewPagerAdapter
import br.com.apps.trucktech.ui.fragments.nav_home.home.private_adapters.PeriodRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.HeaderDefaultHorizontalAdapter
import br.com.apps.trucktech.util.state.State
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PerformanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerformanceFragment : Fragment() {

    private var _binding: FragmentPerformanceBinding? = null
    private val binding get() = _binding!!

    private val parentViewModel by viewModels<HomeViewModel>({ requireParentFragment() })
    private val viewModel: PerformanceViewModel by viewModel()

    private var viewPagerAdapter: HomeFragmentPerformanceViewPagerAdapter? = null
    private var viewPager2: ViewPager2? = null
    private var indicator: CircleIndicator3? = null
    private var periodAdapter: PeriodRecyclerAdapter? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerformanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewModel()
        initStateManager()
        initHeaderRecycler()
        initPeriodRecycler()
        initViewPager()
        initPagerTransformer()
        initCircleIndicator()
    }

    private fun initializeViewModel() {
        lifecycleScope.launch {
            parentViewModel.data.asFlow().collect { data ->
                data?.travels?.let {
                    viewModel.initialize(it, data.averageAim, data.performanceAim)
                } ?: viewModel.setState(State.Error(NullPointerException()))
            }
        }
    }

    private fun initStateManager() {
        viewModel.data.observe(viewLifecycleOwner) {
            periodAdapter?.update(viewModel.getPeriodData())
            viewPagerAdapter?.update(viewModel.getPagerData())
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {}

                is State.Loaded -> {
                    binding.apply {
                        panelPerformanceHeaderRecycler.visibility = View.VISIBLE
                        panelPerformancePeriodRecycler.visibility = View.VISIBLE
                        panelPerformanceViewPager2.visibility = View.VISIBLE
                        panelPerformanceCircleIndicator.visibility = View.VISIBLE
                        layoutError.visibility = View.GONE
                        empty.visibility = View.GONE
                        error.visibility = View.GONE
                    }
                }

                is State.Empty -> {
                    binding.apply {
                        panelPerformanceHeaderRecycler.visibility = View.GONE
                        panelPerformancePeriodRecycler.visibility = View.GONE
                        panelPerformanceViewPager2.visibility = View.GONE
                        panelPerformanceCircleIndicator.visibility = View.GONE
                        layoutError.visibility = View.VISIBLE
                        empty.visibility = View.VISIBLE
                        error.visibility = View.GONE
                    }
                }

                is State.Error -> {
                    binding.apply {
                        panelPerformanceHeaderRecycler.visibility = View.GONE
                        panelPerformancePeriodRecycler.visibility = View.GONE
                        panelPerformanceViewPager2.visibility = View.GONE
                        panelPerformanceCircleIndicator.visibility = View.GONE
                        layoutError.visibility = View.VISIBLE
                        empty.visibility = View.GONE
                        error.visibility = View.VISIBLE
                    }
                }

                else -> {}
            }
        }

    }

    private fun initHeaderRecycler() {
        val recyclerView = binding.panelPerformanceHeaderRecycler
        val adapter = HeaderDefaultHorizontalAdapter(
            context = requireContext(),
            dataSet = viewModel.headerItemsMap,
            adapterPos = viewModel.headerPos,
            itemClickListener = { headerTxt ->
                viewModel.newHeaderSelected(headerTxt)
                periodAdapter?.run {
                    if (selectedPos != 0) resetSelector()
                }
                viewPager2?.run {
                    if (currentItem != 0) setCurrentItem(0, true)
                }
            }
        )
        recyclerView.adapter = adapter
    }

    private fun initPeriodRecycler() {
        val recyclerView = binding.panelPerformancePeriodRecycler
        periodAdapter = PeriodRecyclerAdapter(
            context = requireContext(),
            adapterPos = viewModel.periodPos,
            itemClickListener = { adapterPos ->
                viewModel.newPeriodSelected(adapterPos)
                viewPagerAdapter?.update(viewModel.getPagerData())
            }
        )
        recyclerView.adapter = periodAdapter
    }

    private fun initViewPager() {
        viewPager2 = binding.panelPerformanceViewPager2
        viewPagerAdapter = HomeFragmentPerformanceViewPagerAdapter(requireContext())
        viewPager2?.adapter = viewPagerAdapter
        viewPager2?.offscreenPageLimit = 3
        viewPager2?.clipToPadding = false
        viewPager2?.clipChildren = false
        viewPager2?.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun initPagerTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1f - kotlin.math.abs(position)
            page.scaleY = 0.75f + (r * 0.25f)
        }
        viewPager2?.setPageTransformer(transformer)
    }

    private fun initCircleIndicator() {
        indicator = binding.panelPerformanceCircleIndicator
        indicator?.setViewPager(viewPager2)
        indicator?.adapterDataObserver?.let { viewPagerAdapter?.registerAdapterDataObserver(it) }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        periodAdapter = null
        viewPager2?.adapter = null
        viewPagerAdapter = null
        viewPager2 = null
        indicator = null
        _binding = null
    }

}