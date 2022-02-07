package zoneent

import zio.*
import zio.stream.*

object HelloStream extends ZIOAppDefault :

  def run = 
    val data = List(1,2,3,2)
    val myStream = ZStream.fromIterable(data)
    val runStream = myStream >>> ZSink.count
    for
      cnt <- runStream
      _ <- ZIO.debug(cnt)
    yield ()
  
