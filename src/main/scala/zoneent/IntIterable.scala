package zoneent

import zio.*
import zio.stream.*
import scala.io.Source
import scala.io.BufferedSource

trait IntIterable {
    def newIterable: Task[Iterable[Int]]
}

case class IntIterableLiveTest() extends IntIterable {
  override def newIterable: UIO[Iterable[Int]] = ZIO.succeed(List(1,2,3,4,3))
}

object IntIterableLiveTest extends (() => IntIterable) {
  val layer: ULayer[IntIterable] = IntIterableLiveTest.toLayer
}

case class FileToIterable(filename: String) extends IntIterable {
  override def newIterable: Task[Iterable[Int]] = Task.attempt {
    Source.fromFile(filename).getLines.to(Iterable).map(_.length)
  }
}

case class BSIterable(bs: BufferedSource) extends IntIterable {
  override def newIterable: Task[Iterable[Int]] = Task.attempt {
    bs.getLines.to(Iterable).map(_.length)
  }
}

object BSIterable {
  def bsLayerFromFile(filename: String): ZLayer[Any, Throwable, BufferedSource] = ZManaged
    .fromAutoCloseable(Task.attempt(Source.fromFile(filename)))
    .toLayer

  def layer(filename: String) = bsLayerFromFile(filename) >>> (BSIterable(_)).toLayer
}

object FileToIterable {
  val layer: URLayer[String, IntIterable] = (FileToIterable(_)).toLayer
}