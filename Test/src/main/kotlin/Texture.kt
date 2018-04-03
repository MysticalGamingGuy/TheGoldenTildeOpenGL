import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack.stackPush
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.IntBuffer

class Texture(image: String) {

    companion object {
        init { stbi_set_flip_vertically_on_load(true) }
    }

    private var width: Int = 0
    private var height: Int = 0
    private var textureID: Int

    init {
        val f = File("Test/src/main/resources/$image")
        if (!f.exists()) throw IOException()

        var widthBuffer: IntBuffer? = null; var heightBuffer: IntBuffer? = null; var chanelBuffer: IntBuffer
        var buffer: ByteBuffer? = null
        stackPush().use {
            widthBuffer = it.callocInt(1); heightBuffer = it.callocInt(1); chanelBuffer = it.callocInt(1)
            buffer = stbi_load(f.toString(), widthBuffer, heightBuffer, chanelBuffer, 0)
        }
        widthBuffer?.let { width = it.get() }
        heightBuffer?.let { height = it.get() }
        if (buffer == null) throw IOException(stbi_failure_reason())

        textureID = glGenTextures()
        GL_TEXTURE_2D.let {
            glActiveTexture(GL_TEXTURE31)
            glBindTexture(it, textureID)
            glTexImage2D(it, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer)
            glGenerateMipmap(it)
            glTexParameteri(it, GL_TEXTURE_WRAP_S, GL_REPEAT)
            glTexParameteri(it, GL_TEXTURE_WRAP_T, GL_REPEAT)
            glTexParameteri(it, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(it, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glBindTexture(it, 0)
            stbi_image_free(buffer)
        }
    }

    fun use(i: Int) {
        if (i !in 0..31) throw Exception("i Not in Range")
        glActiveTexture(GL_TEXTURE0+i)
        glBindTexture(GL_TEXTURE_2D, textureID)
    }
}
