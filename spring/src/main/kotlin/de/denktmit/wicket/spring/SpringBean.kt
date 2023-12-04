package de.denktmit.wicket.spring

import java.io.Serializable

class SpringBean<T>(
    val type: Class<T>,
    val name: String? = null
) : Lazy<T>, Serializable {
    @Transient
    var bean: T? = null

    override val value: T
        get() {
            if (bean == null) {
                bean = initializer()
            }
            return bean!!
        }

    private fun initializer() =
        when {
            name != null -> SpringContextUtil.bean(type, name)
                ?: throw IllegalArgumentException("No bean found for type '$type' and name '$name'")

            else -> SpringContextUtil.bean(type)
                ?: throw IllegalArgumentException("No bean found for type '$type'")
        }

    override fun isInitialized() =
        bean != null
}

/** usage `val service by bean<ServiceType>()` */
inline fun <reified T> bean(name: String? = null) =
    SpringBean(T::class.java, name)
