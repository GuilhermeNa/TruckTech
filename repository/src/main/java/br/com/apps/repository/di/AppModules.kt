package br.com.apps.repository.di

import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.UserRepository
import br.com.apps.repository.repository.advance.AdvanceRead
import br.com.apps.repository.repository.advance.AdvanceRepository
import br.com.apps.repository.repository.auth.AuthenticationRepository
import br.com.apps.repository.repository.bank.BankRepository
import br.com.apps.repository.repository.document.DocumentRead
import br.com.apps.repository.repository.document.DocumentRepository
import br.com.apps.repository.repository.document.DocumentWrite
import br.com.apps.repository.repository.employee.EmployeeRead
import br.com.apps.repository.repository.employee.EmployeeRepository
import br.com.apps.repository.repository.employee.EmployeeWrite
import br.com.apps.repository.repository.expend.ExpendRead
import br.com.apps.repository.repository.expend.ExpendRepository
import br.com.apps.repository.repository.expend.ExpendWrite
import br.com.apps.repository.repository.fine.FineRead
import br.com.apps.repository.repository.fine.FineRepository
import br.com.apps.repository.repository.fine.FineWrite
import br.com.apps.repository.repository.fleet.FleetRead
import br.com.apps.repository.repository.fleet.FleetRepository
import br.com.apps.repository.repository.fleet.FleetWrite
import br.com.apps.repository.repository.freight.FreightRead
import br.com.apps.repository.repository.freight.FreightRepository
import br.com.apps.repository.repository.freight.FreightWrite
import br.com.apps.repository.repository.income.IncomeRepository
import br.com.apps.repository.repository.label.LabelRead
import br.com.apps.repository.repository.label.LabelRepository
import br.com.apps.repository.repository.label.LabelWrite
import br.com.apps.repository.repository.loan.LoanRead
import br.com.apps.repository.repository.loan.LoanRepository
import br.com.apps.repository.repository.loan.LoanWrite
import br.com.apps.repository.repository.refuel.RefuelRead
import br.com.apps.repository.repository.refuel.RefuelRepository
import br.com.apps.repository.repository.refuel.RefuelWrite
import br.com.apps.repository.repository.request.RequestRead
import br.com.apps.repository.repository.request.RequestRepository
import br.com.apps.repository.repository.request.RequestWrite
import br.com.apps.repository.repository.travel.TravelRead
import br.com.apps.repository.repository.travel.TravelRepository
import br.com.apps.repository.repository.travel.TravelWrite
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
    single<ExpendRepository> { ExpendRepository(get(), get()) }
    single<LoanRepository> { LoanRepository(get()) }
    single<AdvanceRepository> { AdvanceRepository(get()) }
    single<BankRepository> { BankRepository(get()) }
}

val writeModules = module {
    single<FleetWrite> { FleetWrite(get()) }
    single<DocumentWrite> { DocumentWrite(get()) }
    single<ExpendWrite> { ExpendWrite(get()) }
    single<FineWrite> { FineWrite(get()) }
    single<FreightWrite> { FreightWrite(get()) }
    single<LoanWrite> { LoanWrite(get()) }
    single<RefuelWrite> { RefuelWrite(get()) }
    single<RequestWrite> { RequestWrite(get()) }
    single<TravelWrite> { TravelWrite(get()) }
    single<LabelWrite> { LabelWrite(get()) }
    single<EmployeeWrite> { EmployeeWrite(get()) }
}

val readModules = module {
    single<FleetRead> { FleetRead(get()) }
    single<AdvanceRead> { AdvanceRead(get()) }
    single<DocumentRead> { DocumentRead(get()) }
    single<ExpendRead> { ExpendRead(get()) }
    single<FineRead> { FineRead(get()) }
    single<FineRead> { FineRead(get()) }
    single<FreightRead> { FreightRead(get()) }
    single<LoanRead> { LoanRead(get()) }
    single<RefuelRead> { RefuelRead(get()) }
    single<RequestRead> { RequestRead(get()) }
    single<TravelRead> { TravelRead(get()) }
    single<LabelRead> { LabelRead(get()) }
    single<EmployeeRead> { EmployeeRead(get()) }
}

val fireBaseModules = module {
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }
    single<FirebaseStorage> { Firebase.storage }

}
