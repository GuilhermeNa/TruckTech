package br.com.apps.usecase.di

import br.com.apps.usecase.usecase.AuthenticationUseCase
import br.com.apps.usecase.usecase.BankAccountUseCase
import br.com.apps.usecase.usecase.CostUseCase
import br.com.apps.usecase.usecase.CustomerUseCase
import br.com.apps.usecase.usecase.DocumentUseCase
import br.com.apps.usecase.usecase.EmployeeUseCase
import br.com.apps.usecase.usecase.FineUseCase
import br.com.apps.usecase.usecase.FleetUseCase
import br.com.apps.usecase.usecase.FreightUseCase
import br.com.apps.usecase.usecase.IncomeUseCase
import br.com.apps.usecase.usecase.LabelUseCase
import br.com.apps.usecase.usecase.OrderUseCase
import br.com.apps.usecase.usecase.OutlayUseCase
import br.com.apps.usecase.usecase.RefuelUseCase
import br.com.apps.usecase.usecase.RequestUseCase
import br.com.apps.usecase.usecase.StorageUseCase
import br.com.apps.usecase.usecase.TimeLineUseCase
import br.com.apps.usecase.usecase.TravelAidUseCase
import br.com.apps.usecase.usecase.TravelUseCase
import br.com.apps.usecase.usecase.UserUseCase
import org.koin.dsl.module

val useCaseModules = module {
    single<CostUseCase> { CostUseCase() }
    single<DocumentUseCase> { DocumentUseCase(get()) }
    single<EmployeeUseCase> { EmployeeUseCase(get(), get()) }
    single<OutlayUseCase> { OutlayUseCase(get()) }
    single<FineUseCase> { FineUseCase(get()) }
    single<FleetUseCase> { FleetUseCase(get()) }
    single<FreightUseCase> { FreightUseCase(get()) }
    single<LabelUseCase> { LabelUseCase(get()) }
    single<OrderUseCase> { OrderUseCase() }
    single<RequestUseCase> { RequestUseCase(get(), get(), get(), get()) }
    single<TimeLineUseCase> { TimeLineUseCase() }
    single<UserUseCase> { UserUseCase(get()) }
    single<AuthenticationUseCase> { AuthenticationUseCase(get(), get()) }
    single<IncomeUseCase> { IncomeUseCase(get()) }
    single<StorageUseCase> { StorageUseCase(get()) }
    single<TravelUseCase> { TravelUseCase(get(),get(), get(), get(), get()) }
    single<RefuelUseCase> { RefuelUseCase(get()) }
    single<CustomerUseCase> { CustomerUseCase(get()) }
    single<TravelAidUseCase> { TravelAidUseCase(get()) }
    single<BankAccountUseCase> { BankAccountUseCase(get()) }
}
