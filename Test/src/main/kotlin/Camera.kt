import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(private var pos: Vector3f) {

    private val up = Vector3f(0f, 1f, 0f)
    val lookDir = Vector3f(0f, 0f, -1f)
    private val spd = 0.5f
    var fov = 85f
        private set
    private var pitch: Float = 0.toFloat()
    private var yaw = -90f
    private val sensitivity = 0.04f

    private var forward = false
    private var backward = false
    private var left = false
    private var right = false
    var aspect = 0f

    val matrix: Matrix4f
        get() = Matrix4f()
                .perspective(Math.toRadians(fov.toDouble()).toFloat(), aspect, 0.1f, 100f)
                .lookAt(pos, lookDir.add(pos), up)

    constructor(x: Float=0f, y: Float=0f, z: Float=0f) : this(Vector3f(x,y,z))

    fun move(delta: Float) {
        lookDir.set((Math.cos(Math.toRadians(yaw.toDouble())) * Math.cos(Math.toRadians(pitch.toDouble()))).toFloat(),
                Math.sin(Math.toRadians((-pitch).toDouble())).toFloat(),
                (Math.sin(Math.toRadians(yaw.toDouble())) * Math.cos(Math.toRadians(pitch.toDouble()))).toFloat()).normalize()
        val moveDir = (if (forward) 1 else 0) - if (backward) 1 else 0
        val strafeDir = (if (left) 1 else 0) - if (right) 1 else 0
        val cross = Vector3f(up).cross(lookDir).mul(strafeDir.toFloat())
        val move = Vector3f(lookDir).mul(moveDir.toFloat())
        move.add(cross)
        if (moveDir != 0 && strafeDir != 0)
            move.normalize()
        pos!!.add(move.mul(delta * spd))
    }

    fun setForward(forward: Boolean) {
        this.forward = forward
    }

    fun setBackward(backward: Boolean) {
        this.backward = backward
    }

    fun setLeft(left: Boolean) {
        this.left = left
    }

    fun setRight(right: Boolean) {
        this.right = right
    }

    fun addPitch(pitch: Float) {
        this.pitch += pitch * sensitivity
        if (this.pitch > 89.9)
            this.pitch = 89.9f
        else if (this.pitch < -89.9) this.pitch = -89.9f
    }

    fun addYaw(yaw: Float) {
        this.yaw += yaw * sensitivity
    }
}
