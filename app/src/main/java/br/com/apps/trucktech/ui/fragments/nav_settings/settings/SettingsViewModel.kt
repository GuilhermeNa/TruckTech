package br.com.apps.trucktech.ui.fragments.nav_settings.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.apps.trucktech.R

class SettingsViewModel(vmData: SettingsVmData) : ViewModel() {

    /**
     * LiveData with a dark layer state, used when dialog is requested.
     */
    private var _darkLayer = MutableLiveData(false)
    val darkLayer get() = _darkLayer

    /**
     * LiveData with a bottom nav state, used when dialog is requested.
     */
    private var _bottomNav = MutableLiveData(true)
    val bottomNav get() = _bottomNav

    /**
     * The list of items to be shown on Ui.
     */
    val settingsItems = listOf(
        SettingsItem(
            imageUrl = vmData.urlImage,
            title = vmData.userName
        ),
        SettingsItem(
            imageId = R.drawable.icon_key,
            title = PASSWORD,
            description = "Alterar a senha"
        ),
        SettingsItem(
            imageId = R.drawable.icon_theme,
            title = THEME,
            description = "Alterar as configurações de tema do App"
        ),
        SettingsItem(
            imageId = R.drawable.icon_bank,
            title = BANK,
            description = "Seus dados bancários para recebimento de comissão"
        ),
        SettingsItem(
            imageId = R.drawable.icon_logout,
            title = LOGOUT,
            description = "Sair do App e esquecer senha"
        )
    )

    companion object {
        const val PASSWORD = "Senha"
        const val THEME = "Tema"
        const val LOGOUT = "Sair"
        const val BANK = "Dados bancários"
    }

    //---------------------------------------------------------------------------------------------//
    // -
    //---------------------------------------------------------------------------------------------//

    /**
     * Sets the visibility of the [_darkLayer] to true, indicating that it should be shown.
     */
    fun requestDarkLayer() {
        _darkLayer.value = true
    }

    /**
     * Sets the visibility of the [_darkLayer] to false, indicating that it should be dismissed.
     */
    fun dismissDarkLayer() {
        _darkLayer.value = false
    }

    /**
     * Sets the visibility of the [_bottomNav] to true, indicating that it should be shown.
     */
    fun requestBottomNav() {
        _bottomNav.value = true
    }

    /**
     * Sets the visibility of the [_bottomNav] to false, indicating that it should be dismissed.
     */
    fun dismissBottomNav() {
        _bottomNav.value = false
    }

}

data class SettingsItem(
    val imageId: Int? = null,
    val imageUrl: String? = null,
    val title: String? = null,
    val description: String? = null
)

data class SettingsVmData(
    val userName: String,
    val urlImage: String? = null
)