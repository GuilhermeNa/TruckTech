package br.com.apps.repository

import br.com.apps.repository.repository.AdvanceRepository
import br.com.apps.repository.repository.AuthenticationRepository
import br.com.apps.repository.repository.DocumentRepository
import br.com.apps.repository.repository.EmployeeRepository
import br.com.apps.repository.repository.ExpendRepository
import br.com.apps.repository.repository.FineRepository
import br.com.apps.repository.repository.FleetRepository
import br.com.apps.repository.repository.FreightRepository
import br.com.apps.repository.repository.IncomeRepository
import br.com.apps.repository.repository.LabelRepository
import br.com.apps.repository.repository.LoanRepository
import br.com.apps.repository.repository.RefuelRepository
import br.com.apps.repository.repository.RequestRepository
import br.com.apps.repository.repository.StorageRepository
import br.com.apps.repository.repository.TravelRepository
import br.com.apps.repository.repository.UserRepository
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
    single<FleetRepository> { FleetRepository(get()) }
    single<UserRepository> { UserRepository(get()) }
    single<DocumentRepository> { DocumentRepository(get()) }
    single<LabelRepository> { LabelRepository(get()) }
    single<IncomeRepository> { IncomeRepository(get()) }
    single<FineRepository> { FineRepository(get()) }
    single<EmployeeRepository> { EmployeeRepository(get()) }
    single<RequestRepository> { RequestRepository(get()) }
    single<StorageRepository> { StorageRepository(get()) }
    single<TravelRepository> { TravelRepository(get(), get(), get(), get()) }

    single<FreightRepository> { FreightRepository(get()) }
    single<RefuelRepository> { RefuelRepository(get()) }
    single<ExpendRepository> { ExpendRepository(get()) }
    single<LoanRepository> { LoanRepository(get()) }
    single<AdvanceRepository> { AdvanceRepository(get()) }

}

val fireBaseModules = module {
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }
    single<FirebaseStorage> { Firebase.storage }
}
