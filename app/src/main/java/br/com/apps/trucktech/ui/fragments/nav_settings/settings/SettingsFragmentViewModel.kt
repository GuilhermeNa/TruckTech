package br.com.apps.trucktech.ui.fragments.nav_settings.settings

import androidx.lifecycle.ViewModel
import br.com.apps.model.model.user.User
import br.com.apps.trucktech.R

class SettingsFragmentViewModel(

    private val user: User?

) : ViewModel() {

    val settingsItems = listOf(
        SettingsItem(
            imageUrl = "https://www3.al.sp.gov.br/repositorio/deputadoPortal/fotos/20230315-170849-id=1649-GRD.jpeg",
            title = user?.name
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

}

data class SettingsItem(

    val imageId: Int? = null,
    val imageUrl: String? = null,
    val title: String? = null,
    val description: String? = null

)