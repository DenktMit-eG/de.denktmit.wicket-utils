package de.denktmit.wicket.components.collection

import java.io.Serializable

/** A row for the collection tests. */
data class Member(val name: String, val team: String, var rate: Int) : Serializable

/** Twelve members in three teams, unsorted. */
fun members(): List<Member> = listOf(
  Member("Mia", "blue", 120), Member("Ben", "red", 100), Member("Anna", "blue", 130), Member("Zed", "green", 90),
  Member("Carl", "red", 110), Member("Dora", "green", 95), Member("Eve", "blue", 125), Member("Finn", "red", 105),
  Member("Gus", "green", 85), Member("Hana", "blue", 115), Member("Ivo", "red", 100), Member("Jo", "green", 80),
)

fun source(items: List<Member> = members()): ListDataSource<Member, String> = ListDataSource<Member, String>(items)
  .sortBy("name", compareBy { it.name })
  .sortBy("rate", compareBy { it.rate })
