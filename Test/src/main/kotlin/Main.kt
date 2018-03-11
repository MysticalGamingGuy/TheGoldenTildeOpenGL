fun main(args: Array<String>) {
    Window(1200,600).run {
        center()
        setBackgroundColor(.2f, .3f, .3f, 1f)
        camera = Camera()
        add(
                Shape(0f, 0f, -3f),
                Shape(0f, -3f, 0f),
                Shape(-3f, 0f, 0f),
                Shape(0f, 0f, 3f),
                Shape(0f, 3f, 0f),
                Shape(3f, 0f, 0f)
        )
        loop()
    }
}