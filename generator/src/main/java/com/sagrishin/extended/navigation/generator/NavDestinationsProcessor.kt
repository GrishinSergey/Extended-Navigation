package com.sagrishin.extended.navigation.generator

import com.google.auto.service.AutoService
import com.sagrishin.extended.navigation.annotations.NavDestination
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType

@AutoService(Processor::class)
class NavDestinationsProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(NavDestination::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

    override fun process(typeElements: MutableSet<out TypeElement>?, environment: RoundEnvironment): Boolean {
        for (element in environment.getElementsAnnotatedWith(NavDestination::class.java)) {
            if (element is Element && element.enclosingElement is TypeElement) {
                val functionName = element.simpleName.toString()
                val packageName = processingEnv.elementUtils.getPackageOf(element).qualifiedName.toString()

                val types = processingEnv.typeUtils
                val elements = processingEnv.elementUtils
                val composableElement = element.asType() as ExecutableType
                val arguments = "com.sagrishin.extended.navigation.library.Arguments"
                val superType = elements.getTypeElement(arguments).asType()

                val args = composableElement.parameterTypes.find { types.isSubtype(it, superType) }?.toString()

                val destination = TypeSpec.classBuilder("${functionName}Destination")
                    .addSuperinterface(ClassName("com.sagrishin.extended.navigation.library", "Destination"))
                    .addProperty(getDestinationArgsProperty(args))
                    .addProperty(getDestinationRouteProperty())
                    .addFunction(getDestinationComposablePart(functionName, args))
                    .addType(getDestinationCompanionPart(functionName, args))
                    .build()

                val file = FileSpec.builder(packageName, "${functionName}Destination")
                    .addType(destination)
                    .build()

                val kaptGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
                file.writeTo(File(kaptGeneratedDir, "${functionName}Destination.kt"))
            }
        }

        return true
    }

    private fun getDestinationRouteProperty(): PropertySpec {
        return PropertySpec.builder(
            name = "route",
            type = String::class.asTypeName(),
            modifiers = arrayOf(KModifier.OVERRIDE),
        ).run {
            initializer(buildCodeBlock {
                val args = "args.map { \"{\${it.name}}\" }.toTypedArray()"
                add("listOf(Route, *${args}).joinToString(\"/\")")
            })
        }.build()
    }

    private fun getDestinationArgsProperty(argsValue: String?): PropertySpec {
        val listType = ClassName("kotlin.collections", "List")
            .parameterizedBy(ClassName("androidx.navigation", "NamedNavArgument"))

        return PropertySpec.builder(
            name = "args",
            type = listType,
            modifiers = arrayOf(KModifier.OVERRIDE),
        ).run {
            initializer(buildCodeBlock {
                if (argsValue == null) {
                    add("listOf()")
                } else {
                    val packageName = argsValue.split('.').toMutableList().apply { removeLast() }.joinToString(".")
                    val simpleNames = argsValue.split('.').last()
                    val toNavArgument = MemberName("com.sagrishin.extended.navigation.library", "toNavArgument")
                    add("listOf(%T::class.%M())", ClassName(packageName, simpleNames), toNavArgument)
                }
            })
        }.build()
    }

    private fun getDestinationComposablePart(functionName: String, argsValue: String?): FunSpec {
        return FunSpec.builder("Composable")
            .addAnnotation(ClassName("androidx.compose.runtime", "Composable"))
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("navBackStackEntry", ClassName("androidx.navigation","NavBackStackEntry"))
            .addCode(buildCodeBlock {
                when (argsValue == null) {
                    true -> {
                        add("$functionName()")
                    }
                    false -> {
                        val takeNavArgument = MemberName("com.sagrishin.extended.navigation.library", "takeNavArgument")
                        add("$functionName(args = navBackStackEntry.%M())", takeNavArgument)
                    }
                }
            }).build()
    }

    private fun getDestinationCompanionPart(routeName: String, argsValue: String?): TypeSpec {
        val propertySpec = PropertySpec.builder(
            name = "Route",
            type = String::class,
            modifiers = arrayOf(KModifier.CONST, KModifier.INTERNAL),
        ).run {
            initializer("%S", routeName)
        }.build()

        val urlFunctionSpec = FunSpec.builder("url").run {
            if (argsValue == null) {
                this
            } else {
                val packageName = argsValue.split('.').toMutableList().apply { removeLast() }.joinToString(".")
                val simpleNames = argsValue.split('.').last()
                addParameter("args", ClassName(packageName, simpleNames))
            }
        }.addCode(buildCodeBlock {
            if (argsValue == null) {
                add("return \"\$Route\"")
            } else {
                val toJson = MemberName("com.sagrishin.extended.navigation.library.ExtendedNavigationGsonUtils.Companion", "toJson")
                add("return \"\$Route/\${args.%M()}\"", toJson)
            }
        }).returns(String::class).build()

        return TypeSpec.companionObjectBuilder()
            .addProperty(propertySpec)
            .addFunction(urlFunctionSpec)
            .build()
    }

}
