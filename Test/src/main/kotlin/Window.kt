import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.GL_TEXTURE1
import org.lwjgl.system.MemoryUtil.NULL
import java.awt.Color

internal class Window( private var size : Vector2i) {

    constructor(x:Int=1200,y:Int=600) : this(Vector2i(x,y))

    private var lastX = 0f
    private var lastY = 0f
    private var firstMouse = true
    private var window = 0L
    var camera = Camera()
    private var shapes = ArrayList<Shape>()
    private val aspect : () -> Float = {size.x.toFloat()/size.y.toFloat()}

    init {

        //Init GLFW
        if (!glfwInit()) throw IllegalStateException("Failed to Initialize GLFW")

        // Setup Window Hints
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)

        //Create GLFW Window
        window = glfwCreateWindow(size.x, size.y, "LearnOpenGL", NULL, NULL)
        if (window == 0L) throw RuntimeException("Failed to create a GLFW Window")

        glfwSwapInterval(1)
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        glfwMakeContextCurrent(window)
        glfwShowWindow(window)

        //Callbacks
        glfwSetCursorPosCallback(window) { _, x, y ->
            if (firstMouse) {
                lastX = x.toFloat()
                lastY = y.toFloat()
                firstMouse = false
            }
            camera.addPitch((y - lastY).toFloat())
            camera.addYaw((x - lastX).toFloat())
            lastX = x.toFloat()
            lastY = y.toFloat()
        }

        glfwSetFramebufferSizeCallback(window) { _, w, h ->
            size.x = w
            size.y = h
            glViewport(0, 0, size.x, size.y )
            camera.aspect = aspect()
        }

        glfwSetKeyCallback(window) { _, key, _, action, _ ->
            if (action == GLFW_RELEASE) {
                when (key) {
                    GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose(window, true)
                    GLFW_KEY_W -> camera.setForward(false)
                    GLFW_KEY_A -> camera.setLeft(false)
                    GLFW_KEY_S -> camera.setBackward(false)
                    GLFW_KEY_D -> camera.setRight(false)
                }
            } else if (action == GLFW_PRESS) {
                when (key) {
                    GLFW_KEY_W -> camera.setForward(true)
                    GLFW_KEY_A -> camera.setLeft(true)
                    GLFW_KEY_S -> camera.setBackward(true)
                    GLFW_KEY_D -> camera.setRight(true)
                }
            }
        }

        //Link OpenGL with GLFW, Now you can call GL functions
        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        setBackgroundColor(Color.black)
    }

    fun loop() {

        camera.aspect = aspect()

        //Shaders and Textures
        val shader = Shader(Shaders.VERTEX,Shaders.FRAGMENT)
        shader.use()
        val t1 = Texture("wall.png")
        val t2 = Texture("container.jpg")
        t1.use(GL_TEXTURE0)
        t2.use(GL_TEXTURE1)
        shader.setInt("texture1", 0)
        shader.setInt("texture2", 1)

        var lastFrame = glfwGetTime()
        while (!glfwWindowShouldClose(window)) {
            val currentFrame = glfwGetTime().toFloat()
            val delta = (currentFrame - lastFrame).toFloat()
            lastFrame = currentFrame.toDouble()
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            shapes.forEach { it.draw(shader,camera, currentFrame, delta) }
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
