package br.com.apps.usecase

import org.koin.dsl.module

val useCaseModules = module {
    single<CostUseCase> { CostUseCase() }
    single<DocumentUseCase> { DocumentUseCase(get()) }
    single<EmployeeUseCase> { EmployeeUseCase(get(), get()) }
    single<ExpendUseCase> { ExpendUseCase(get(), get()) }
    single<FineUseCase> { FineUseCase(get()) }
    single<FleetUseCase> { FleetUseCase(get()) }
    single<FreightUseCase> { FreightUseCase(get(), get()) }
    single<LabelUseCase> { LabelUseCase(get()) }
    single<OrderUseCase> { OrderUseCase() }
    single<RequestUseCase> { RequestUseCase(get()) }
    single<TimeLineUseCase> { TimeLineUseCase() }
    single<UserUseCase> { UserUseCase(get()) }
    single<AuthenticationUseCase> { AuthenticationUseCase(get(), get()) }
    single<IncomeUseCase> { IncomeUseCase(get()) }
    single<StorageUseCase> { StorageUseCase(get()) }
    single<TravelUseCase> { TravelUseCase(get(),get(), get(), get()) }
    single<RefuelUseCase> { RefuelUseCase(get()) }

}