package eu.tykki

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.opengl._
import org.slf4j.LoggerFactory
import ShaderUtils._
import Utils._

object Game extends App {
  val logger = LoggerFactory.getLogger(getClass)
  var displayWidth = 1024
  var displayHeight = 640
  val aspectRatio = displayWidth / displayHeight.toFloat
  val displayMode = new DisplayMode(displayWidth, displayHeight)
  val windowTitle = "BestGame"
  val vertex = Array(
    -0.7f, -0.7f, 0.0f,
    0.7f, -0.7f, 0.0f,
    0.0f, 0.7f, 0.0f
  )
  val vertexBuffer = floatArrayToBuffer(vertex)

  var vaoId = 0
  var vboId = 0
  var programId = 0
  var lastFps = 0L
  var fps = 0

  init()
  run()
  quit()

  def init() {
    Display.setTitle(windowTitle)
    Display.setDisplayMode(displayMode)
    Display.setResizable(true)

    lastFps = getTimeInMS

    val pixelFormat = new PixelFormat()
    val contextAttributes = new ContextAttribs(3, 1).withForwardCompatible(true)
    Display.create(pixelFormat, contextAttributes)
    programId = loadShaders("/simple.vert.glsl", "/simple.frag.glsl")
    glUseProgram(programId)

    glClearColor(0f, 0f, 0f, 1f)
    vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)
    vboId = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vboId)
    glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
  }

  def run() {
    while (!Display.isCloseRequested) {
      if (Display.wasResized()) resize()
      glClear(GL_COLOR_BUFFER_BIT)

      glDrawArrays(GL_TRIANGLES, 0, 3)

      Display.update()

      updateFps()
    }
  }

  def resize() {
    displayWidth = Display.getWidth
    displayHeight = Display.getHeight

    var width = 0
    var height = 0
    if (displayWidth / aspectRatio > displayHeight) {
      height = displayHeight
      width = (height * aspectRatio).toInt
    } else {
      width = displayWidth
      height = (width / aspectRatio).toInt
    }

    logger.info(s"Resize. Width: $width Height: $height")

    glViewport((displayWidth - width) / 2, (displayHeight - height) / 2, width, height)
  }

  def updateFps() {
    if (getTimeInMS - lastFps > 1000) {
      Display.setTitle(s"$windowTitle FPS: $fps")
      fps = 0
      lastFps += 1000
    }
    fps = fps + 1
  }

  def quit() {
    Display.destroy()
  }
}
