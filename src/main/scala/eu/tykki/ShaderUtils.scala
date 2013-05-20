package eu.tykki

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.slf4j.LoggerFactory
import Utils._


object ShaderUtils {
  val logger = LoggerFactory.getLogger(getClass)

  def loadShaders(vertexShaderPath: String, fragmentShaderPath: String): Int = {
    logger.info(s"Compiling shader $vertexShaderPath")
    val vertexShaderId = compileShader(loadStringFromFileInClasspath(vertexShaderPath), GL_VERTEX_SHADER)

    logger.info(s"Compiling shader $fragmentShaderPath")
    val fragmentShaderId = compileShader(loadStringFromFileInClasspath(fragmentShaderPath), GL_FRAGMENT_SHADER)

    logger.info("Linking shader program")
    val programId = linkProgram(vertexShaderId, fragmentShaderId)

    glBindAttribLocation(programId, 0, "verts")

    programId
  }

  def compileShader(shaderSource: String, shaderType: Int): Int = {
    val shaderId = glCreateShader(shaderType)
    glShaderSource(shaderId, shaderSource)
    glCompileShader(shaderId)
    val compileStatus = glGetShader(shaderId, GL_COMPILE_STATUS)
    val infoLogLength = glGetShader(shaderId, GL_INFO_LOG_LENGTH)
    val infoLog = glGetShaderInfoLog(shaderId, infoLogLength)

    if (compileStatus != GL_TRUE) {
      logger.error(s"Shader compilation failed!\n$infoLog")
    } else if (infoLog != "") {
      logger.info(infoLog)
    }
    shaderId
  }

  def linkProgram(shaderId1: Int, shaderId2: Int): Int = {
    val shaderProgramId = glCreateProgram()
    glAttachShader(shaderProgramId, shaderId1)
    glAttachShader(shaderProgramId, shaderId2)
    glLinkProgram(shaderProgramId)

    val linkStatus = glGetProgram(shaderProgramId, GL_LINK_STATUS)
    val infoLogLength = glGetShader(shaderProgramId, GL_INFO_LOG_LENGTH)
    val infoLog = glGetProgramInfoLog(shaderProgramId, infoLogLength)

    if (linkStatus != GL_TRUE) {
      logger.error(s"Linking shader program failed!\n$infoLog")
    } else if (infoLog != "") {
      logger.info(infoLog)
    }

    shaderProgramId
  }
}
