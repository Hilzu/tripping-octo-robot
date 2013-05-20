package eu.tykki

import scala.io.Source
import org.slf4j.LoggerFactory
import java.io.FileNotFoundException
import java.nio.FloatBuffer
import org.lwjgl.BufferUtils

object Utils {
  val logger = LoggerFactory.getLogger(getClass)

  @inline def clamp(x: Float, min: Float, max: Float) = if (x < min) min else if (x > max) max else x

  def loadStringFromFile(path: String): String = {
    val source = Source.fromFile(path)
    val contents = source.mkString
    source.close()
    contents
  }

  def loadStringFromFileInClasspath(path: String): String = {
    val resourceStream = getClass.getResourceAsStream(path)
    if (resourceStream == null) {
      logger.error(s"Resource $path not found from classpath")
      throw new FileNotFoundException(path)
    }
    Source.fromInputStream(resourceStream).mkString
  }

  def getTimeInMS: Long = System.nanoTime() / 1000000

  def floatArrayToBuffer(array: Array[Float]): FloatBuffer = {
    val buffer = BufferUtils.createFloatBuffer(array.length)
    buffer.put(array)
    buffer.flip()
    buffer
  }

}
