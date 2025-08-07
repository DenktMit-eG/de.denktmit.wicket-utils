package de.denktmit.wicket.components.component

import org.apache.wicket.protocol.ws.api.WebSocketBehavior
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage
import org.apache.wicket.protocol.ws.api.message.TextMessage

class DmWebSocketBehavior(
  @Transient
  var onConnect: ((ConnectedMessage) -> Unit)? = null,
  @Transient
  var onPush: ((WebSocketRequestHandler, IWebSocketPushMessage) -> Unit)? = null,
  @Transient
  var onMessageReceived: ((WebSocketRequestHandler, TextMessage) -> Unit)? = null,
) : WebSocketBehavior() {
  override fun onPush(
    handler: WebSocketRequestHandler,
    message: IWebSocketPushMessage,
  ) {
    onPush?.let {
      it(handler, message)
    }
  }

  override fun onMessage(
    handler: WebSocketRequestHandler,
    message: TextMessage,
  ) {
    onMessageReceived?.let {
      it(handler, message)
    }
  }

  override fun onConnect(message: ConnectedMessage) {
    onConnect?.let { it(message) }
  }
}
