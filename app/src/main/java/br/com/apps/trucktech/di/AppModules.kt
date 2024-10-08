package br.com.apps.trucktech.di

import br.com.apps.repository.di.fireBaseModules
import br.com.apps.repository.di.readModules
import br.com.apps.repository.di.repositoryModules
import br.com.apps.repository.di.writeModules
import br.com.apps.trucktech.service.CustomerService
import br.com.apps.trucktech.service.FreightService
import br.com.apps.trucktech.service.RequestService
import br.com.apps.trucktech.ui.activities.main.MainActivityViewModel
import br.com.apps.trucktech.ui.fragments.base_fragments.AuthViewModel
import br.com.apps.trucktech.ui.fragments.login.LoginViewModel
import br.com.apps.trucktech.ui.fragments.nav_documents.documents_list.DocumentListVmData
import br.com.apps.trucktech.ui.fragments.nav_documents.documents_list.DocumentsListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.fines.FinesListViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.frag_performance.PerformanceViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.home.frag_to_receive.ReceivableViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.time_line.TimelineFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_home.to_receive.ToReceiveViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.item_editor.ItemEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.item_editor.ItemEditorVmData
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.RequestEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.RequestEditorVmData
import br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.RequestLVMData
import br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.RequestsListViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_editor.BankEVmData
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_editor.BankEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.BankListFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview.BankPVmData
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_preview.BankPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.change_password.ChangePasswordFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsViewModel
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsVmData
import br.com.apps.trucktech.ui.fragments.nav_settings.themes.ThemeFragmentViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_editor.OutlayEVMData
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_editor.OutlayEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_list.ExpendLVmData
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_list.ExpendListViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_preview.ExpendPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_preview.ExpendPreviewVmData
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor.FreightEVMData
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_editor.FreightEditorViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview.FreightPreviewViewModel
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freight_preview.FreightPreviewVmData
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freights_list.FreightLVmData
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
    viewModel<MainActivityViewModel> {
        MainActivityViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
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
    viewModel<LoginViewModel> { LoginViewModel() }
    viewModel<BankListFragmentViewModel> { (id: String) ->
        BankListFragmentViewModel(
            id,
            get(),
            get()
        )
    }
    viewModel<ChangePasswordFragmentViewModel> { ChangePasswordFragmentViewModel(get()) }
    viewModel<OutlayEditorViewModel> { (vmData: OutlayEVMData) ->
        OutlayEditorViewModel(
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
    viewModel<ExpendListViewModel> { (vmData: ExpendLVmData) ->
        ExpendListViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<DocumentsListFragmentViewModel> { (vmData: DocumentListVmData) ->
        DocumentsListFragmentViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<FinesListViewModel> { FinesListViewModel() }
    viewModel<FreightEditorViewModel> { (vmData: FreightEVMData) ->
        FreightEditorViewModel(
            vmData,
            get(),
            get(),
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
    viewModel<FreightsListViewModel> { (vmData: FreightLVmData) ->
        FreightsListViewModel(vmData, get(), get())
    }
    viewModel<AuthViewModel> { AuthViewModel(get()) }
    viewModel<RequestEditorViewModel> { (vmData: RequestEditorVmData) ->
        RequestEditorViewModel(
            vmData,
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel<RequestsListViewModel> { (vmData: RequestLVMData) ->
        RequestsListViewModel(
            vmData,
            get(),
            get(),
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
    viewModel<BankPreviewViewModel> { (vmData: BankPVmData) ->
        BankPreviewViewModel(
            vmData,
            get(),
            get()
        )
    }
    viewModel<BankEditorViewModel> { (vmData: BankEVmData) ->
        BankEditorViewModel(
            vmData,
            get(),
            get(),
            get()
        )
    }
    viewModel<HomeViewModel> { HomeViewModel() }
    viewModel<PerformanceViewModel> { PerformanceViewModel(get()) }
    viewModel<TravelPreviewViewModel> { (vmData: TravelPreviewVmData) ->
        TravelPreviewViewModel(
            vmData,
            get()
        )
    }
    viewModel<ReceivableViewModel> { ReceivableViewModel() }
    viewModel<ItemEditorViewModel> { (vmData: ItemEditorVmData) ->
        ItemEditorViewModel(
            vmData,
            get(),
            get(),
            get()
        )
    }
}

val serviceModules = module {
    factory <RequestService> { RequestService(get(), get()) }
    factory <FreightService> { FreightService(get()) }
    factory <CustomerService> { CustomerService(get()) }
}

val appModules = listOf(
    viewModelModules,
    serviceModules,
    useCaseModules,
    repositoryModules,
    fireBaseModules,
    writeModules,
    readModules
)