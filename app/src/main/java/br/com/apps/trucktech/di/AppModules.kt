package br.com.apps.trucktech.di

import br.com.apps.model.model.user.User
import br.com.apps.repository.fireBaseModules
import br.com.apps.repository.repositoryModules
import br.com.apps.trucktech.ui.activities.main.DriverAndTruck
import br.com.apps.trucktech.ui.activities.main.MainActivityViewModel
import br.com.apps.trucktech.ui.fragments.login.LoginFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_documents.document.DocumentFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_documents.documents_list.DocumentsListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.discount.DiscountFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.fines.FinesFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeFragmentFineViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeFragmentPerformanceViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeFragmentToReceiveViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.payment.PaymentFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.refund.RefundFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.time_line.TimelineFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.to_receive.ToReceiveFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.RequestEditorFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_cost.RequestEditorCostFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_refuel.RequestEditorRefuelViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_wallet.RequestEditorWalletFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_preview.RequestPreviewFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.RequestsListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_editor.BankEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.BankListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview.BankPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.change_password.ChangePasswordFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.themes.ThemeFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_editor.CostEditorFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.cost_preview.CostPreviewFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.costs_list.CostsListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor.FreightEditorFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview.FreightPreviewFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freights_list.FreightsListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.records.RecordsFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_editor.RefuelEditorFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_preview.RefuelPreviewFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuels_list.RefuelsListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.travels.TravelsListViewModel
import br.com.apps.usecase.useCaseModules
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel<MainActivityViewModel> { MainActivityViewModel(get(), get()) }
    viewModel<BankListFragmentViewModel> { (id: String) -> BankListFragmentViewModel(id, get()) }
    viewModel<ChangePasswordFragmentViewModel> { ChangePasswordFragmentViewModel(get()) }
    viewModel<CostEditorFragmentViewModel> { CostEditorFragmentViewModel() }
    viewModel<CostPreviewFragmentViewModel> { CostPreviewFragmentViewModel() }
    viewModel<CostsListFragmentViewModel> { CostsListFragmentViewModel() }
    viewModel<DocumentFragmentViewModel> { (id: String) ->
        DocumentFragmentViewModel(
            id,
            get(),
            get()
        )
    }
    viewModel<DocumentsListFragmentViewModel> { (masterUid: String, id: String) ->
        DocumentsListFragmentViewModel(
            masterUid,
            id,
            get(),
            get()
        )
    }
    viewModel<FinesFragmentViewModel> { FinesFragmentViewModel() }
    viewModel<FreightEditorFragmentViewModel> { FreightEditorFragmentViewModel() }
    viewModel<FreightPreviewFragmentViewModel> { FreightPreviewFragmentViewModel() }
    viewModel<FreightsListFragmentViewModel> { (id: String) -> FreightsListFragmentViewModel(id, get()) }
    viewModel<HomeFragmentViewModel> { HomeFragmentViewModel(get()) }
    viewModel<LoginFragmentViewModel> { LoginFragmentViewModel(get()) }
    viewModel<RequestEditorFragmentViewModel> { RequestEditorFragmentViewModel(get()) }
    viewModel<RequestPreviewFragmentViewModel> { (id: String) ->
        RequestPreviewFragmentViewModel(
            id,
            get()
        )
    }
    viewModel<RequestsListFragmentViewModel> { (driverAndTruck: DriverAndTruck) ->
        RequestsListFragmentViewModel(
            driverAndTruck,
            get()
        )
    }
    viewModel<RecordsFragmentViewModel> { RecordsFragmentViewModel() }
    viewModel<RefuelEditorFragmentViewModel> { RefuelEditorFragmentViewModel() }
    viewModel<RefuelPreviewFragmentViewModel> { RefuelPreviewFragmentViewModel() }
    viewModel<RefuelsListFragmentViewModel> { RefuelsListFragmentViewModel() }
    viewModel<SettingsFragmentViewModel> { (user: User) -> SettingsFragmentViewModel(user) }
    viewModel<ThemeFragmentViewModel> { ThemeFragmentViewModel() }
    viewModel<TimelineFragmentViewModel> { TimelineFragmentViewModel() }
    viewModel<TravelsListViewModel> { (id: String) -> TravelsListViewModel(id, get()) }
    viewModel<HomeFragmentPerformanceViewModel> { HomeFragmentPerformanceViewModel() }
    viewModel<HomeFragmentToReceiveViewModel> { HomeFragmentToReceiveViewModel() }
    viewModel<ToReceiveFragmentViewModel> { ToReceiveFragmentViewModel() }
    viewModel<DiscountFragmentViewModel> { DiscountFragmentViewModel() }
    viewModel<PaymentFragmentViewModel> { PaymentFragmentViewModel() }
    viewModel<RefundFragmentViewModel> { RefundFragmentViewModel() }
    viewModel<RequestEditorRefuelViewModel> { (idRequest: String, idItem: String?) ->
        RequestEditorRefuelViewModel(
            idRequest,
            idItem,
            get()
        )
    }
    viewModel<RequestEditorCostFragmentViewModel> { (masterUid: String?, idRequest: String, idItem: String?) ->
        RequestEditorCostFragmentViewModel(
            masterUid,
            idRequest,
            idItem,
            get(),
            get()
        )
    }
    viewModel<RequestEditorWalletFragmentViewModel> { (idRequest: String, idItem: String?) ->
        RequestEditorWalletFragmentViewModel(
            idRequest,
            idItem,
            get()
        )
    }
    viewModel<HomeFragmentFineViewModel> { HomeFragmentFineViewModel(get()) }
    viewModel<BankPreviewViewModel> { (employeeId: String, bankId: String) ->
        BankPreviewViewModel(
            employeeId,
            bankId,
            get()
        )
    }
    viewModel<BankEditorViewModel> { (employeeId: String, bankId: String?) ->
        BankEditorViewModel(
            employeeId,
            bankId,
            get()
        )
    }
}

val appModules = listOf(
    viewModelModules,
    useCaseModules,
    repositoryModules,
    fireBaseModules
)





