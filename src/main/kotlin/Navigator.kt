import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

const val INTRANET = "https://intranet.upv.es/pls/soalu/est_intranet.ni_Dual?P_IDIOMA=c"

class Navigator(private val userInput: UserInput) {

    private val driver: WebDriver

    private companion object {
        const val DNI = "dni"
        const val PASSWORD = "clau"
        const val LOGIN_INTRANET = "upv_btsubmit"
        const val ACTIVITIES = "Actividades y Escuelas: reserva de plaza semanal e inscripción / Consulta de disponibilidad"
        const val ACTIVITY_TYPE = "tipoact"
        const val TARGET_ACTIVITY_TYPE = "6690"
        const val ACTIVITY = "acti"
        const val TARGET_ACTIVITY = "21229"
        const val AVAILABLE_TAG = "IAOL_BGColorGrpLibre"
        lateinit var RESERVATION: String
    }

    init {
        WebDriverManager.chromedriver().clearDriverCache().setup()
        driver = WebDriverManager.chromedriver().create()

        RESERVATION = "/html/body/div[2]/div/div/div[3]/div[2]/div/table/tbody/tr[${userInput.reservationHour}]/td[${userInput.day + 1}]"
    }

    fun start() {
        intranet()
        login()
        goToActivitiesMenu()
        selectActivity()
        tryToReserve()
        exit()
    }

    private fun intranet() = driver.get(INTRANET)

    private fun login() {
        findElementByName(DNI).sendKeys(userInput.dni)
        findElementByName(PASSWORD).sendKeys(userInput.password)
        driver.findElement(By.className(LOGIN_INTRANET)).submit()
    }

    private fun goToActivitiesMenu() {
        driver.findElement(By.linkText(ACTIVITIES)).click()

    }

    private fun selectActivity() {
        Select(findElementByName(ACTIVITY_TYPE))
            .selectByValue(TARGET_ACTIVITY_TYPE)

        Select(findElementByName(ACTIVITY))
            .selectByValue(TARGET_ACTIVITY)
    }

    private fun tryToReserve() {
        for (i in 1..userInput.maxTries) {
            val wait = WebDriverWait(driver, Duration.ofSeconds(3))
            val hour = driver.findElement(By.xpath(RESERVATION))

            try {
                wait.until {
                    hour
                    .getAttribute("class")
                    .equals(AVAILABLE_TAG, true)
                }
                hour.click()
                println("Reservado con éxito.")
                break
            } catch (ignored: Exception) {
                driver.navigate().refresh()
            }
        }
    }

    private fun exit() {
        driver.quit()
        WebDriverManager.chromedriver().clearDriverCache()
    }



    private fun findElementByName(name: String) = driver.findElement(By.name(name))



}