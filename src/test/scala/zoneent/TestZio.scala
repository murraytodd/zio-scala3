package zoneent

import zio.Clock.nanoTime
import zio.Task
import zio.test.Assertion.{equalTo,isGreaterThan}
import zio.test.*
import zoneent.TestZio.suite
import zoneent.TestZio.test

object TestZio extends DefaultRunnableSpec :

  val constEffect = Task(0L)
  val timeTask = Live.live(nanoTime).map { time => println(time); time }

  def spec = suite("clock")(
    test("time is non-zero") {
      assertM(timeTask)(isGreaterThan(0L))
    },
    test("constant Task") {
      assertM(constEffect)(equalTo(0L))
    }
  )