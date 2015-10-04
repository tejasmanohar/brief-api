name := """brief-back-end"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache,
  javaWs,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  "org.hibernate" % "hibernate-entitymanager" % "4.3.5.Final",
  "org.hibernate" % "hibernate-envers" % "4.3.5.Final",
  "org.hibernate" % "hibernate-core" % "4.3.5.Final",
  "org.hibernate" % "hibernate-ehcache" % "4.3.5.Final",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.0.Final",
  "com.google.inject" % "guice" % "4.0",
  "com.typesafe.play" %% "play-mailer" % "2.4.1",
  "org.twitter4j" % "twitter4j-core" % "4.0.3",
  "com.wordnik" %% "swagger-play2" % "1.3.12" exclude("org.reflections", "reflections"),
  "org.reflections" % "reflections" % "0.9.8" notTransitive (),
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.amazonaws" % "aws-java-sdk" % "1.3.11"
)
