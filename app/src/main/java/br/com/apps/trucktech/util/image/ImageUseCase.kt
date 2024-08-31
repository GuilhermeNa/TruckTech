package br.com.apps.trucktech.util.image

class ImageUseCase {

    companion object {

        const val EDITING_WITHOUT_NEW_IMAGE = 1
        const val EDITING_AND_REPLACING_IMAGE = 2
        const val ADDING_WITHOUT_IMAGE = 3
        const val ADDING_WITH_IMAGE = 4
        const val EDITING_AND_REMOVING_IMAGE = 5
        const val EDITING_AND_INSERTING_FIRST_IMAGE = 6

        fun checkDirection(
            isEditing: Boolean,
            image: Image?,
            hasPreviousImg: Boolean
        ): Int {
            val ba = image?.byteArray
            val url = image?.url

            val hasNoImage = ba == null && url == null
            val hasOnlyUrl = url != null && ba == null
            val hasOnlyBa = ba != null && url == null

            return when {
                isEditing && hasOnlyUrl -> EDITING_WITHOUT_NEW_IMAGE
                isEditing && hasOnlyBa && hasPreviousImg -> EDITING_AND_REPLACING_IMAGE
                !isEditing && hasNoImage -> ADDING_WITHOUT_IMAGE
                !isEditing && hasOnlyBa -> ADDING_WITH_IMAGE
                isEditing && hasNoImage && hasPreviousImg -> EDITING_AND_REMOVING_IMAGE
                isEditing && hasOnlyBa && !hasPreviousImg -> EDITING_AND_INSERTING_FIRST_IMAGE

                else -> 0
            }
        }
    }


}