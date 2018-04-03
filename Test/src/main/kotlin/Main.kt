fun main(args: Array<String>) {

    Window(1200, 600, "Test").run {

        center()
        setBackgroundColor(.2f, .3f, .3f, 1f)
        camera = Camera(spd = 3f)

        val shader = Shader("fragment.frag", "vertex.vert").apply {
            setTexture("icon.png", 0)
            setTexture("face.jpg", 1)
            onDraw = {
                setFloat("amount", (Math.sin(it) / 2 + .5).toFloat())
            }
        }

        /*val shader2 = Shader("fragment.frag", "vertex.vert").apply {
            setTexture("wall.png", 0)
            setTexture("container.jpg", 1)
            onDraw = {
                setFloat("amount", (Math.sin(it) / 2 + .5).toFloat())
            }
        }

        val shader3 = Shader("simpleColour.frag", "vertex.vert").apply {
            onDraw = {
                val mix = (Math.sin(it) / 2 + .5).toFloat()
                setFloat("col", mix, 1 - mix, 0f)
            }
        }*/

        add(
                Shape(Shapes.ICOSAHEDRON, shader, 0f, 0f, -3f),
                Shape(Shapes.ICOSAHEDRON, shader, 0f, -3f, 0f),
                Shape(Shapes.DODECAHEDRON, shader, -3f, 0f, 0f),
                Shape(Shapes.ICOSAHEDRON, shader, 0f, 0f, 3f),
                Shape(Shapes.DODECAHEDRON, shader, 0f, 3f, 0f),
                Shape(Shapes.ICOSAHEDRON, shader, 3f, 0f, 0f)
        /*
        * Shape(Shapes.ICOSAHEDRON, shader, 0f, 0f, -3f),
                Shape(Shapes.ICOSAHEDRON, shader, 0f, -3f, 0f),
                Shape(Shapes.DODECAHEDRON, shader2, -3f, 0f, 0f),
                Shape(Shapes.ICOSAHEDRON, shader2, 0f, 0f, 3f),
                Shape(Shapes.DODECAHEDRON, shader3, 0f, 3f, 0f),
                Shape(Shapes.ICOSAHEDRON, shader3, 3f, 0f, 0f)*/
        )
        start()
    }
}