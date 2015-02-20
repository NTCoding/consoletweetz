package consoletweetz

import scala.math.Ordering
import org.joda.time.DateTime
import consoletweetz.DurationCalculator

object TweetSystem {
  def apply() = new TweetSystem(new DurationCalculator)
}

class TweetSystem(private val durationCalculator: DurationCalculator) {
  private var timelines = Seq.empty[Timeline]
  private var followerSubscriptions = Map[String, Seq[String]]()
  private implicit val dateSorter = new MostRecentFirstDateSorter()

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
    val t = getOrCreateTimelineFor(user) 
    val updatedTimeline = t.copy(tweets = (t.tweets :+ Tweet(message, DateTime.now())))
    // not thread safe
    timelines = timelines.filterNot(_ == t) :+ updatedTimeline
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

  private def showWallFor(username: String) = {
    val usersToShowOnWall = followerSubscriptions.getOrElse(username, Seq.empty) :+ username
    val tweets = usersToShowOnWall.flatMap { username => 
      getOrCreateTimelineFor(username).tweets.map(t => (username, t)) 
    }
    tweets.sortBy(_._2.posted)
          .map(x => toTweetDisplay(x._2, Some(x._1)))
          .mkString("\n")
  }

  private def getOrCreateTimelineFor(username: String) = timelines.find(_.username == username).getOrElse(Timeline(username, Seq.empty))

  private def toTweetDisplay(tweet: Tweet, username: Option[String] = None) = {
    val nameDisplay = username.map(n => s"$n -> ").getOrElse("")
    s"$nameDisplay${tweet.message} (${timeSince(tweet.posted)} ago)"
  }

  private def showTimeline(user: String) = getOrCreateTimelineFor(user).tweets.reverse.map(toTweetDisplay(_)).mkString("\n")

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

case class Timeline(username: String, tweets: Seq[Tweet])
case class Tweet(message: String, posted: DateTime)