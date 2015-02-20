package test.aat.walls

import java.lang.Thread
import org.scalatest.{FreeSpec, MustMatchers}
import consoletweetz.TweetSystem

class Tweeters_walls_show_the_tweets_of_the_tweeters_they_follow extends FreeSpec with MustMatchers {

	"If a tweeter has posted some messages" - {
		val system = TweetSystem()
		system.execute("Margaret -> It's a lovely sunny day")
		Thread.sleep(1)
		system.execute("Margaret -> I'm going to relax on the beach all day")

		"And another tweeter followers them" - {
			system.execute("Sarah follows Margaret")

			"When the follower looks at their wall they can see: " - {
				val response = system.execute("Sarah wall")
				info(response)
				val lines = response.split("\n")

				"The followee's tweets chronoligically ordered on the follower's wall with the most recent first" in {
					lines(0) must startWith("Margaret -> I'm going to relax on the beach all day (")
					lines(1) must startWith("Margaret -> It's a lovely sunny day (")
				}

				"A temporal notification showing how long ago each tweet was tweeted" in {
					lines.foreach(_ must endWith ("seconds ago)"))
				}
			}
		}
	}


	"If multiple tweeters have posted some messages" - {
		val system = TweetSystem()
		system.execute("Nico -> I am going to win the world championship this year")
		Thread.sleep(1)
		system.execute("Lewis -> I'll do my talking on the track")
		Thread.sleep(1)
		system.execute("Nico -> My dad is bigger than yours!")

		"And another tweeter follows all of them" - {
			system.execute("Toto follows Lewis")	
			system.execute("Toto follows Nico")	

			"When the follower looks at their wall they can see:" - {
				val response = system.execute("Toto wall")
				info(response)
				val lines = response.split("\n")

				"The follower can see all of followees' tweets chronologically ordered on the follower's wall (even the tweets made before the tweeter began following)" in {
					lines(0) must startWith("Nico -> My dad is bigger than yours!")
					lines(1) must startWith("Lewis -> I'll do my talking on the track")
					lines(2) must startWith("Nico -> I am going to win the world championship this year")
				}	

			}
		}				
	}


	"If a tweeter has posted some messages" - {
		val system = TweetSystem()
		system.execute("SmartDev -> I am going to buy Patterns, Practices and Principles of Domain-Driven Design")
		Thread.sleep(1)
		system.execute("SmartDev -> Order Placed. Yipppeeee")
		Thread.sleep(1)

		"And other tweeters have posted some messages" - {
			system.execute("James -> We are going to limit WIP so that we can finish more work")
			Thread.sleep(1)
			system.execute("Louise -> Can I get some ice cream")

			"And the original tweeter follows the other tweeters" - {
				system.execute("SmartDev follows James")
				Thread.sleep(1)
				system.execute("SmartDev follows Louise")

				"When the original tweeter looks at their wall they can see:" - {
					val response = system.execute("SmartDev wall")
					info(response)
					val lines = response.split("\n")

					"Their own tweets & the tweets of the tweeters they are following's tweets chronoligically sorted" in {
						lines(0) must startWith("Louise -> Can I get some ice cream")
						lines(1) must startWith("James -> We are going to limit WIP so that we can finish more work")
						lines(2) must startWith("SmartDev -> Order Placed. Yipppeeee")
						lines(3) must startWith("SmartDev -> I am going to buy Patterns, Practices and Principles of Domain-Driven Design")
					}

				}
			}
		}
	}

}