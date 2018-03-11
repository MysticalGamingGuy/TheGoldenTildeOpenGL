import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack.stackPush
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer

class Texture(imageFileName : String) {

    companion object {
        private var i = 0
        private fun inc() : Int = i++
    }

    private var width: Int = 0
    private var height: Int = 0
    private val textureID : Int
    val pos : Int

    init {
        stbi_set_flip_vertically_on_load(true)
        val file = File("Tutorial2/src/main/resources/$imageFileName")
        if( !file.exists()) throw IOException()

        //Load Texture
        var buffer: ByteBuffer? = null
        stackPush().use {
            val widthBuffer = it.callocInt(1); val heightBuffer = it.callocInt(1); val chanelBuffer = it.callocInt(1)
            buffer = stbi_load(file.toString(), widthBuffer, heightBuffer, chanelBuffer, 0)
            width = widthBuffer.get()
            height = heightBuffer.get()
        }

        if (buffer == null) throw IOException(stbi_failure_reason())

        textureID = glGenTextures()
        pos = Texture.inc()
        GL_TEXTURE_2D.let {
            glActiveTexture(GL_TEXTURE0+pos)
            glBindTexture(it, textureID)
            glTexImage2D(it, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer)
            glGenerateMipmap(it)
            glTexParameteri(it, GL_TEXTURE_WRAP_S, GL_REPEAT)
            glTexParameteri(it, GL_TEXTURE_WRAP_T, GL_REPEAT)
            glTexParameteri(it, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(it, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        }
        stbi_image_free(buffer)
    }

}