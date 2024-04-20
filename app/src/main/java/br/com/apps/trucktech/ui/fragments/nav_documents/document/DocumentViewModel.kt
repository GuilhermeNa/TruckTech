package br.com.apps.trucktech.ui.fragments.nav_documents.document

import androidx.lifecycle.ViewModel
import br.com.apps.usecase.DocumentUseCase
import br.com.apps.usecase.StorageUseCase

class DocumentViewModel(
    private val id: String,
    private val documentsUseCase: DocumentUseCase,
    private val storageUseCase: StorageUseCase
): ViewModel() {

    val document = documentsUseCase.getById(id)




}