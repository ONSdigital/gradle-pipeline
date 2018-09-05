package uk.gov.ons.sbr.service.session

import org.apache.spark.sql.SparkSession

import uk.gov.ons.sbr.logger.SessionLogger

private[service] object SparkSessionManager {
  private[service] val SparkAppName = "Registers Statistical Methods Library (SML)"

  implicit val sparkSession: SparkSession = SparkSession
    .builder()
    .appName(name = SparkAppName)
    .getOrCreate()

  def withMethod(method: SparkSession => Unit): Unit = { activeSession: SparkSession =>
      val sessionName = activeSession.sparkContext.appName
      SessionLogger.log(msg = s"Running method with spark instance [$sessionName]")
      try method
      catch {
        case ex: Exception =>
          //          throw ex(s"Failed to construct DataFrame when running method with error; [${ex.getMessage}]")
          throw new Exception(s"Failed to construct DataFrame when running method with error; [${ex.getMessage}]")
      }
      finally
        SessionLogger.log(msg = s"Stopping active session [$sessionName] started on " +
          s"[${activeSession.sparkContext.startTime}] in thread.")
        activeSession.close
    }
}
