package com.github.linguExplorer.teavm

import com.github.quillraven.fleks.world
import java.io.File
import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder
import com.github.xpenatan.gdx.backends.teavm.config.plugins.TeaReflectionSupplier
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass
import com.github.quillraven.fleks.World

import org.teavm.vm.TeaVMOptimizationLevel

/** Builds the TeaVM/HTML application. */
@SkipClass
object TeaVMBuilder {

    @JvmStatic fun main(arguments: Array<String>) {
        val teaBuildConfiguration = TeaBuildConfiguration().apply {
            assetsPath.add(AssetFileHandle("../assets"))
            webappPath = File("build/dist").canonicalPath
            // Register any extra classpath assets here:


            // additionalAssetsClasspathFiles += "com/github/linguExplorer/asset.extension"

            htmlTitle = "linguExplorer"
            htmlWidth = 640
            htmlHeight = 360
        }
        // Register any classes or packages that require reflection here:

        TeaReflectionSupplier.addReflectionClass(java.util.List::class.java)
        TeaReflectionSupplier.addReflectionClass(java.util.Map::class.java)
        TeaReflectionSupplier.addReflectionClass(java.lang.reflect.Type::class.java)
        TeaReflectionSupplier.addReflectionClass(java.lang.reflect.ParameterizedType::class.java)
        TeaReflectionSupplier.addReflectionClass(java.lang.reflect.Constructor::class.java)
        TeaReflectionSupplier.addReflectionClass(java.lang.reflect.Parameter::class.java)

        TeaReflectionSupplier.addReflectionClass(World::class.java)

        TeaReflectionSupplier.addReflectionClass("com.github.linguExplorer.component")


        val tool = TeaBuilder.config(teaBuildConfiguration)
        tool.mainClass = "com.github.linguExplorer.teavm.TeaVMLauncher"
        TeaBuilder.build(tool)
    }
}
