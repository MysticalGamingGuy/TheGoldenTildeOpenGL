import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

class Shape(private val Shape: Shapes, private val shader: Shader, pos: Vector3f) {

    constructor(shape: Shapes,shader: Shader, x: Float, y: Float, z: Float) : this(shape,shader, Vector3f(x, y, z))

    private val modelMatrix = Matrix4f().translate(pos)

    fun draw(currentFrame: Double, matrix: Matrix4f) {
        shader.use(currentFrame)
        shader.setMatrix("projection", matrix)
        shader.setMatrix("model", modelMatrix)
        glBindVertexArray(Shape.vao)
        glDrawElements(GL_TRIANGLES, Shape.count, GL_UNSIGNED_INT, 0)
    }

}

enum class Shapes(vertices: FloatArray, indices: IntArray, val count: Int) {
    ICOSAHEDRON(
            floatArrayOf(
                    -0.525731f, 0.000000f, -0.850651f, 0f, 0f,
                    0.525731f, 0.000000f, -0.850651f, 0f, 1f,
                    0.525731f, 0.000000f, 0.850651f, 1f, 0f,
                    -0.525731f, 0.000000f, 0.850651f, 1f, 1f,
                    -0.850651f, -0.525731f, 0.000000f, 0f, 0f,
                    -0.850651f, 0.525731f, 0.000000f, 0f, 1f,
                    0.850651f, 0.525731f, 0.000000f, 1f, 0f,
                    0.850651f, -0.525731f, 0.000000f, 1f, 1f,
                    0.000000f, -0.850651f, 0.525731f, 0f, 0f,
                    0.000000f, -0.850651f, -0.525731f, 0f, 1f,
                    0.000000f, 0.850651f, -0.525731f, 1f, 0f,
                    0.000000f, 0.850651f, 0.525731f, 1f, 1f),
            intArrayOf(
                    2, 10, 1, 11, 2, 1,
                    6, 11, 1, 5, 6, 1,
                    10, 5, 1, 9, 3, 4,
                    5, 9, 4, 6, 5, 4,
                    12, 6, 4, 3, 12, 4,
                    12, 3, 7, 11, 12, 7,
                    2, 11, 7, 8, 2, 7,
                    3, 8, 7, 12, 11, 6,
                    10, 9, 5, 8, 3, 9,
                    10, 8, 9, 2, 8, 10),
            60),
    DODECAHEDRON(
            floatArrayOf(
                    -0.57735f, -0.57735f, 0.57735f, 0f, 1f,
                    0.934172f, 0.356822f, 0f, 1f, 1f,
                    0.934172f, -0.356822f, 0f, 0f, 1f,
                    -0.934172f, 0.356822f, 0f, 1f, 1f,
                    -0.934172f, -0.356822f, 0f, 0f, 1f,
                    0f, 0.934172f, 0.356822f, 1f, 0f,
                    0f, 0.934172f, -0.356822f, 0f, 1f,
                    0.356822f, 0f, -0.934172f, 1f, 0f,
                    -0.356822f, 0f, -0.934172f, 0f, 1f,
                    0f, -0.934172f, -0.356822f, 1f, 1f,
                    0f, -0.934172f, 0.356822f, 0f, 1f,
                    0.356822f, 0f, 0.934172f, 0f, 1f,
                    -0.356822f, 0f, 0.934172f, 0f, 1f,
                    0.57735f, 0.57735f, -0.57735f, 1f, 1f,
                    0.57735f, 0.57735f, 0.57735f, 0f, 1f,
                    -0.57735f, 0.57735f, -0.57735f, 1f, 1f,
                    -0.57735f, 0.57735f, 0.57735f, 0f, 1f,
                    0.57735f, -0.57735f, -0.57735f, 1f, 0f,
                    0.57735f, -0.57735f, 0.57735f, 0f, 1f,
                    -0.57735f, -0.57735f, -0.57735f,1f, 0f
            ),
            intArrayOf(
                    19,3,2, 12,19,2,
                    15,12,2, 8,14,2,
                    18,8,2, 3,18,2,
                    20,5,4, 9,20,4,
                    16,9,4, 13,17,4,
                    1,13,4, 5,1,4,
                    7,16,4, 6,7,4,
                    17,6,4, 6,15,2,
                    7,6,2, 14,7,2,
                    10,18,3, 11,10,3,
                    19,11,3, 11,1,5,
                    10,11,5, 20,10,5,
                    20,9,8, 10,20,8,
                    18,10,8, 9,16,7,
                    8,9,7, 14,8,7,
                    12,15,6, 13,12,6,
                    17,13,6, 13,1,11,
                    12,13,11, 19,12,11
            ),108
    );

    val vao = glGenVertexArrays()

    init {
        for (i in indices.indices) indices[i]--

        val vbo = glGenBuffers()
        val ebo = glGenBuffers()

        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)

        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        val stride = 5 * java.lang.Float.BYTES
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, (3 * java.lang.Float.BYTES).toLong())
        glEnableVertexAttribArray(1)
    }
}