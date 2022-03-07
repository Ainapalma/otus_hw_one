import org.apache.hadoop.conf._
import org.apache.hadoop.fs._
import java.net.URI

object ReadAndTransformHDFS {

  def main(args: Array[String]): Unit = {

    val conf = new Configuration()
    val fileSystem = FileSystem.get(new URI("hdfs://localhost:9000"), conf)
    val srcPath = "/stage"
    val destPath = "/ods"

    def createFile(filePath: String): Unit = {
      val path = new Path(filePath)
      if (!fileSystem.exists(path)) {
        val out = fileSystem.create(path)
        out.close()
      }
    }

    def removeFolder(fileSystem: FileSystem, folderName: String): Boolean = {
      val path = new Path(folderName)
      fileSystem.delete(path, true)
    }

    val srcDirs = fileSystem.listStatus(new Path(srcPath))
    val mapDirs = srcDirs.filter(_.isDirectory).map(_.getPath)
    mapDirs.foreach { fileStatusPath  => {
      val microDirs = fileSystem.listStatus(fileStatusPath).map(_.getPath.toString.substring(27))

      var checkFirst = true
      var first = "start"

      microDirs.map(f = fileInDir => {
        if (fileInDir.indexOf("csv") > -1) {
          if (checkFirst) {
            createFile(destPath + fileInDir)
            first = fileInDir
            checkFirst = false
          } else {
            val first = fileSystem.listStatus(new Path(destPath + "/" + fileInDir.split("/")(1)))
                                  .map(_.getPath)
                                  .mkString
            val out: FSDataOutputStream = fileSystem.append(new Path(first))
            out.close()
          }
        }
      })
    }}

    removeFolder(fileSystem, srcPath)

    }
}
