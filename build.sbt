lazy val common = project
lazy val rb_001_messaging = project.dependsOn(common % "test->test;compile->compile")
lazy val rb_002_elasticity = project.dependsOn(common % "test->test;compile->compile")
lazy val rb_003_faulty = project.dependsOn(common % "test->test;compile->compile")
lazy val rb_004_resilience = project.dependsOn(common % "test->test;compile->compile")

lazy val rb_005_remoting_catalog = project.dependsOn(common % "test->test;compile->compile")

lazy val rb_006_remoting_customer = project.dependsOn(
  common % "test->test;compile->compile",
  rb_005_remoting_catalog % "compile->compile")

lazy val rb_007_remoting_rarebooks = project.dependsOn(
  common % "test->test;compile->compile",
  rb_005_remoting_catalog % "compile->compile")
