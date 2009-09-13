package net.hamnaberg.recondo.storage

import core.{Vary, Key}
import java.io._
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

private[recondo] class FileManager(baseDirectory: File) {
  val files = FileManager.ensureDirectoryExists(new File(baseDirectory, "files"))

  def createFile(key: Key, stream: InputStream) = {
    val file = resolve(files, key)
    val out = new FileOutputStream(file)
    try {
      StreamUtils.copy(stream, out)
    }
    finally {
      StreamUtils.close(stream)
      StreamUtils.close(out)
    }
    file
  }

  private def resolve(baseDirectory: File, key: Key) = {
    val uriSha = DigestUtils.shaHex(key.uri.toString)
    val uriFolder = FileManager.ensureDirectoryExists(new File(baseDirectory, uriSha))
    val filename = key.vary match {
      case Vary(x) if (x.isEmpty) => "default"
      case x => DigestUtils.shaHex(x.toString)
    }
    new File(uriFolder, filename)
  }
}

private[recondo] object FileManager {
  private def ensureDirectoryExists(directory: File) = {
    if (!directory.exists() && !directory.mkdirs()) {
      throw new IllegalArgumentException(String.format("Directory %s did not exist, and could not be created", directory));
    }
    directory
  }
}

object StreamUtils {
  
  def close(c:Closeable) {
    try {
      c.close
    }
    catch {
      case e:IOException => 
    }
  }

  def using[T <: Closeable](c: T)(f: T => Unit) = {
    try {
      f(c)
    }
    finally {
      close(c)
    }
  }

  def copy(is: InputStream, os: OutputStream) {
    IOUtils.copy(is, os);
  }
}