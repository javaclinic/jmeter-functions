package com.javaclinic.jmeter.functions;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * JMeter function to return random long number.
 *
 *   Used in JMeter as:
 *     __RandomDate()          - returns a random date in last 365 days
 *     __RandomDate(seconds)   - returns a random date in last x seconds
 *     __RandomDate(start,end) - returns a random date between start and end date
 *
 * Requires Java 1.8
 * 
 * @author nevenc
 *
 */
public class RandomDate extends AbstractFunction {
	
	private static final String KEY = "__RandomDate";
	private final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
	private static final List<String> description = new LinkedList<String>();
	private static final Logger log = LoggingManager.getLoggerForClass();
	private static final Random random = new Random(System.currentTimeMillis());

	private Object[] values;
	private static final long ONE_YEAR_IN_SECONDS = 365*86400;

	static {
		description.add("Random date in last year, e.g. __RandomDate()");
		description.add("Random date in last X seconds, e.g. __RandomDate(x)");
		description.add("Random date between two dates, e.g. __RandomDate(2015-01-01,2015-12-31)");
	}

	public RandomDate() {
	}

	@Override
	public List<String> getArgumentDesc() {
		return description;
	}

	@Override
	public String getReferenceKey() {
		return KEY;
	}

	@Override
	public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
		checkParameterCount(parameters, 0, 2);
		values = parameters.toArray();
	}

	@Override
	public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
		long numberSeconds = ONE_YEAR_IN_SECONDS;
		long now = Instant.now().getEpochSecond();
		LocalDateTime randomDate = null;
		if ( values.length == 2) {
			try {
				String startValue = ((CompoundVariable) values[0]).execute();
				String endValue = ((CompoundVariable) values[1]).execute();
				long startDate = LocalDate.parse(startValue).toEpochDay();
				long endDate = LocalDate.parse(endValue).toEpochDay();
				long randomTimestamp = startDate + ((long)((endDate-startDate)*random.nextDouble()));
				randomDate = LocalDateTime.ofEpochSecond(randomTimestamp,0,ZoneOffset.UTC);
				log.debug(String.format("Random date between %s and %s is: %s", startValue, endValue, randomDate));
				return FORMATTER.format(randomDate);
			} catch (DateTimeException dte) {
				log.info("Invalid date format, resorting to default - random date in last 365 days. Error:" + dte);
				throw new InvalidVariableException(dte);
			}
		} else if ( values.length == 1) {
			numberSeconds = Long.parseLong(((CompoundVariable) values[0]).execute());
		}
		long randomTimeInMilis = (long) (numberSeconds * random.nextDouble());
		randomDate = LocalDateTime.ofEpochSecond(now - randomTimeInMilis, 0, ZoneOffset.UTC);
		log.debug(String.format("Random date in last %d seconds: %s", numberSeconds, randomDate.toString()));
		return FORMATTER.format(randomDate);
	}

}
