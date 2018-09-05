package uk.gov.ons.sbr.helpers.utils

import java.io.File
import java.nio.file.{Files, Path}

import uk.gov.ons.registers.LogPatch

object TestFileEnvSetup {
  def createTempFile(prefix: String, suffix: String = "_test.csv"): Path = {
    val testPath = Files.createTempFile(prefix, suffix)
    LogPatch.log(msg = s"Temporary file [${testPath.getFileName}] created at path: $testPath")
    testPath
  }

  def createTempDirectory(prefix: String): Path = {
    val testDir = Files.createTempDirectory(prefix)
    LogPatch.log(msg = s"Temporary directory [${testDir.getFileName}] created at parent path: $testDir")
    testDir
  }

  def createAPath(pathStr: String): Path =
    new File(pathStr).toPath
}

