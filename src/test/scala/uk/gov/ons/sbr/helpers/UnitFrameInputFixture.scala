package uk.gov.ons.sbr.helpers

trait UnitFrameInputFixture{
  val aFrame = List
  val Properties = List

  val NoValue = ""

  def aFrameHeader(fieldNames: String*): List[String] = fieldNames.toList

  def aUnit(value: String*): List[String] = value.toList

  def aSelectionStrata(value: String*): List[String] = value.toList
}
