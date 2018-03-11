import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(val pos: Vector3f) {

    //private var pitch = 0.0
    //private var yaw = 0.0
    //private val sensitivity = 0.04f

    private val up = Vector3f(0f, 1f, 0f)
    val lookDir = Vector3f(0f,0f,-1f)
    private var fov = Math.toRadians(85.0).toFloat()
    var aspect = 0F
    val matrix = Matrix4f().perspective(fov, aspect, 0.1f, 100f).lookAt(Vector3f(), Vector3f(0f,0f,-1f), up)

    constructor(x: Float, y: Float, z: Float) : this(Vector3f(x,y,z))

    constructor() : this(Vector3f())

    /*

    fun update() {
        lookDir.set((Math.cos(Math.toRadians(yaw.toDouble())) * Math.cos(Math.toRadians(pitch.toDouble()))).toFloat(),
                Math.sin(Math.toRadians((-pitch).toDouble())).toFloat(),
                (Math.sin(Math.toRadians(yaw.toDouble())) * Math.cos(Math.toRadians(pitch.toDouble()))).toFloat()).normalize()
    }

    private fun addPitch(pitch: Double) {
        this.pitch += pitch * sensitivity
        if (this.pitch > 89.9)
            this.pitch = 89.9
        else if (this.pitch < -89.9) this.pitch = -89.9
        //println(this.pitch)
    }

    private fun addYaw(yaw: Double) {
        this.yaw += yaw * sensitivity
    }


    private var lastX = 0.0
    private var lastY = 0.0
    val mouseCallbackFirst = { w:Long, x:Double, y:Double ->
        glfwSetCursorPosCallback(w,mouseCallback)
        lastX = x
        lastY = y
        addPitch(y - lastY)
        addYaw(x - lastX)
    }
    private val mouseCallback = { _:Long, x:Double, y:Double ->
        addPitch(y - lastY)
        addYaw(x - lastX)
        lastX = x
        lastY = y
    }
    */
}
