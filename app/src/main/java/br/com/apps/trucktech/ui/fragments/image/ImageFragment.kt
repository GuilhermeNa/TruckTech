package br.com.apps.trucktech.ui.fragments.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.apps.trucktech.databinding.FragmentImageBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.popBackStack

/**
 * A simple [Fragment] subclass.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val urlImage = arguments?.get(ARG_URL_IMAGE)

        with(binding) {
            fragIImg.loadImageThroughUrl(urlImage)
            fragILayoutBack.setOnClickListener {
                it.popBackStack()
            }
        }

    }

    companion object {
        const val ARG_URL_IMAGE = "urlImage"
    }


}