package de.loosetie.util.wicket.validator

import org.apache.wicket.validation.CompoundValidator
import org.apache.wicket.validation.validator.EmailAddressValidator

class MailValidator : CompoundValidator<String>() {
  init {
    add(EmailAddressValidator.getInstance())
  }
}
