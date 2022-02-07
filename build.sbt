import sbt.Keys.testFrameworks

val scala3Version = "3.1.1"
val zioVersion = "2.0.0-RC2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-with-zio-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,
    
    libraryDependencies ++= Seq(
    //  "com.novocode" % "junit-interface" % "0.11" % Test, // if you need (Java) JUnit tests
      "org.scalatest" %% "scalatest" % "3.2.9", // if you need to also have (non-ZIO) Scalatest tests
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-streams" % zioVersion,
      "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
      "dev.zio" %% "zio-test" % zioVersion % Test
    ),

    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
