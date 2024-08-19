package br.com.apps.trucktech.ui.fragments.nav_requests.item_editor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.model.model.request.Item
import br.com.apps.repository.util.RESULT_KEY
import br.com.apps.repository.util.TAG_DEBUG
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.FragmentItemEditorBinding
import br.com.apps.trucktech.expressions.hideKeyboard
import br.com.apps.trucktech.expressions.popBackStack
import br.com.apps.trucktech.ui.activities.CameraActivity
import br.com.apps.trucktech.ui.fragments.base_fragments.BaseFragmentWithToolbar
import br.com.apps.trucktech.ui.fragments.image.ImageFragment
import br.com.apps.trucktech.util.DeviceCapabilities
import br.com.apps.trucktech.util.state.State
import coil.load
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.security.InvalidParameterException

private const val TOOLBAR_TITLE_EDITING = "Editando Item"
private const val TOOLBAR_TITLE_CREATING = "Adicionando Item"

class ItemEditorFragment : BaseFragmentWithToolbar() {

    private var _binding: FragmentItemEditorBinding? = null
    private val binding get() = _binding!!
    private var stateHandler: ItemEditorState? = null
    private var listener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var treeObserver: ViewTreeObserver? = null

    private val args: ItemEditorFragmentArgs by navArgs()
    private val vmData by lazy {
        ItemEditorVmData(
            requestId = args.requestId,
            itemId = args.itemId,
            access = mainActVM.loggedUser.accessLevel
        )
    }
    private val viewModel: ItemEditorViewModel by viewModel { parametersOf(vmData) }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val data = result.data!!
                processActivityLauncherResult(data)
            }
        }

    private fun processActivityLauncherResult(data: Intent) {
        try {

            val ba = data.getByteArrayExtra(RESULT_KEY)
            ba?.run {
                viewModel.setUrlImage(this)
            }

        } catch (e: Exception) {
            Log.e(
                TAG_DEBUG,
                "RequestEditorFragment, processActivityLauncherResult: ${e.message.toString()}"
            )
        }
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW
    //---------------------------------------------------------------------------------------------//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemEditorBinding.inflate(inflater, container, false)
        stateHandler = ItemEditorState(binding, lifecycleScope)
        return binding.root
    }

    //---------------------------------------------------------------------------------------------//
    // ON VIEW CREATED
    //---------------------------------------------------------------------------------------------//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateManager()
        initTextWatcher()
        initOnTouchListener()
        initFab()
        initImgButtons()
    }

    private fun initImgButtons() {
        binding.run {
            fragIeBadgeMenuShow.setOnClickListener {
                val bundle = Bundle()

                when (viewModel.urlImage.value) {
                    is ByteArray -> bundle.putByteArray(
                        ImageFragment.ARG_URL_IMAGE,
                        viewModel.urlImage.value as ByteArray
                    )

                    is String -> bundle.putString(
                        ImageFragment.ARG_URL_IMAGE,
                        viewModel.urlImage.value as String
                    )
                }

                Navigation.findNavController(it).navigate(
                    R.id.action_global_image_fragment,
                    bundle
                )

            }

            fragIeBadgeMenuDelete.setOnClickListener {
                viewModel.setUrlImage(null)
            }

        }
    }

    private fun initFab() {
        binding.fragIeFab.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initOnTouchListener() {
        binding.run {
            fragIeLayout.setOnTouchListener { _, _ ->
                fragIeEdtxtDescription.let { edTxt ->
                    if (edTxt.hasFocus()) edTxt.clearFocus()
                }
                fragIeEdtxtValue.let { edTxt ->
                    if (edTxt.hasFocus()) edTxt.clearFocus()
                }
                hideKeyboard()
                true
            }


        }
    }

    private fun initTextWatcher() {
        // binding.fragIeEdtxtValue.addTextChangedListener(MonetaryMaskUtil(binding.fragIeEdtxtValue))
    }

    override fun initMenuClickListeners(toolbar: Toolbar) {
        super.initMenuClickListeners(toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_editor_save -> {
                    //TODO
                    true
                }

                else -> throw NullPointerException()
            }
        }
    }

    private fun save() {

    }

    override fun configureBaseFragment(configurator: BaseFragmentConfigurator) {
        configurator.toolbar(
            hasToolbar = true,
            hasNavigation = true,
            toolbar = binding.fragIeToolbar.toolbar,
            menuId = R.menu.menu_editor,
            toolbarTextView = binding.fragIeToolbar.toolbarText,
            title = when (args.itemId) {
                null -> TOOLBAR_TITLE_CREATING
                else -> TOOLBAR_TITLE_EDITING
            }
        )
        configurator.bottomNavigation(hasBottomNavigation = false)
    }

    private fun initStateManager() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> stateHandler?.showLoading()
                is State.Loaded -> {
                    if (viewModel.isFirstBoot) {
                        stateHandler?.showLoaded()
                    }
                    setKeyBoardVisibilityListener()
                }

                is State.Error -> requireView().popBackStack()
                else -> throw InvalidParameterException()
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { data ->
            bind(data)
        }

        viewModel.urlImage.observe(viewLifecycleOwner) { urlImage ->
            if (urlImage == null) {
                stateHandler?.hideImage()
                stateHandler?.setFabIconPhoto()
            } else {
                binding.fragIeImg.load(urlImage)
                stateHandler?.showImage()
                stateHandler?.setFabIconRefresh()
            }

        }

    }

    private fun setKeyBoardVisibilityListener() {
        viewModel.setViewHeight(binding.root.height)

        listener = DeviceCapabilities.softKeyboardListener(
            root = binding.root,
            height = viewModel.viewHeight
        ) { isOpen ->
            when (isOpen) {
                true -> stateHandler?.hideFab()
                false -> stateHandler?.showFab()
            }
        }

        treeObserver = binding.root.viewTreeObserver
            .apply { addOnGlobalLayoutListener(listener) }
    }

    private fun bind(item: Item) {
        binding.apply {
            fragIeEdtxtDescription.setText(item.description)
            fragIeEdtxtValue.setText(item.value.toCurrencyPtBr())
        }
    }

//---------------------------------------------------------------------------------------------//
// ON DESTROY VIEW
//---------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        treeObserver!!.removeOnGlobalLayoutListener(listener)
        stateHandler = null
        _binding = null
    }

}