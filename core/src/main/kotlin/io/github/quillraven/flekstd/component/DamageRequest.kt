package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.EntityRef
import ktx.collections.GdxArray

data class DamageInfo(
    val source: EntityRef,
    val amount: Float,
)

data class DamageRequest(
    val requests: GdxArray<DamageInfo>,
) : Component<DamageRequest> {
    override fun type() = DamageRequest

    companion object : ComponentType<DamageRequest>()
}
