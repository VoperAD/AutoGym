import org.apache.commons.lang3.Validate

fun main(args: Array<String>) {

    val dni = printAndRead("Introduce tu DNI:")
    val password = printAndRead("Introduce tu contraseña:")

    val day = printAndRead("Elige un día:", *days().toTypedArray())
        .also { isValidOption(it, 1..5) }
        .run { toInt() }

    val reservationHour = printAndRead("Elige una hora:", *hours().toTypedArray())
        .also { isValidOption(it, 1..14) }
        .run { toInt() }

    val maxTries = printAndRead("Número máximo de intentos:")
        .also {
            val int = it.toIntOrNull()
            Validate.isTrue(int != null && int > 0, "El número máximo de intentos debe ser positivo")
        }
        .run { toInt() }

    val userInput = UserInput(dni, password, day, reservationHour, maxTries)

    Navigator(userInput).start()
}

fun printAndRead(vararg print: String): String {
    print.forEach { println(it) }
    return readln()
        .also {
            Validate.notBlank(it, "El valor introducido no puede ser vacío")
            println()
        }
}

fun isValidOption(option: String, range: IntRange) {
    val int = option.toIntOrNull()
    Validate.isTrue(int != null && int in range, "El valor introducido [$option] no es válido.")
}

fun days() = listOf(
    "1 - Lunes",
    "2 - Martes",
    "3 - Miércoles",
    "4 - Jueves",
    "5 - Viernes"
)

fun hours() = listOf(
    "1 - 07:35-08:30",
    "2 - 08:30-09:30",
    "3 - 09:30-10:30",
    "4 - 11:30-12:30",
    "5 - 12:30-13:30",
    "6 - 13:30-14:30",
    "7 - 14:30-15:30",
    "8 - 15:30-16:30",
    "9 - 16:30-17:30",
    "10 - 17:30-18:30",
    "11 - 18:30-19:30",
    "12 - 19:30-20:30",
    "13 - 20:30-21:30",
    "14 - 21:30-22:30"
)
