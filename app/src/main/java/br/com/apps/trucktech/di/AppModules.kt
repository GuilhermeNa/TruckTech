package br.com.apps.trucktech.di

import br.com.apps.model.IdHolder
import br.com.apps.repository.di.fireBaseModules
import br.com.apps.repository.di.readModules
import br.com.apps.repository.di.repositoryModules
import br.com.apps.repository.di.writeModules
import br.com.apps.trucktech.ui.activities.main.MainActivityViewModel
import br.com.apps.trucktech.ui.fragments.base_fragments.AuthViewModel
import br.com.apps.trucktech.ui.fragments.login.LoginViewModel
import br.com.apps.trucktech.ui.fragments.nav_documents.documents_list.DocumentsListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.discount.DiscountViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.fines.FinesListViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.frag_performance.PerformanceViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.frag_to_receive.ReceivableViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.payment.PaymentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.refund.RefundViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.time_line.TimelineFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.to_receive.ToReceiveViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.RequestEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.RequestEditorVmData
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_cost.RequestEditorCostFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_cost.RequestEditorCostVmData
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_refuel.RequestEditorRefuelViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_refuel.RequestEditorRefuelVmData
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_wallet.RequestEditorWalletFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor_wallet.RequestEditorWalletVmData
import br.com.apps.trucktech.ui.fragments.nav_requests.request_preview.RequestPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_preview.RequestPreviewVmData
import br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.RequestLVMData
import br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.RequestsListViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_editor.BankEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.BankListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview.BankPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.change_password.ChangePasswordFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsVmData
import br.com.apps.trucktech.ui.fragments.nav_settings.themes.ThemeFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_editor.ExpendEVMData
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_editor.ExpendEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_list.ExpendListViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_preview.ExpendPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_preview.ExpendPreviewVmData
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor.FreightEVMData
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor.FreightEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview.FreightPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview.FreightPreviewVmData
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freights_list.FreightsListViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.records.RecordsViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_editor.RefuelEVMData
import br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_editor.RefuelEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_preview.RefuelPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuel_preview.RefuelPreviewVmData
import br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuels_list.RefuelsListViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.travel_preview.TravelPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.travel_preview.TravelPreviewVmData
import br.com.apps.trucktech.ui.fragments.nav_travel.travels.TravelLVMData
import br.com.apps.trucktech.ui.fragments.nav_travel.travels.TravelsListViewModel
import br.com.apps.usecase.di.useCaseModules
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel<MainActivityViewModel> { MainActivityViewModel(get(), get()) }
    viewModel<LoginViewModel> { LoginViewModel() }
    viewModel<BankListFragmentViewModel> { (id: String) ->
        BankListFragmentViewModel(
            id,
            get(),
            get()
        )
    }
    viewModel<ChangePasswordFragmentViewModel> { ChangePasswordFragmentViewModel(get()) }
    viewModel<ExpendEditorViewModel> { (vmData: ExpendEVMData) ->
        ExpendEditorViewModel(
            vmData,
            get(),
            get(),
            get()
        )
    }
    viewModel<ExpendPreviewViewModel> { (vmData: ExpendPreviewVmData) ->
        ExpendPreviewViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<ExpendListViewModel> { (idHolder: IdHolder) -> ExpendListViewModel(idHolder, get()) }
    viewModel<DocumentsListFragmentViewModel> { (idHolder: IdHolder) ->
        DocumentsListFragmentViewModel(
            idHolder,
            get(),
            get()
        )
    }
    viewModel<FinesListViewModel> { (idHolder: IdHolder) -> FinesListViewModel(idHolder, get()) }
    viewModel<FreightEditorViewModel> { (vmData: FreightEVMData) ->
        FreightEditorViewModel(
            vmData,
            get(),
            get(),
            get()
        )
    }
    viewModel<FreightPreviewViewModel> { (vmData: FreightPreviewVmData) ->
        FreightPreviewViewModel(
            vmData,
            get(),
            get(),
            get()
        )
    }
    viewModel<FreightsListViewModel> { (idHolder: IdHolder) ->
        FreightsListViewModel(
            idHolder,
            get(),
            get()
        )
    }
    viewModel<AuthViewModel> { AuthViewModel(get()) }
    viewModel<RequestEditorViewModel> { (vmData: RequestEditorVmData) -> RequestEditorViewModel(vmData, get(), get(), get()) }
    viewModel<RequestPreviewViewModel> { (vmData: RequestPreviewVmData) ->
        RequestPreviewViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<RequestsListViewModel> { (vmData: RequestLVMData) ->
        RequestsListViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<RecordsViewModel> { RecordsViewModel(get()) }
    viewModel<RefuelEditorViewModel> { (vmData: RefuelEVMData) ->
        RefuelEditorViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<RefuelPreviewViewModel> { (vmData: RefuelPreviewVmData) ->
        RefuelPreviewViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<RefuelsListViewModel> { (travelId: String) ->
        RefuelsListViewModel(
            travelId,
            get()
        )
    }
    viewModel<SettingsViewModel> { (vmData: SettingsVmData) -> SettingsViewModel(vmData) }
    viewModel<ThemeFragmentViewModel> { ThemeFragmentViewModel() }
    viewModel<TimelineFragmentViewModel> { TimelineFragmentViewModel() }
    viewModel<TravelsListViewModel> { (vmData: TravelLVMData) ->
        TravelsListViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<ToReceiveViewModel> { ToReceiveViewModel() }
    viewModel<DiscountViewModel> { (id: String) -> DiscountViewModel(id, get(), get(), get()) }
    viewModel<PaymentViewModel> { (id: String) -> PaymentViewModel(id, get()) }
    viewModel<RefundViewModel> { (id: String) -> RefundViewModel(id, get()) }
    viewModel<RequestEditorRefuelViewModel> { (vmData: RequestEditorRefuelVmData) ->
        RequestEditorRefuelViewModel(
            vmData,
            get(),
            get(),
            get()
        )
    }
    viewModel<RequestEditorCostFragmentViewModel> { (vmData: RequestEditorCostVmData) ->
        RequestEditorCostFragmentViewModel(
            vmData,
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel<RequestEditorWalletFragmentViewModel> { (vmData: RequestEditorWalletVmData) ->
        RequestEditorWalletFragmentViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<BankPreviewViewModel> { (idHolder: IdHolder) ->
        BankPreviewViewModel(
            idHolder,
            get(),
            get()
        )
    }
    viewModel<BankEditorViewModel> { (idHolder: IdHolder) ->
        BankEditorViewModel(
            idHolder,
            get(),
            get()
        )
    }
    viewModel<HomeViewModel> {
        HomeViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel<PerformanceViewModel> { PerformanceViewModel(get()) }
    viewModel<ReceivableViewModel> { ReceivableViewModel() }
    viewModel<TravelPreviewViewModel> { (vmData: TravelPreviewVmData) -> TravelPreviewViewModel(vmData, get()) }
}

val appModules = listOf(
    viewModelModules,
    useCaseModules,
    repositoryModules,
    fireBaseModules,
    writeModules,
    readModules
)