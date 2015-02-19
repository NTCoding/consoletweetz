package test.aat.timelines

import org.scalatest.FreeSpec 
import org.scalatest.MustMatchers
import scala.util.matching.Regex

class Tweeters_can_post_to_their_timelines extends FreeSpec with MustMatchers {
  val system = TweetSystem()

  "When a new tweeter posts messages for the first time" - {
    system.execute("Sandro -> I looooooove clean code")
    system.execute("Sandro -> I haaaaaate spaghetti code")
    val timeline = system.execute("Sandro")

    "Their account is created and they can see their messages on their timeline" in {
      val lines = timeline.split("\n")
      lines.length must equal(2)
      lines(0) must startWith("I looooooove clean code")
      lines(1) must startWith("I haaaaaate spaghetti code")
    } 

    "Each message has a temporal notification indicating how long ago the message was posted" in {
      val pattern = new Regex("\\(\\d (second|seconds) ago\\)")
      timeline.split("\n").foreach { line =>
        pattern.findAllIn(line).length must equal(1)
      }   
    }
  }

}

object TweetSystem {
  def apply() = new TweetSystem()
}

class TweetSystem {

  def execute(command: String): String = {
    ""
  }
}