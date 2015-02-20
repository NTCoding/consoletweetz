
import consoletweetz.TweetSystem


object Application {
  val system = TweetSystem()

  def main(args: Array[String]) {
    printInstructionsToConsole()
    processCommandsUntilQuit(readLine())
  }

  private def processCommandsUntilQuit(command: String): Unit = command match {
    case "*help*" =>
      printInstructionsToConsole()
      processCommandsUntilQuit(readLine())

    case "*quit*"   =>
      quitApplication()

    case null =>
      quitApplication()

    case anyOtherCommand =>
      val output = system.execute(anyOtherCommand)
      println(output)
      processCommandsUntilQuit(readLine())
  }

  private def printInstructionsToConsole() {
    println("PRINT instructions to console")
  }

  private def quitApplication() {
    println("PRINT quit message")
  }

}

