package de.denktmit.wicket.model

import org.apache.wicket.Page
import org.apache.wicket.core.request.mapper.MountedMapper
import org.apache.wicket.protocol.http.WebApplication

annotation class MountPage(
    val path: String,
    val deployment: Boolean = true
)
