package de.denktmit.wicket.components.session

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

class DmSessionTest : WidgetTestBase() {

  @Test
  fun `nobody is signed in on a fresh session`() {
    assertThat(DmSession.get().isSignedIn).isFalse()
    assertThat(DmSession.currentUser()).isNull()
  }

  @Test
  fun `sign in binds the session and exposes the user, sign out clears it`() {
    val session = DmSession.get()
    session.signIn(SimpleUser("anna", "Anna", setOf("operator")))
    assertThat(session.isTemporary).isFalse()
    assertThat(DmSession.currentUser()?.displayName).isEqualTo("Anna")
    assertThat(DmSession.currentUser()?.hasRole("operator")).isTrue()
    assertThat(DmSession.currentUser()?.hasRole("admin")).isFalse()

    session.signOut()
    assertThat(session.user).isNull()
  }

  @Test
  fun `locale can be changed for the widgets`() {
    DmSession.get().useLocale(Locale.FRANCE)
    assertThat(DmSession.get().locale).isEqualTo(Locale.FRANCE)
  }
}
