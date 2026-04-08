package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmWebSocketBehavior
import io.mockk.mockk
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage
import org.apache.wicket.protocol.ws.api.message.TextMessage
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class DmWebSocketBehaviorTest : WicketTestBase() {

  @Test
  fun `constructs with defaults`() {
    val behavior = DmWebSocketBehavior()

    assertThat(behavior).isNotNull
  }

  @Test
  fun `websocket callbacks are invoked when configured`() {
    var connected = false
    var pushed = false
    var messaged = false
    val behavior =
      DmWebSocketBehavior(
        onConnect = { connected = true },
        onPush = { _, _ -> pushed = true },
        onMessageReceived = { _, _ -> messaged = true },
      )

    invokeDeclared(behavior, "onConnect", mockk<ConnectedMessage>(relaxed = true))
    invokeDeclared(behavior, "onPush", mockk<WebSocketRequestHandler>(relaxed = true), mockk<IWebSocketPushMessage>(relaxed = true))
    invokeDeclared(behavior, "onMessage", mockk<WebSocketRequestHandler>(relaxed = true), mockk<TextMessage>(relaxed = true))

    assertThat(connected).isTrue()
    assertThat(pushed).isTrue()
    assertThat(messaged).isTrue()
  }

  @Test
  fun `null callbacks do not throw`() {
    val behavior = DmWebSocketBehavior()

    assertThatCode {
      invokeDeclared(behavior, "onConnect", mockk<ConnectedMessage>(relaxed = true))
      invokeDeclared(behavior, "onPush", mockk<WebSocketRequestHandler>(relaxed = true), mockk<IWebSocketPushMessage>(relaxed = true))
      invokeDeclared(behavior, "onMessage", mockk<WebSocketRequestHandler>(relaxed = true), mockk<TextMessage>(relaxed = true))
    }.doesNotThrowAnyException()
  }
}
