package de.loosetie.util.wicket.spring

import de.loosetie.utils.getOrSet
import java.io.Serializable

class SpringBean<T>(
  val type: Class<T>,
  val name: String? = null,
) : Lazy<T>,
  Serializable {
  @Transient
  var bean: Any? = null

  override val value: T
    get() =
      ::bean.getOrSet {
        when {
          name != null -> SpringContextUtil.bean(type, name)
          else -> SpringContextUtil.bean(type)
        } as? Any ?: throw IllegalArgumentException("No bean found for type '$type' and name '$name'")
      } as T

  override fun isInitialized() = bean != null
}

/** usage `val service by bean<ServiceType>()` */
inline fun <reified T> bean(name: String? = null) = SpringBean(T::class.java, name)
