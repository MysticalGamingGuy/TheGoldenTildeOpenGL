import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack.stackPush
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.IntBuffer

internal class Texture(image: String) {

    private var textureID: Int = 0
    private var width: Int = 0
    private var height: Int = 0

    init {
        try {
            val f = File("Test/src/main/resources/" + image)
            if (!f.exists()) throw IOException()

            //Load Texture
            var widthBuffer: IntBuffer? = null
            var heightBuffer: IntBuffer? = null
            var chanelBuffer: IntBuffer
            var buffer: ByteBuffer? = null
            stackPush().use { stack ->
                widthBuffer = stack.callocInt(1)
                heightBuffer = stack.callocInt(1)
                chanelBuffer = stack.callocInt(1)
                buffer = stbi_load(f.toString(), widthBuffer, heightBuffer, chanelBuffer, 0)
            }
            widthBuffer?.let { width = it.get() }
            heightBuffer?.let { height = it.get() }
            if (buffer == null) throw IOException(stbi_failure_reason())

            //Gen & bind Texture
            textureID = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, textureID)

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer)
            glGenerateMipmap(GL_TEXTURE_2D)

            //Setup GL textureID parameters
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

            glBindTexture(GL_TEXTURE_2D, 0)
            stbi_image_free(buffer)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun use(active: Int) {
        glActiveTexture(active)
        glBindTexture(GL_TEXTURE_2D, textureID)
    }

    companion object {
        init {
            stbi_set_flip_vertically_on_load(true)
        }
    }
}
