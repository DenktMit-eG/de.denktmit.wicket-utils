package de.denktmit.wicket.components

import org.apache.wicket.Page
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class WicketTestBase {

  private class TestHomePage : WebPage()

  protected lateinit var tester: WicketTester

  @BeforeEach
  fun setUpWicket() {
    tester =
      WicketTester(
        object : WebApplication() {
          override fun getHomePage(): Class<out Page> = TestHomePage::class.java
        },
      )
  }

  @AfterEach
  fun tearDownWicket() {
    tester.destroy()
  }
}
