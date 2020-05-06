import Dependencies._

ThisBuild / scalaVersion     := "2.12.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.maroon297"
ThisBuild / organizationName := "maroon297"

lazy val core = (project in file("core"))
  .dependsOn(codegen)
  .enablePlugins(ScalikejdbcPlugin)
  .settings(
    name := "core",
    libraryDependencies ++= {
      Seq(
        scalaTest % Test,
        "org.scalikejdbc" %% "scalikejdbc"       % "3.4.1",
        "org.scalikejdbc" %% "scalikejdbc-config"  % "3.4.1",
        "org.scalikejdbc" %% "scalikejdbc-test" % "3.4.1" % Test,
        "ch.qos.logback"  %  "logback-classic"   % "1.2.3",
         "mysql" % "mysql-connector-java" % "8.0.19",
        "com.typesafe.akka" %% "akka-stream" % "2.6.4",
        "com.typesafe.akka" %% "akka-http" % "10.1.11")
    }
  )

lazy val codegen = (project in file("codegen"))
    .settings(
      name := "codegen",
      libraryDependencies ++= {
        val circeVersion = "0.13.0"
        Seq(
          "com.typesafe.akka" %% "akka-stream" % "2.6.4",
          "com.typesafe.akka" %% "akka-http" % "10.1.11",
          "io.circe" %% "circe-core" % circeVersion,
          "io.circe" %% "circe-generic" % circeVersion,
          "io.circe" %% "circe-java8" % "0.11.0",
          "io.circe" %% "circe-parser" % circeVersion,
          "org.typelevel" %% "cats-core" % "2.1.1")
      },
      guardrailTasks in Compile := List(
          ScalaServer(file("codegen/api.yaml"), pkg="com.maroon297.codegen")
        )
    )
