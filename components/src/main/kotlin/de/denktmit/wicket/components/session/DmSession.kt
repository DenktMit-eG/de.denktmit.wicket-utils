package de.denktmit.wicket.components.session

import org.apache.wicket.Session
import org.apache.wicket.protocol.http.WebSession
import org.apache.wicket.request.Request
import java.io.Serializable
import java.util.Locale

/** The signed-in user as every widget sees it (requirement K.104): a name, a display name and roles, nothing more. */
interface DmUser : Serializable {
  /** The stable user name (the key the application addresses the user by). */
  val name: String

  /** What the page shows for the user. */
  val displayName: String get() = name

  /** The roles the user holds; the application decides what they mean. */
  val roles: Set<String> get() = emptySet()

  /** Whether the user holds [role]. */
  fun hasRole(role: String): Boolean = role in roles
}

/** A plain user value for applications that carry nothing else. */
data class SimpleUser(
  override val name: String,
  override val displayName: String = name,
  override val roles: Set<String> = emptySet(),
) : DmUser

/**
 * The session the toolbox's widgets read the signed-in user and the locale from (requirement K.104). An application
 * returns it (or a subclass) from `WebApplication.newSession`; [signIn] binds the session so the user survives the
 * request, [signOut] invalidates it. Widgets reach it through [DmSession.get]; where the application does not use
 * it, [DmSession.current] is `null` and widgets fall back to the plain Wicket session.
 */
open class DmSession(request: Request) : WebSession(request) {

  /** The signed-in user, `null` when nobody is. */
  var user: DmUser? = null
    private set

  /** Whether a user is signed in. */
  val isSignedIn: Boolean get() = user != null

  /** Signs [user] in and binds the session. */
  fun signIn(user: DmUser) {
    this.user = user
    bind()
  }

  /** Signs the user out and invalidates the session at the end of the request. */
  fun signOut() {
    user = null
    invalidate()
  }

  /** Changes the locale the widgets render with. */
  fun useLocale(locale: Locale) {
    setLocale(locale)
  }

  companion object {
    /** The current session as a [DmSession]; throws when the application uses another session class. */
    @JvmStatic
    fun get(): DmSession = Session.get() as DmSession

    /** The current session as a [DmSession], or `null` when the application uses another session class. */
    @JvmStatic
    fun current(): DmSession? = if (Session.exists()) Session.get() as? DmSession else null

    /** The signed-in user of the current session, `null` without one. */
    @JvmStatic
    fun currentUser(): DmUser? = current()?.user
  }
}
