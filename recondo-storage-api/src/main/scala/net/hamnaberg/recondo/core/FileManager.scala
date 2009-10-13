package net.hamnaberg.recondo.storage

import java.io._
import net.hamnaberg.recondo.util._
import org.apache.commons.codec.digest.DigestUtils
import net.hamnaberg.recondo.core.{Vary, Key}

private[recondo] class FileManager(baseDirectory: File) {
  val files = FileManager.ensureDirectoryExists(new File(baseDirectory, "files"))

  def createDataFile(key: Key, stream: InputStream) = {
    val file = resolve(key, true)
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

  def resolve(key: Key, data: Boolean) = {
    val uriSha = DigestUtils.shaHex(key.uri.toString)
    val uriFolder = FileManager.ensureDirectoryExists(new File(files, uriSha))
    val filename = key.vary match {
      case Vary(x) if (x.isEmpty) => "default"
      case x => DigestUtils.shaHex(x.toString)
    }
    if (data) {
      new File(uriFolder, filename + ".data")
    }
    else {
      new File(uriFolder, filename)
    }
  }
}

private[recondo] object FileManager {
  private def ensureDirectoryExists(directory: File) = {
    if (!directory.exists() && !directory.mkdirs()) {
      throw new IllegalArgumentException("Directory %s did not exist, and could not be created".format(directory));
    }
    directory
  }
}