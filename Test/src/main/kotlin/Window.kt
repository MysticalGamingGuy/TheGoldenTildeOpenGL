import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import java.awt.Color

internal class Window( private var size : Vector2i, title : String) {

    constructor(x:Int=1200,y:Int=600, title : String) : this(Vector2i(x,y),title)

    private var window = 0L
    var camera = Camera()
    private var shapes = ArrayList<Shape>()
    private val aspect : () -> Float = { size.x/size.y.toFloat() }

    init {

        if (!glfwInit()) throw IllegalStateException("Failed to Initialize GLFW")
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)

        //Create GLFW Window
        window = glfwCreateWindow(size.x, size.y, title, NULL, NULL).also {
            if (it == NULL) throw RuntimeException("Failed to create a GLFW Window")
            glfwSwapInterval(1)
            glfwSetInputMode(it, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
            glfwMakeContextCurrent(it)
            glfwShowWindow(it)

            glfwSetFramebufferSizeCallback(it) { _, w, h ->
                size.set(w,h)
                glViewport(0, 0, w, h)
                camera.aspect = aspect()
            }
        }

        createCapabilities()
        glEnable(GL_DEPTH_TEST)
        setBackgroundColor(Color.black)
    }

    fun start() {
        camera.aspect = aspect()
        glfwSetCursorPosCallback(window,camera.mouseCallbackFirst)
        glfwSetKeyCallback(window) { win, key, scanCode, action, mod ->
            if (action == GLFW_RELEASE && key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(win, true)
            camera.movementCallback(key, action)
        }
        var lastFrame = glfwGetTime()

        while (!glfwWindowShouldClose(window)) {
            val currentFrame = glfwGetTime()
            val delta = (currentFrame - lastFrame).toFloat()
            lastFrame = currentFrame
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            val matrix = camera.update(delta)
            shapes.forEach { it.draw(currentFrame, matrix) }
            glfwSwapBuffers(window)
            glfwPollEvents()
        }

        glfwTerminate()
    }

    fun add(vararg shapes: Shape){
        this.shapes.addAll(shapes)
    }

    fun center(){
        val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(window, (vidMode.width() - size.x) / 2, (vidMode.height() - size.y) / 2)
    }

    fun setBackgroundColor(c: Color) {
        glClearColor(c.red.toFloat(), c.green.toFloat(), c.blue.toFloat(), c.alpha.toFloat())
    }

    fun setBackgroundColor(r:Float, g: Float, b: Float, a: Float = 1f) {
        glClearColor(r,g,b,a)
    }

}
