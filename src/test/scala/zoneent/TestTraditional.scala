package zoneent

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TestTraditional extends AnyFunSuite with Matchers :

  test("Simple thing") {
    1 should be > 0
  }