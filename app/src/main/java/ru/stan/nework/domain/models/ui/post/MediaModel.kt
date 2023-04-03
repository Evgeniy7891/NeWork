package ru.stan.nework.domain.models.ui.post

import android.net.Uri
import java.io.File

data class MediaModel(
    val uri: Uri? = null,
    val file: File? = null,
    val type: AttachmentType? = null,
)
