package br.com.apps.repository.di

import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.UserRepository
import br.com.apps.repository.repository.advance.AdvanceReadImpl
import br.com.apps.repository.repository.advance.AdvanceRepository
import br.com.apps.repository.repository.auth.AuthenticationRepository
import br.com.apps.repository.repository.bank.BankRepository
import br.com.apps.repository.repository.bank_account.BankAccountReadImpl
import br.com.apps.repository.repository.bank_account.BankAccountRepository
import br.com.apps.repository.repository.bank_account.BankAccountWriteImpl
import br.com.apps.repository.repository.customer.CustomerReadImpl
import br.com.apps.repository.repository.customer.CustomerRepository
import br.com.apps.repository.repository.document.DocumentReadImpl
import br.com.apps.repository.repository.document.DocumentRepository
import br.com.apps.repository.repository.document.DocumentWriteImpl
import br.com.apps.repository.repository.employee.EmployeeReadImpl
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.repository.employee.EmployeeWriteImpl
import br.com.apps.repository.repository.fine.FineReadImpl
import br.com.apps.repository.repository.fine.FineRepository
import br.com.apps.repository.repository.fine.FineWriteImpl
import br.com.apps.repository.repository.fleet.FleetReadImpl
import br.com.apps.repository.repository.fleet.FleetRepository
import br.com.apps.repository.repository.fleet.FleetWriteImpl
import br.com.apps.repository.repository.freight.FreightReadImpl
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.repository.freight.FreightWriteImpl
import br.com.apps.repository.repository.income.IncomeRepository
import br.com.apps.repository.repository.label.LabelReadImpl
import br.com.apps.repository.repository.label.LabelRepository
import br.com.apps.repository.repository.label.LabelWriteImpl
import br.com.apps.repository.repository.loan.LoanReadImpl
import br.com.apps.repository.repository.loan.LoanRepository
import br.com.apps.repository.repository.loan.LoanWriteImpl
import br.com.apps.repository.repository.outlay.OutlayReadImpl
import br.com.apps.repository.repository.outlay.OutlayRepository
import br.com.apps.repository.repository.outlay.OutlayWriteImpl
import br.com.apps.repository.repository.payable.EmployeePayableReadImpl
import br.com.apps.repository.repository.payable.EmployeePayableRepository
import br.com.apps.repository.repository.receivable.EmployeeReceivableReadImpl
import br.com.apps.repository.repository.receivable.EmployeeReceivableRepository
import br.com.apps.repository.repository.refuel.RefuelReadImpl
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.repository.refuel.RefuelWriteImpl
import br.com.apps.repository.repository.request.RequestReadImpl
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.repository.request.RequestWriteImpl
import br.com.apps.repository.repository.transaction.TransactionReadImpl
import br.com.apps.repository.repository.transaction.TransactionRepository
import br.com.apps.repository.repository.travel.TravelReadImpl
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.repository.travel.TravelWriteImpl
import br.com.apps.repository.repository.travel_aid.TravelAidReadImpl
import br.com.apps.repository.repository.travel_aid.TravelAidRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.koin.dsl.module

val repositoryModules = module {
    single<AuthenticationRepository> { AuthenticationRepository(get()) }
    single<FleetRepository> { FleetRepository(get(), get()) }
    single<UserRepository> { UserRepository(get()) }
    single<DocumentRepository> { DocumentRepository(get(), get()) }
    single<LabelRepository> { LabelRepository(get(), get()) }
    single<IncomeRepository> { IncomeRepository(get()) }
    single<FineRepository> { FineRepository(get(), get()) }
    single<EmployeeRepository> { EmployeeRepository(get(), get()) }
    single<RequestRepository> { RequestRepository(get(), get()) }
    single<StorageRepository> { StorageRepository(get()) }
    single<TravelRepository> { TravelRepository(get(), get()) }
    single<FreightRepository> { FreightRepository(get(), get()) }
    single<RefuelRepository> { RefuelRepository(get(), get()) }
    single<OutlayRepository> { OutlayRepository(get(), get()) }
    single<LoanRepository> { LoanRepository(get()) }
    single<AdvanceRepository> { AdvanceRepository(get()) }
    single<BankRepository> { BankRepository(get()) }
    single<CustomerRepository> { CustomerRepository(get()) }
    single<TravelAidRepository> { TravelAidRepository(get()) }
    single<EmployeeReceivableRepository> { EmployeeReceivableRepository(get()) }
    single<EmployeePayableRepository> { EmployeePayableRepository(get()) }
    single<BankAccountRepository> { BankAccountRepository(get(), get()) }
    single<TransactionRepository> { TransactionRepository(get()) }
}

val writeModules = module {
    single<FleetWriteImpl> { FleetWriteImpl(get()) }
    single<DocumentWriteImpl> { DocumentWriteImpl(get()) }
    single<OutlayWriteImpl> { OutlayWriteImpl(get()) }
    single<FineWriteImpl> { FineWriteImpl(get()) }
    single<FreightWriteImpl> { FreightWriteImpl(get()) }
    single<LoanWriteImpl> { LoanWriteImpl(get()) }
    single<RefuelWriteImpl> { RefuelWriteImpl(get()) }
    single<RequestWriteImpl> { RequestWriteImpl(get()) }
    single<TravelWriteImpl> { TravelWriteImpl(get()) }
    single<LabelWriteImpl> { LabelWriteImpl(get()) }
    single<EmployeeWriteImpl> { EmployeeWriteImpl(get()) }
    single<BankAccountWriteImpl> { BankAccountWriteImpl(get()) }
}

val readModules = module {
    single<FleetReadImpl> { FleetReadImpl(get()) }
    single<AdvanceReadImpl> { AdvanceReadImpl(get()) }
    single<DocumentReadImpl> { DocumentReadImpl(get()) }
    single<OutlayReadImpl> { OutlayReadImpl(get()) }
    single<FineReadImpl> { FineReadImpl(get()) }
    single<FineReadImpl> { FineReadImpl(get()) }
    single<FreightReadImpl> { FreightReadImpl(get()) }
    single<LoanReadImpl> { LoanReadImpl(get()) }
    single<RefuelReadImpl> { RefuelReadImpl(get()) }
    single<RequestReadImpl> { RequestReadImpl(get()) }
    single<TravelReadImpl> { TravelReadImpl(get()) }
    single<LabelReadImpl> { LabelReadImpl(get()) }
    single<EmployeeReadImpl> { EmployeeReadImpl(get()) }
    single<CustomerReadImpl> { CustomerReadImpl(get()) }
    single<TravelAidReadImpl> { TravelAidReadImpl(get()) }
    single<EmployeeReceivableReadImpl> { EmployeeReceivableReadImpl(get()) }
    single<EmployeePayableReadImpl> { EmployeePayableReadImpl(get()) }
    single<BankAccountReadImpl> { BankAccountReadImpl(get()) }
    single<TransactionReadImpl> { TransactionReadImpl(get()) }
}

val fireBaseModules = module {
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }
    single<FirebaseStorage> { Firebase.storage }
}
