package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.EntityTags
import com.github.quillraven.fleks.entityTagOf

enum class Tag : EntityTags by entityTagOf() {
    SPAWN_START,
    SPAWN_END,
}