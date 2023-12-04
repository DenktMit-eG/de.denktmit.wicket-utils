# wicket-utils

Utils for [Apache Wicket](https://wicket.apache.org/) development with [Kotlin](https://kotlinlang.org/)

## Features

* Annotation based mounts
  ```kotlin
  @MountPage("some/path")
  class MyPage: WebPage { ... }
  
  class MyApplication: WebApplication {
     override fun init() {
      // ...
      mountAnnotatedPages()
    }
  }
  ```

* DSL-like components
  ```kotlin
  class MyPage: Page {
    override fun onInitialize() {
      +SomeComponent("id") {
        // configure component ...
      }         
    }
  }
  ```

* Models for Lambdas and properties
  ```kotlin
  modelOf { "getter" }
  modelOf( { getter() }, { setter(it) })
  modelOf( MyObject::name )
  modelOf( MyObject::birthdate, LocalDate::year )
  ```

* PageParameters by delegates
  ```kotlin
  data class MyParams(
    val pp: PageParameters
  ) {
    var name: String by pp
  }
  ```
* Typesafe PageParameters
  ```kotlin
  bookmarkablePageLink.apply {
    param(MyParams::name, "value")
  }
  component.setResponsePage(MyPage::class.java, myParams.copy()) {
    name = "value"
  }
  ```

* Spring beans by delegate
  ```kotlin
  class MyComponent() {
    val bean: BeanType by bean()
    val bean: BeanType by bean("name")
  }
  ```

* easy CSS class handling
  ```kotlin
  component.addCssClass("a", "b", classNameAsCssClass())
  component.removeCssClass("c")
  ```