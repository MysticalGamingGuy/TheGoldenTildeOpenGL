import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack.stackPush
import java.nio.file.Files
import java.nio.file.Paths

class Shader(vararg shaderIDS: Shaders) {

    private val shaderProgramID: Int

    init {
        shaderProgramID = glCreateProgram().also {
            shaderIDS.forEach { s -> glAttachShader(it, s.shaderID) }
            glLinkProgram(it)
            checkShaderProgramError(it)
        }
    }

    private fun checkShaderProgramError(shaderProgramID: Int) {
        if (glGetProgrami(shaderProgramID, GL_LINK_STATUS) != GL_TRUE) {
            System.err.println("SHADER PROGRAM LINK FAILED\n${glGetProgramInfoLog(shaderProgramID)}")
            System.exit(-1)
        }
    }

    fun use() = glUseProgram(shaderProgramID)
    fun setFloat(uniformName: String, r: Float, g: Float, b: Float, a: Float) = glUniform4f(glGetUniformLocation(shaderProgramID, uniformName), r, g, b, a)
    fun setFloat(uniformName: String, f: Float) = glUniform1f(glGetUniformLocation(shaderProgramID, uniformName), f)
    fun setMatrix(uniformName: String, matrix: Matrix4f) {
        stackPush().use {
            val floatBuffer = it.callocFloat(16)
            matrix.get(floatBuffer)
            glUniformMatrix4fv(glGetUniformLocation(shaderProgramID, uniformName), false, floatBuffer)
        }
    }

    fun setTexture(texture: Texture) {
        glUniform1i(glGetUniformLocation(shaderProgramID, "texture${texture.pos}"), texture.pos)
    }

}

enum class Shaders(val shaderID: Int) {
    VERTEX(loadShader("vertex.vert")), FRAGMENT(loadShader("fragment.frag"))
}

private fun loadShader(fileName: String): Int {
    val shaderID = glCreateShader(if (fileName.endsWith(".vert")) GL_VERTEX_SHADER else GL_FRAGMENT_SHADER)
    return shaderID.also {
        glShaderSource(it, Files.lines(Paths.get("Tutorial2/src/main/shaders/$fileName")).reduce { s, s1 -> "$s\n$s1 " }.get())
        glCompileShader(it)
        checkShaderError(it)
    }
}

private fun checkShaderError(shaderID: Int) {
    if (glGetShaderi(shaderID, GL_COMPILE_STATUS) != GL_TRUE) {
        System.err.println("SHADER COMPILATION FAILED\n${glGetShaderInfoLog(shaderID)}")
        System.exit(-1)
    }
}
