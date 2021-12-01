lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      scalaVersion := "2.13.6"
    )),
    name := "aoc21-scala"
  )

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.0"
libraryDependencies += "com.disneystreaming" %% "weaver-cats" % "0.7.6" % Test
testFrameworks += new TestFramework("weaver.framework.CatsEffect")
