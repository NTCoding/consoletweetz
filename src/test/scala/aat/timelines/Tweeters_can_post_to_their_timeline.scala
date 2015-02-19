package test.aat.timelines

import org.scalatest.FreeSpec 
import org.scalatest.MustMatchers
import scala.util.matching.Regex
import scala.collection.JavaConverters._
import org.joda.time.DateTime

class Tweeters_can_post_to_their_timelines extends FreeSpec with MustMatchers {
  val system = TweetSystem()

  "When a new tweeter posts messages for the first time" - {
    system.execute("Sandro -> I looooooove clean code")
    system.execute("Sandro -> I haaaaaate spaghetti code")
    val timeline = system.execute("Sandro")
    info(s"Got timeline: \n$timeline")

    "Their account is created and they can see their messages on their timeline" ignore {
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
  def apply() = new TweetSystem(new DurationCalculator)
}

class TweetSystem(private val durationCalculator: DurationCalculator) {
  private var timelines = Map[String, Seq[(String, DateTime)]]()

  def execute(command: String): String = {
    command.split("->").toSeq.map(_.trim) match {
      case Seq(user, message) =>
        updateTimeline(user, message)
      case Seq(user) =>
        showTimeline(user)
    }
  }

  private def updateTimeline(user: String, message: String) = {
    val tl = timelines.getOrElse(user, Seq.empty)
    // not thread safe
    timelines =  (timelines - user).updated(user, tl :+ (message, DateTime.now()))
    ""
  }

  private def showTimeline(user: String) = {
    timelines(user).map { 
      case (line: String, dt: DateTime) =>
        s"$line (${timeSince(dt)} ago)"
    } mkString("\n")
  }

  private def timeSince(dt: DateTime) = durationCalculator.calculate(dt, DateTime.now())
}

class DurationCalculator {

  def calculate(start: DateTime, end: DateTime): String = {
    ""
  }

}