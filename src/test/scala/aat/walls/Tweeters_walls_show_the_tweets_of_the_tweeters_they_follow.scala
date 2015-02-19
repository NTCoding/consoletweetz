package test.aat.walls

import org.scalatest.{FreeSpec, MustMatchers}
import consoletweetz.TweetSystem

class Tweeters_walls_show_the_tweets_of_the_tweeters_they_follow extends FreeSpec with MustMatchers {

	"If a tweeter has posted some messages" - {
		val system = TweetSystem()
		system.execute("Margaret -> It's a lovely sunny day")
		system.execute("Margaret -> I'm going to relax on the beach all day")

		"And another tweeter followers them" - {
			system.execute("Sarah follows Margaret")

			"When the follower looks at their wall they can see: " - {
				val response = system.execute("Sarah wall")
				val lines = response.split("\n")

				"The followee's tweets chronoligically ordered on the follower's wall" in {
					lines(0) must startWith("Margaret -> It's a lovely sunny day (")
						lines(1) must startWith("Margaret -> I'm going to relax on the beach all day (")
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
			system.execute("Lewis -> I'll do my talking on the track")
			system.execute("Nico -> My dad is bigger than yours!")

			"And another tweeter follows all of them" - {
				system.execute("Toto follows Lewis")	
				system.execute("Toto follows Nico")	

				"When the follower looks at their wall they can see:" - {
					val response = system.execute("Toto wall")
					val lines = response.split("\n")

					"The follower can see all of followees' tweets chronologically ordered on the follower's wall (even the tweets made before the tweeter began following)" in {
						lines(0) must startWith("Nico -> I am going to win the world championship this year")
						lines(1) must startWith("Lewis -> I'll do my talking on the track")
						lines(2) must startWith("Nico -> My dad is bigger than yours!")
					}	

				}
			}				
		}

	"If a tweeter has posted some messages" - {
		val system = TweetSystem()
		system.execute("SmartDev -> I am going to buy Patterns, Practices and Principles of Domain-Driven Design")
		system.execute("SmartDev -> Order Placed. Yipppeeee")

		"And other tweeters have posted some messages" - {
			system.execute("James -> We are going to limit WIP so that we can finish more work")
			system.execute("Louise -> Can I get some ice cream")

			"And the original tweeter follows the other tweeters" - {
				system.execute("SmartDev follows James")
				system.execute("SmartDev follows Louise")

				"When the original tweeter looks at their wall they can see:" - {
					val response = system.execute("SmartDev wall")
					val lines = response.split("\n")

					"Their own tweets & the tweets of the tweeters they are following's tweets chronoligically sorted" in {
						lines(0) must startWith("SmartDev -> I am going to buy Patterns, Practices and Principles of Domain-Driven Design")
						lines(1) must startWith("SmartDev -> Order Placed. Yipppeeee")
						lines(2) must startWith("James -> We are going to limit WIP so that we can finish more work")
						lines(3) must startWith("Louise -> Can I get some ice cream")
					}

				}
			}
		}
	}
	
}