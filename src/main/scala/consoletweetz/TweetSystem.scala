package consoletweetz

import scala.math.Ordering
import org.joda.time.DateTime
import consoletweetz.DurationCalculator

object TweetSystem {
  def apply() = new TweetSystem(new DurationCalculator)
}

class TweetSystem(private val durationCalculator: DurationCalculator) {
  private var timelines = Map[String, Seq[(String, DateTime)]]()
  private var followerSubscriptions = Map[String, Seq[String]]()
  private val dateSorter = new MostRecentFirstDateSorter()

  def execute(command: String): String = {
    if (command.contains("->"))
      processTweet(command)
    else 
      processNonTweet(command)
  }

  private def processTweet(command: String) = {
    command.split("->").toSeq.map(_.trim) match {
      case Seq(user, message) =>
      updateTimeline(user, message)
    }
  }

  private def updateTimeline(user: String, message: String) = {
    val tl = timelines.getOrElse(user, Seq.empty)
    // not thread safe
    timelines =  (timelines - user).updated(user, tl :+ (message, DateTime.now()))
    ""
  }

  private def processNonTweet(command: String) = {
    command.split(" ").toSeq.map(_.trim) match {
      case Seq(follower, "follows", followee) =>
        registerFollower(follower, followee)  
      case Seq(tweeter, "wall") =>
        showWallFor(tweeter)
      case Seq(user) =>
       showTimeline(user)
    }
  }

  private def showWallFor(tweeter: String) = {
    val usersToShowOnWall = followerSubscriptions.getOrElse(tweeter, Seq.empty) :+ tweeter
    val tweets = usersToShowOnWall.flatMap { username => 
      timelines.getOrElse(username, Seq.empty).map(tl => (username, tl._1, tl._2)) 
    }
    val sorted = tweets.sortBy(_._3)(dateSorter)
    val wallLines = sorted.map(x => toTweetDisplay(x._2, x._3, Some(x._1)))
    wallLines.mkString("\n")
  }

  private def toTweetDisplay(line: String, dt: DateTime, name: Option[String] = None) = {
    val nameDisplay = name.map(n => s"$n -> ").getOrElse("")
    s"$nameDisplay$line (${timeSince(dt)} ago)"
  }

  private def showTimeline(user: String) = {
    timelines(user).reverse.map { 
      case (line: String, dt: DateTime) =>
      toTweetDisplay(line, dt)
    } mkString("\n")
  }

  private def timeSince(dt: DateTime) = durationCalculator.calculate(dt, DateTime.now())

  private def registerFollower(follower: String, followee: String) = {
    val subs = followerSubscriptions.getOrElse(follower, Seq.empty)
    followerSubscriptions = (followerSubscriptions - follower).updated(follower, subs :+ followee)
    ""
  }

}

class MostRecentFirstDateSorter extends Ordering[DateTime] {
  
  def compare(x: DateTime, y: DateTime) = {
    if (x.isBefore(y)) 1
    else if (y.isBefore(x)) -1
    else 0
  }
}