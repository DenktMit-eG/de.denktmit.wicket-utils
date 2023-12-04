package de.denktmit.wicket.spring

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class SpringContextUtil : ApplicationContextAware {

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    companion object {
        lateinit var context: ApplicationContext

        fun <T> bean(type: Class<T>): T =
            context.getBean(type)

        fun <T> bean(type: Class<T>, name: String): T =
            context.getBean(name, type)
    }
}
