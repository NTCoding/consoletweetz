package consoletweetz

import org.joda.time.{DateTime, Duration}

class DurationCalculator {

	def calculate(start: DateTime, end: DateTime): String = {
		val p = new Duration(start, end)
		if (p.getStandardDays >= 1)
			s"${p.getStandardDays} days"
		else if (p.getStandardHours >= 1)
			s"${p.getStandardHours} hours"
		else if (p.getStandardMinutes >= 1)
			s"${p.getStandardMinutes} minutes"
		else
			s"${p.getStandardSeconds} seconds"
	}

}

