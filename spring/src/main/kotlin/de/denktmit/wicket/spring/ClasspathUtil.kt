package de.denktmit.wicket.spring

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.core.type.filter.AssignableTypeFilter
import org.springframework.core.type.filter.TypeFilter

inline fun <reified T> findClasses(
    vararg basePackages: String,
    annotations: List<Class<out Annotation>> = listOf()
): List<Class<T>> {
    return basePackages.flatMap {
        ClassPathScanningCandidateComponentProvider(false).apply {
            addIncludeFilter(AssignableTypeFilter(T::class.java))
            annotations.forEach {
                addExcludeFilter(AnnotationTypeFilter(it).not())
            }
        }.findCandidateComponents(it)
    }
        .toSet()
        .mapNotNull { beanDefinition: BeanDefinition ->
            @Suppress("UNCHECKED_CAST")
            Thread.currentThread().contextClassLoader.loadClass(beanDefinition.beanClassName) as Class<T>
        }
}


fun TypeFilter.not() =
    TypeFilter { metadataReader, metadataReaderFactory -> !this@not.match(metadataReader, metadataReaderFactory) }
