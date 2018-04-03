import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack.stackPush
import java.nio.file.Files
import java.nio.file.Paths

class Shader(vararg shaderFileNames: String) {

    private val shaderProgramID: Int
    private val textures: HashMap<Int, Texture> = HashMap()
    var onDraw : (currentFrame: Double) -> Unit = {}

    init {
        shaderProgramID = glCreateProgram().also {
            shaderFileNames.forEach { s -> glAttachShader(it, loadShader(s)) }
            glLinkProgram(it)
            checkShaderProgramError(it)
            glUseProgram(it)
        }
    }

    private fun loadShader(fileName: String): Int {
        val type = when (fileName.split(".")[1]) {
            "vert" -> GL_VERTEX_SHADER
            "frag" -> GL_FRAGMENT_SHADER
            else -> throw RuntimeException("Shader Extension Cannot be determined")
        }
        return glCreateShader(type).also {
            glShaderSource(it, Files.lines(Paths.get("Test/src/main/shaders/$fileName")).reduce { s, s1 -> "$s\n$s1 " }.get())
            glCompileShader(it)
            checkShaderError(it)
        }
    }

    private fun checkShaderProgramError(shaderProgramID: Int) {
        if (glGetProgrami(shaderProgramID, GL_LINK_STATUS) != GL_TRUE) {
            System.err.println("SHADER PROGRAM LINK FAILED\n${glGetProgramInfoLog(shaderProgramID)}")
            System.exit(-1)
        }
    }

    private fun checkShaderError(shaderID: Int) {
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) != GL_TRUE) {
            System.err.println("SHADER COMPILATION FAILED\n${glGetShaderInfoLog(shaderID)}")
            System.exit(-1)
        }
    }

    fun use(currentFrame: Double) {
        glUseProgram(shaderProgramID)
        textures.forEach { set ->
            set.value.use(set.key)
            setInt("texture${set.key}",set.key)
        }
        onDraw(currentFrame)
    }

    fun setFloat(uniformName: String, r: Float, g: Float, b: Float, a: Float) = glUniform4f(getUniform(uniformName), r, g, b, a)

    fun setFloat(uniformName: String, r: Float, g: Float, b: Float) = glUniform3f(getUniform(uniformName), r, g, b)

    fun setFloat(uniformName: String, f: Float) = glUniform1f(getUniform(uniformName), f)

    fun setInt(s: String, i: Int) = glUniform1i(getUniform(s), i)

    fun setMatrix(uniformName: String, matrix: Matrix4f) {
        stackPush().use {
            val floatBuffer = it.callocFloat(16)
            matrix.get(floatBuffer)
            glUniformMatrix4fv(getUniform(uniformName), false, floatBuffer)
        }
    }

    private fun getUniform(s:String) = glGetUniformLocation(shaderProgramID, s)

    fun setTexture(t: Texture, i: Int) {
        textures[i] = t
    }

    fun setTexture(s: String, i: Int) {
        setTexture(Texture(s),i)
    }

}


