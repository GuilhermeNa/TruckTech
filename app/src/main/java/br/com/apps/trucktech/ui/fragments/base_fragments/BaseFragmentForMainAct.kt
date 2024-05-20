package br.com.apps.trucktech.ui.fragments.base_fragments

import android.os.Bundle
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.ui.activities.login.LoginActivity
import br.com.apps.trucktech.ui.activities.main.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

abstract class BaseFragmentForMainAct: BaseFragment() {

    val sharedViewModel: MainActivityViewModel by activityViewModel()

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userIsStillLogged()
    }

    /**
     * Every time a new fragment is started, it will check if the user is still logged in via the
     * login view model. If not, it will navigate to the Login Activity.
     */
    private fun userIsStillLogged() {
        authViewModel.getCurrentUser().let { user ->
            if(user == null) {
                requireActivity().navigateTo(LoginActivity::class.java)
                requireActivity().finish()
            }
        }
    }

}