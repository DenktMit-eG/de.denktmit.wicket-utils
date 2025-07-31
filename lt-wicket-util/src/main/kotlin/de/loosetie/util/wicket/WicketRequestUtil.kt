package de.loosetie.util.wicket

import org.apache.wicket.request.Url
import org.apache.wicket.request.cycle.RequestCycle

object WicketRequestUtil {
  fun getHost() =
    RequestCycle
      .get()
      .request
      .url
      .toString(Url.StringMode.FULL)
      .substringAfter("//")
      .substringBefore("/")
}
