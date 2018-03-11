import org.lwjgl.*
import org.lwjgl.glfw.*
import org.lwjgl.opengl.*
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.*
import java.awt.Color


class Window( private val width : Float = 1200f, private val height : Float = 600f, title : String) {

    private val window : Long
    private val shapes = ArrayList<Shape>()
    var camera = Camera()

    init {
        //Init GLFW
        println("Hello LWJGL ${Version.getVersion()}")
        GLFWErrorCallback.createPrint(System.err).set()
        if (!glfwInit()) throw IllegalStateException("Unable to initialize GLFW")

        //Windowing Hints
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        //Create Window, Set Callbacks
        window = glfwCreateWindow(width.toInt(), height.toInt(), title, NULL, NULL)
        if (window == NULL) throw RuntimeException("Failed to create the GLFW window")
        window.let {
            glfwSetKeyCallback(it) { win, key, _, action, _ -> if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(win, true) }
            glfwMakeContextCurrent(it)
            glfwSetInputMode(it, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
            glfwSwapInterval(1)
            glfwShowWindow(window)
        }

        GL.createCapabilities()

        add(
                Shape(0f, 0f, -3f),
                Shape(0f, -3f, 0f),
                Shape(-3f, 0f, 0f),
                Shape(0f, 0f, 3f),
                Shape(0f, 3f, 0f),
                Shape(3f, 0f, 0f)
        )

        val vidMode : GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(window, ((vidMode.width() - width) / 2).toInt(), ((vidMode.height() - height) / 2).toInt())

        glClearColor(.5f, .5f, 0f, 1f)
        glViewport(0, 0, width.toInt(), height.toInt())
        glEnable(GL_DEPTH_TEST)
        camera.aspect = width / height
        //glfwSetCursorPosCallback(window,camera.mouseCallbackFirst)
        val shader = Shader(Shaders.VERTEX, Shaders.FRAGMENT)
        shader.use()


        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            shader.setMatrix("projection", camera.matrix)
            shapes.forEach { it.draw(shader) }
            glfwSwapBuffers(window)
            glfwPollEvents()
        }

        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        glfwTerminate()
        glfwSetErrorCallback(null).free()
    }

    fun center(){

    }

    fun start(loop : ()->Unit) {

    }

    fun add(vararg shapes: Shape){
        this.shapes.addAll(shapes)
    }

    fun setClearColor(r : Float, g : Float, b : Float, a : Float = 1.0f){
        glClearColor(r, g, b, a)
    }

    fun setClearColor(c: Color) {
        glClearColor(c.red.toFloat(), c.green.toFloat(), c.blue.toFloat(), c.alpha.toFloat())
    }

}