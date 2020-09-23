#!/usr/bin/env kotlinc -script

@file:DependsOn("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")
@file:DependsOn("com.github.seratch:kotliquery:1.3.0")
@file:DependsOn("org.postgresql:postgresql:42.2.16")

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotliquery.queryOf
import kotliquery.sessionOf
import java.io.File
import java.time.LocalDateTime

val historyFilename = "tabei-history.json"
val dbHost = "ec2-52-73-199-211.compute-1.amazonaws.com"
val dbName = "d9com8hh97ur6v"
val dbUser = "oshrhtdthqodid"
val dbPassword = "[ACTUAL PASSWORD GOES HERE]"

data class HistoryItem(
        val pairingBoardName: String,
        val people: List<Person>,
        val pairingTime: String,
)

data class Person(
        val id: Int,
        val name: String,
)

val objectMapper = jacksonObjectMapper()
val projectId = 1


val historyItems = objectMapper.readValue<List<HistoryItem>>(File(historyFilename))

val dbSession = sessionOf("jdbc:postgresql://$dbHost:5432/$dbName",
        dbUser, dbPassword,
        returnGeneratedKey = true
)

insertPeople()
insertHistoryItems()

fun insertPeople() {
    val people = historyItems
            .flatMap { it.people }
            .distinct()
    val insertQuery = "insert into person (id, name, project_id) values (?, ?, ?)"
    people.forEach { person ->
        println("Inserting person $person")
        dbSession.run(queryOf(insertQuery, person.id, person.name, projectId).asUpdate)
    }
}

fun insertHistoryItems() {
    val insertHistoryQuery = "insert into pairing_history(pairing_board_name, timestamp, project_id) " +
            "values (?, ?, ?)"
    val insertHistoryPerson = "insert into pairing_history_people(pairing_history_id, person_id) values (?, ?)"

    historyItems.forEach { historyItem ->
        val id = dbSession.run(queryOf(insertHistoryQuery,
                historyItem.pairingBoardName,
                formatTimestamp(historyItem.pairingTime),
                projectId
        ).asUpdateAndReturnGeneratedKey)
        historyItem.people.forEach { person ->
            dbSession.run(queryOf(insertHistoryPerson, id, person.id).asUpdate)
        }
        println("Inserted $historyItem")
    }
}

fun formatTimestamp(iso: String): LocalDateTime {
    val trimmed = iso.substring(0, iso.indexOf("+"))
    return LocalDateTime.parse(trimmed)
}
