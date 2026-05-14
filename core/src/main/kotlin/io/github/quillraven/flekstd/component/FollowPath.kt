package io.github.quillraven.flekstd.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.collections.GdxArray

data class FollowPath(val path: GdxArray<Vector2>) : Component<FollowPath> {
    var currentPathIndex = 1
    var alpha = 0f

    override fun type() = FollowPath

    companion object : ComponentType<FollowPath>()
}