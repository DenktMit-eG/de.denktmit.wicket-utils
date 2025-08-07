package de.denktmit.wicket.components.page

import org.apache.wicket.request.mapper.parameter.PageParameters
import java.io.Serializable

interface PageParams : Serializable {
  val pp: PageParameters
}
