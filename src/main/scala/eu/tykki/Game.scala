package eu.tykki

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.opengl._
import org.slf4j.LoggerFactory
import ShaderUtils._
import org.lwjgl.BufferUtils

object Game extends App {
  val logger = LoggerFactory.getLogger(getClass)
  val displayMode = new DisplayMode(640, 480)
  val FPS = 60
  val vertices = Array(
    -1.0f, -1.0f, 0.0f,
    1.0f, -1.0f, 0.0f,
    0.0f, 1.0f, 0.0f
  )
  val verticesBuffer = {
    val buffer = BufferUtils.createFloatBuffer(vertices.length)
    buffer.put(vertices)
    buffer.flip()
    buffer
  }

  var vaoId = 0
  var vboId = 0
  var programId = 0

  init()
  run()
  quit()

  def init() {
    logger.info(s"FPS cap: $FPS")

    Display.setTitle("LWJGL Test")
    Display.setDisplayMode(displayMode)

    val pixelFormat = new PixelFormat()
    val contextAttributes = new ContextAttribs(3, 1).withForwardCompatible(true)
    Display.create(pixelFormat, contextAttributes)
    programId = loadShaders("/simple.vert.glsl", "simple.frag.glsl")
    glUseProgram(programId)

    logger.info("OpenGL version: " + glGetString(GL_VERSION))

    glClearColor(0f, 1f, 0f, 1f)
    vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)
    vboId = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vboId)
    glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
  }

  def run() {
    while (!Display.isCloseRequested) {
      glClear(GL_COLOR_BUFFER_BIT)

      glEnableVertexAttribArray(0)
      glBindBuffer(GL_ARRAY_BUFFER, vboId)
      glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
      glDrawArrays(GL_TRIANGLES, 0, 3)
      glDisableVertexAttribArray(0)

      Display.update()
      Display.sync(FPS)
    }
  }

  def quit() {
    Display.destroy()
  }
}
