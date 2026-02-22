package project.repit.util

import android.content.Context
import project.repit.model.Routine

object RoutineFileUtil {
    private const val FILE_NAME = "routines.txt"
    private const val FIELD_SEPARATOR = "\t"

    fun readRoutines(context: Context): List<Routine> {
        val file = context.getFileStreamPath(FILE_NAME)
        if (file == null || !file.exists()) {
            val defaultRoutines = createDefaultRoutines()
            saveRoutines(context, defaultRoutines)
            return defaultRoutines
        }

        return file.readLines()
            .filter { it.isNotBlank() }
            .mapNotNull { parseRoutine(it) }
    }

    fun saveRoutines(context: Context, routines: List<Routine>) {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            routines.forEach { routine ->
                writer.appendLine(serializeRoutine(routine))
            }
        }
    }

    private fun createDefaultRoutines(): List<Routine> = listOf(
        Routine(
            name = "Boire 2L d'eau",
            description = "Boire de l'eau régulièrement toute la journée.",
            category = "Santé",
            startAt = "Maintenant",
            endAt = "Ce soir",
            periodicity = "Tous les jours",
            priority = "Élevée"
        ),
        Routine(
            name = "Marcher 20 minutes",
            description = "Faire une marche rapide chaque jour.",
            category = "Sport",
            startAt = "Aujourd'hui",
            endAt = "Aujourd'hui",
            periodicity = "Tous les jours",
            priority = "Moyenne"
        ),
        Routine(
            name = "Dormir avant 23h",
            description = "Couper les écrans et se coucher plus tôt.",
            category = "Bien-être",
            startAt = "Ce soir",
            endAt = "Demain matin",
            periodicity = "Tous les soirs",
            priority = "Faible"
        )
    )

    private fun serializeRoutine(routine: Routine): String = listOf(
        routine.name.escape(),
        routine.description.escape(),
        routine.category.escape(),
        routine.startAt.escape(),
        routine.endAt.escape(),
        routine.periodicity.escape(),
        routine.priority.escape()
    ).joinToString(FIELD_SEPARATOR)

    private fun parseRoutine(line: String): Routine? {
        val parts = line.split(FIELD_SEPARATOR)
        if (parts.size != 7) return null

        return runCatching {
            Routine(
                name = parts[0].unescape(),
                description = parts[1].unescape(),
                category = parts[2].unescape(),
                startAt =  parts[3].unescape(),
                endAt =  parts[4].unescape(),
                periodicity = parts[5].unescape(),
                priority = parts[6].unescape()
            )
        }.getOrNull()
    }

    private fun String.escape(): String = replace("\\", "\\\\").replace("\t", "\\t")

    private fun String.unescape(): String = buildString {
        var index = 0
        while (index < this@unescape.length) {
            val current = this@unescape[index]
            if (current == '\\' && index + 1 < this@unescape.length) {
                val next = this@unescape[index + 1]
                append(if (next == 't') '\t' else next)
                index += 2
            } else {
                append(current)
                index++
            }
        }
    }
}