package ru.stan.nework.domain.models.ui.post

import android.net.Uri
import java.io.File

data class MediaModel(
    var uri: Uri? = null,
    val file: File? = null,
    var type: AttachmentType? = null,
)
