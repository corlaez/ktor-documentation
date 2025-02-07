import e2e.WithTestServer
import e2e.defaultServer
import e2e.readString
import e2e.runGradleAppWaiting
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.junit.*
import org.junit.Assert.assertEquals

class ApplicationTest: WithTestServer() {
    override val server = defaultServer {
        routing {
            post("/upload") {
                val data = call.receiveMultipart()
                val descriptionPart = data.readPart() as PartData.FormItem
                val filePart = data.readPart() as PartData.FileItem

                call.respondText {
                    "${descriptionPart.name}:${descriptionPart.value}," +
                        "${filePart.name}:[${filePart.contentType}][${filePart.contentDisposition}]"
                }
            }
        }
    }

    @Test
    @Ignore
    fun clientSendsMultipartData() {
        val output = runGradleAppWaiting().inputStream.readString()
        assertEquals(
            output,
            "description:Ktor logo,image:[image/png][form-data; name=image; filename=ktor_logo.png]\n"
        )
    }
}