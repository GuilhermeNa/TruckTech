package br.com.apps.trucktech.ui.fragments.nav_home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import br.com.apps.model.model.user.CommonUser
import br.com.apps.trucktech.databinding.FragmentHomeBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.samplePerformanceList
import br.com.apps.trucktech.sampleTravelsList
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.nav_home.home.private_adapters.HomeFragmentPerformanceViewPagerAdapter
import br.com.apps.trucktech.ui.fragments.nav_home.home.private_adapters.PeriodRecyclerAdapter
import br.com.apps.trucktech.ui.public_adapters.HeaderDefaultHorizontalAdapter
import me.relex.circleindicator.CircleIndicator3
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeFragmentViewModel by viewModel()
    private val toReceiveViewModel: HomeFragmentToReceiveViewModel by viewModel()
    private val performanceViewModel: HomeFragmentPerformanceViewModel by viewModel()
    private val fineViewModel: HomeFragmentFineViewModel by viewModel()

    private lateinit var viewPagerAdapter: HomeFragmentPerformanceViewPagerAdapter
    private var viewPager2: ViewPager2? = null
    private var indicator: CircleIndicator3? = null

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadUserData()
    }

    /**
     * When the app starts and the user logs in, it will load the driver data and save it in the
     * shared activity ViewModel.
     */
    private fun loadUserData() {
        loginViewModel.userId?.let {
            sharedViewModel.loadUserData(it)
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initToReceivePanel()
        initPerformancePanel()
        initMyFinesPanel()
        initTimeLinePanel()
    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(hasToolbar = false)
        configurator.bottomNavigation(hasBottomNavigation = true)
    }

    /**
     * State manager for home fragment
     */
    private fun initStateManager() {
        sharedViewModel.userData.observe(viewLifecycleOwner) {
            it?.let { driverAndTruck ->
                driverAndTruck.user?.let { user ->
                    bindHeader(user)
                    user.employeeId?.let { employeeId -> fineViewModel.loadFines(employeeId) }
                }
            }
        }
    }

    private fun bindHeader(user: CommonUser) {
        binding.fragmentHomeName.text = user.name
    }

    /**
     * To Receive Panel is configured here
     */
    private fun initToReceivePanel() {
        binding.homeFragmentPanelToReceive.apply {
            panelToReceiveValue.text = "R$ 2.486,40"
            panelToReceivePercentualComission.text = "70%"
            panelToReceivePercentualRefund.text = "10%"
            panelToReceivePercentualDiscounts.text = "20%"
            panelToReceiveNumberOfFreights.text = "3 frete"
            panelToReceiveNumberOfRefunds.text = "3 reembolsos"
            panelToReceiveNumberOfDiscounts.text = "3 descontos"
            panelToReceiveDescription.text =
                "Esse saldo foi fechado no dia 20 de janeiro e está aguardando aprovação para pagamento."

            panelToReceiveButton.setOnClickListener {
                it.navigateTo(HomeFragmentDirections.actionHomeFragmentToToReceiveFragment())
            }
        }

    }

    /**
     * Performance Panel is configured here
     */
    private fun initPerformancePanel() {
        initHeaderRecycler()
        initPeriodRecycler()
        initViewPager()
        initPagerTransformer()
        initCircleIndicator()
        initPerformancePanelStateManager()
    }

    private fun initHeaderRecycler() {
        val recyclerView = binding.homeFragmentPanelPerformance.panelPerformanceHeaderRecycler
        val adapter = HeaderDefaultHorizontalAdapter(
            context = requireContext(),
            dataSet = performanceViewModel.headerItemsMap,
            adapterPos = performanceViewModel.headerPos.value,
            itemClickListener = { headerTxt ->
                performanceViewModel.newHeaderSelected(headerTxt)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun initPeriodRecycler() {
        val recyclerView = binding.homeFragmentPanelPerformance.panelPerformancePeriodRecycler
        val adapter = PeriodRecyclerAdapter(
            context = requireContext(),
            dataSet = sampleTravelsList,
            adapterPos = performanceViewModel.periodPos.value,
            itemClickListener = { adapterPos ->
                performanceViewModel.newPeriodSelected(adapterPos)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun initViewPager() {
        viewPager2 = binding.homeFragmentPanelPerformance.panelPerformanceViewPager2
        viewPagerAdapter =
            HomeFragmentPerformanceViewPagerAdapter(requireContext(), samplePerformanceList)
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
        indicator = binding.homeFragmentPanelPerformance.panelPerformanceCircleIndicator
        indicator?.setViewPager(viewPager2)
    }

    private fun initPerformancePanelStateManager() {
        performanceViewModel.headerPos.observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                //TODO alterar
            }
        }
    }

    /**
     * My fines Panel is configured here
     */
    private fun initMyFinesPanel() {
        fineViewModel.fineDataSet.observe(viewLifecycleOwner) {
            it?.let { finesList ->

            }
        }


        binding.homeFragmentPanelMyFines.apply {
            panelFinesImage.loadImageThroughUrl(
                "https://gringo.com.vc/wp-content/uploads/2022/06/Multa_18032016_1738_1280_960-1024x768.jpg",
                requireContext()
            )
            panelFinesNew.text = "3"
            panelFinesAccumulated.text = "7"

            panelFinesCard.setOnClickListener {
                it.navigateTo(HomeFragmentDirections.actionHomeFragmentToFinesFragment())
            }
        }

    }

    /**
     * TimeLine Panel is configured here
     */
    private fun initTimeLinePanel() {
        binding.homeFragmentPanelTimeLine.apply {
            panelTimeLineImage.loadImageThroughUrl(
                "https://cdn.sanity.io/images/599r6htc/localized/e09081e08bcc400a488dd7c1fa88a4d1493b52aa-1108x1108.png?w=514&q=75&fit=max&auto=format",
                requireContext()
            )
            panelTimeLineCard.setOnClickListener {
                it.navigateTo(HomeFragmentDirections.actionHomeFragmentToTimelineFragment())
            }
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON DESTROY
    //---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        _binding = null
        viewPager2?.adapter = null
        viewPager2 = null
        indicator = null
        super.onDestroyView()
    }

}