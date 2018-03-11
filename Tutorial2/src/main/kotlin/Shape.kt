import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

class Shape(private val pos : Vector3f){

    constructor(x:Float, y:Float, z:Float) : this(Vector3f(x,y,z))

    private val modelMatrix = Matrix4f().translate(pos)

    companion object {
        private val vertices = floatArrayOf(-0.525731f, 0.000000f, -0.850651f, 0f, 0f, //0
                0.525731f, 0.000000f, -0.850651f,  //1
                0.525731f, 0.000000f, 0.850651f,  //2
                -0.525731f, 0.000000f, 0.850651f,  //3
                -0.850651f, -0.525731f, 0.000000f,  //4
                -0.850651f, 0.525731f, 0.000000f,  //5
                0.850651f, 0.525731f, 0.000000f,  //6
                0.850651f, -0.525731f, 0.000000f, //7
                0.000000f, -0.850651f, 0.525731f,  //8
                0.000000f, -0.850651f, -0.525731f,  //9
                0.000000f, 0.850651f, -0.525731f,  //10
                0.000000f, 0.850651f, 0.525731f) //11

        private val indices = intArrayOf(2, 10, 1, 11, 2, 1, 6, 11, 1, 5, 6, 1, 10, 5, 1, 9, 3, 4, 5, 9, 4, 6, 5, 4, 12, 6, 4, 3, 12, 4, 12, 3, 7, 11, 12, 7, 2, 11, 7, 8, 2, 7, 3, 8, 7, 12, 11, 6, 10, 9, 5, 8, 3, 9, 10, 8, 9, 2, 8, 10)
        private val vao = glGenVertexArrays()
        init {
            for (i in indices.indices) indices[i] --
            //Create vao & vbo & ebo
            val vbo = glGenBuffers()
            val ebo = glGenBuffers()

            //Bind vao & vbo & ebo
            glBindVertexArray(vao)
            glBindBuffer(GL_ARRAY_BUFFER, vbo)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)

            //Modify State
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

            val stride = 3 * java.lang.Float.BYTES
            glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0)
            glEnableVertexAttribArray(0)

            //UnBind
            glBindVertexArray(0) //Unbind the vao first
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        }
    }

    fun draw(shader : Shader){
        shader.setMatrix("model", modelMatrix)
        glBindVertexArray(vao)
        glDrawElements(GL_TRIANGLES, 60, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }


}