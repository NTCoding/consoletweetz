
import consoletweetz.TweetSystem


object Application {
  val system = TweetSystem()

  def main(args: Array[String]) {
    printInstructionsToConsole()
    println()
    print(">")
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
      print(s"$output")
      if (output != "") print("\n")
      print(">")
      processCommandsUntilQuit(readLine())
  }

  private def printInstructionsToConsole() {
    val instructions = """
    |*************************************************************
    |*************************************************************
    |
    |    Welcome to consoletweetz. The future of social media
    |    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    |
    |     There are six commands:
    |       posting: <user name> -> <message>
    |       reading: <user name>
    |       following: <user name> follows <another user>
    |       wall: <user name> wall
    |       *help*: display this message
    |       *quit*: quit the application and get back to doing some work
    | 
    |     Please tweet responsibly.""".stripMargin('|').trim
    print(instructions)
  }

  private def quitApplication() {
    val quitMessage = """
    |
    |Thankyou for using consoletweetz.
    |Please tell all your friends and make us go viral
    """.stripMargin('|').trim
    println(quitMessage)
  }

}

