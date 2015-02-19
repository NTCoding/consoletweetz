package consoletweetz

import org.joda.time.DateTime
import consoletweetz.DurationCalculator

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