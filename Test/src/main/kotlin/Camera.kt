import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*

class Camera(private var pos: Vector3f, private var spd : Float) {

    private val keys : Array<Boolean> = Array(512) { false }
    private val up = Vector3f(0f, 1f, 0f)
    private val lookDir = Vector3f(0f, 0f, -1f)
    private var fov = Math.toRadians(85.0).toFloat()
    private var pitch = 0f
    private var yaw = -90f
    private val sensitivity = .1f
    private var lastMouse = Vector2f()
    var aspect = 0f

    private val mouseCallback: (w:Long, x:Double, y:Double)->Unit = { _, x, y ->
        mouseUpdate(x,y)
        lastMouse.set(x.toFloat(),y.toFloat())
    }
    val mouseCallbackFirst: (w:Long, x:Double, y:Double)->Unit = { w, x, y ->
        lastMouse.set(x.toFloat(),y.toFloat())
        mouseUpdate(x,y)
        glfwSetCursorPosCallback(w, mouseCallback)
    }

    constructor(x: Float=0f, y: Float=0f, z: Float=0f , spd : Float=2f) : this(Vector3f(x,y,z),spd)

    private fun mouseUpdate(x:Double, y:Double){
        addPitch(y.toFloat() - lastMouse.y)
        addYaw(x.toFloat() - lastMouse.x)
    }

    fun update(delta: Float) : Matrix4f {
        lookDir.set((Math.cos(Math.toRadians(yaw.toDouble())) * Math.cos(Math.toRadians(pitch.toDouble()))).toFloat(),
                Math.sin(Math.toRadians((-pitch).toDouble())).toFloat(),
                (Math.sin(Math.toRadians(yaw.toDouble())) * Math.cos(Math.toRadians(pitch.toDouble()))).toFloat()).normalize()
        val moveDir = (if (keys[GLFW_KEY_W]) 1 else 0) - if (keys[GLFW_KEY_S]) 1 else 0
        val strafeDir = (if (keys[GLFW_KEY_A]) 1 else 0) - if (keys[GLFW_KEY_D]) 1 else 0
        val cross = Vector3f(up).cross(lookDir).mul(strafeDir.toFloat())
        val move = Vector3f(lookDir).mul(moveDir.toFloat())
        move.add(cross)
        if (moveDir != 0 && strafeDir != 0)
            move.normalize()
        pos.add(move.mul(delta * spd))
        return Matrix4f().perspective(fov, aspect, 0.1f, 100f).lookAt(pos, lookDir.add(pos), up)
    }

    private fun addPitch(pitch: Float) {
        this.pitch += pitch * sensitivity
        if (this.pitch > 89.9)
            this.pitch = 89.9f
        else if (this.pitch < -89.9) this.pitch = -89.9f
    }

    private fun addYaw(yaw: Float) {
        this.yaw += yaw * sensitivity
    }

    fun movementCallback(key: Int, action: Int) {
        if (action == GLFW_RELEASE) keys[key] = false else if (action == GLFW_PRESS) keys[key] = true
    }
}
