package de.denktmit.wicket.components

internal fun invokeDeclared(
  target: Any,
  methodName: String,
  vararg args: Any?,
): Any? {
  var cursor: Class<*>? = target::class.java
  while (cursor != null) {
    val method =
      cursor.declaredMethods.firstOrNull {
        it.name == methodName && it.parameterCount == args.size
      }
    if (method != null) {
      method.isAccessible = true
      return method.invoke(target, *args)
    }
    cursor = cursor.superclass
  }
  throw NoSuchElementException("No method '$methodName' with ${args.size} argument(s) on ${target::class.java.name}")
}
