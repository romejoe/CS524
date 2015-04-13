lazy val root = (project in file(".")).
  settings(
    name := "CS524 Project",
    version := "1.0",
    scalaVersion := "2.11.6"
  )

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "1.8.0"
)

