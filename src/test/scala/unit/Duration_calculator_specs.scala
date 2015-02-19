package tests.unit

import org.scalatest.FreeSpec 
import org.scalatest.MustMatchers
import consoletweetz.DurationCalculator
import org.joda.time.DateTime

class Duration_calculator_specs extends FreeSpec with MustMatchers {
	val calc = new DurationCalculator()

	"When the duration between two times is under 1 minute, the duration is calculated in seconds (rounded down)" in {
		val secondsDifference = Seq(
			(new DateTime(2015, 1, 1, 1, 1, 1), new DateTime(2015, 1, 1, 1, 1, 2), 1),
			(new DateTime(2015, 1, 1, 1, 1, 1), new DateTime(2015, 1, 1, 1, 1, 9), 8),
			(new DateTime(2015, 1, 1, 1, 1, 10), new DateTime(2015, 1, 1, 1, 2, 9), 59),
			(new DateTime(2017, 5, 5, 6, 7, 33), new DateTime(2017, 5, 5, 6, 7, 54), 21)
		)
		secondsDifference foreach {
			case (start: DateTime, end: DateTime, duration: Int) =>
				calc.calculate(start, end) must equal(s"$duration seconds")
		}
	}

	"When the duration between two times is under 1 hour, the duration is calculated in minutes (rounded down)" in {
		val minutesDifference = Seq(
			(new DateTime(2015, 1, 1, 1, 1, 1), new DateTime(2015, 1, 1, 1, 2, 1), 1),
			(new DateTime(2015, 1, 1, 1, 1, 30), new DateTime(2015, 1, 1, 1, 2, 31), 1),
			(new DateTime(2015, 1, 1, 1, 1, 30), new DateTime(2015, 1, 1, 1, 5, 31), 4),
			(new DateTime(2017, 3, 1, 23, 0, 0), new DateTime(2017, 3, 1, 23, 59, 59), 59)
		)
		minutesDifference foreach {
			case (start: DateTime, end: DateTime, duration: Int) =>
				calc.calculate(start, end) must equal(s"$duration minutes")
		}
	}

	"When the duration between two times is under 1 day, the duration is calculated in hours (rounded down)" in {
		val hoursDifference = Seq(
			(new DateTime(2020, 5, 5, 0, 0, 0), new DateTime(2020, 5, 5, 1, 0, 0), 1),
			(new DateTime(2020, 5, 5, 0, 0, 0), new DateTime(2020, 5, 5, 1, 59, 0), 1),
			(new DateTime(2020, 5, 5, 0, 0, 0), new DateTime(2020, 5, 5, 23, 59, 59), 23),
			(new DateTime(2020, 5, 5, 6, 0, 0), new DateTime(2020, 5, 5, 18, 0, 0), 12)
		)
		hoursDifference foreach {
			case (start: DateTime, end: DateTime, duration: Int) =>
				calc.calculate(start, end) must equal(s"$duration hours")
		}
	}

	"When the duration between two times is 1 day or more, the duration is calculate in days (rounded down)" in {
		val daysDifference = Seq(
			(new DateTime(2019, 11, 6, 0, 0, 0), new DateTime(2019, 11, 7, 0, 0, 0), 1),
			(new DateTime(2019, 11, 6, 0, 0, 0), new DateTime(2019, 11, 7, 23, 59, 59), 1),
			(new DateTime(2019, 11, 4, 0, 0, 0), new DateTime(2020, 11, 7, 0, 0, 0), 369),
			(new DateTime(2019, 11, 23, 0, 0, 0), new DateTime(2019, 12, 1, 0, 0, 0), 8)
		)
		daysDifference foreach {
			case (start: DateTime, end: DateTime, duration: Int) =>
				calc.calculate(start, end) must equal(s"$duration days")
		}
	}
}