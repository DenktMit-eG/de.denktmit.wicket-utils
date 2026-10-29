package de.denktmit.wicket.components.input

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HtmlSanitizerTest {

  @Test
  fun `allowed formatting passes without attributes`() {
    assertThat(HtmlSanitizer.sanitize("<p class=\"x\">Hello <strong>bold</strong> and <em>italic</em><br/></p><ul><li>one</li></ul>"))
      .isEqualTo("<p>Hello <strong>bold</strong> and <em>italic</em><br></p><ul><li>one</li></ul>")
  }

  @Test
  fun `scripts, styles, comments, event handlers and unknown tags go`() {
    assertThat(HtmlSanitizer.sanitize("<script>alert(1)</script><p onclick=\"x()\">a</p><!-- c --><img src=x onerror=alert(1)><div>text</div><style>p{}</style>"))
      .isEqualTo("<p>a</p>text")
  }

  @Test
  fun `links keep a safe href only`() {
    assertThat(HtmlSanitizer.sanitize("<a href=\"https://denktmit.de\" target=\"_blank\">site</a>")).isEqualTo("<a href=\"https://denktmit.de\" rel=\"noopener\">site</a>")
    assertThat(HtmlSanitizer.sanitize("<a href='javascript:alert(1)'>x</a>")).isEqualTo("<a>x</a>")
    assertThat(HtmlSanitizer.sanitize("<a href=mailto:a@b.c>m</a>")).isEqualTo("<a href=\"mailto:a@b.c\" rel=\"noopener\">m</a>")
  }

  @Test
  fun `stray angle brackets and entities`() {
    assertThat(HtmlSanitizer.sanitize("1 < 2 &amp; 3 > 2 <p>x</p>")).isEqualTo("1 &lt; 2 &amp; 3 > 2 <p>x</p>")
    assertThat(HtmlSanitizer.sanitize(null)).isNull()
    assertThat(HtmlSanitizer.sanitize("")).isEqualTo("")
  }
}
