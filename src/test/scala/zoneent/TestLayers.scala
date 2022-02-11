package zoneent

import zio.*
import zio.stream.*
import zio.test.*
import zio.test.Assertion.*
import zio.URLayer
import java.io.FileNotFoundException
import scala.language.postfixOps

object TestLayers extends DefaultRunnableSpec {

  val useIterator = for {
    ii <- ZIO.service[IntIterable]
    gen <- ii.newIterable
  } yield (gen.sum)

  val filename = ZLayer.succeed("README.md")

  val fileRunEffect = useIterator.provideLayer(FileToIterable.layer).provideLayer(filename)

  val fileRunEffect2 = useIterator.provideLayer(BSIterable.layer("README.md"))

  val wrongFileNameEffect = useIterator.provideLayer(BSIterable.layer("RAEDME.md"))

  def spec = suite("Layer tests") (
    test("Unfailing iterator") {
      assertM(useIterator.provideLayer(IntIterableLiveTest.layer))(equalTo(13))
    },
    test("IO iterator") {
      assertM(fileRunEffect)(equalTo(498))
    },
    test("IO2") {
      assertM(fileRunEffect2)(equalTo(498))
    },
    test("Filename misspelling") {
      assertM(wrongFileNameEffect.exit)(fails(anything))
    },
    test("Digit stream") {
      val mystream = ZStream
        .fromIterableZIO(ZIO.service[IntIterable].flatMap(_.newIterable))
      val streamSum = mystream >>> ZSink.sum[Int]
      assertM(streamSum.provide(IntIterableLiveTest.layer))(equalTo(13))
    }
  )

}
