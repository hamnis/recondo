package net.hamnaberg.recondo.storage

import java.io._
import org.apache.commons.codec.digest.DigestUtils
import net.hamnaberg.recondo.core.{Vary, Key}

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