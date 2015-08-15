package com.javaclinic.jmeter.functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
 * @author nevenc
 *
 */
public class RandomDate extends AbstractFunction {

	private static final String KEY = "__RandomDate";
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private static final SimpleDateFormat SIMPLE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	private static final List<String> description = new LinkedList<String>();
	private static final Logger log = LoggingManager.getLoggerForClass();
	private static final Random random = new Random(System.currentTimeMillis());

	private Object[] values;
	private static final long ONE_YEAR_IN_SECONDS = 365*86400;

	static {
		description.add("Random date in last X seconds, e.g. __RandomDate(x)");
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
		long randomTimestamp = System.currentTimeMillis();
		Date randomDate = null;
		if ( values.length == 2) {
			try {
				String startValue = ((CompoundVariable) values[0]).execute();
				String endValue = ((CompoundVariable) values[1]).execute();
				long startDate = SIMPLE_FORMATTER.parse(startValue).getTime();
				long endDate = SIMPLE_FORMATTER.parse(endValue).getTime();
				randomTimestamp = startDate + ((long)((endDate-startDate)*random.nextDouble()));
				randomDate = new Date(randomTimestamp);
				log.debug(String.format("Random date between %s and %s is: %s", startValue, endValue, randomDate));
				return FORMATTER.format(randomDate);
			} catch (ParseException pe) {
				log.info("Invalid date format, resorting to default - random date in last 365 days. Error:" + pe);
				throw new InvalidVariableException(pe);
			}
		} else if ( values.length == 1) {
			numberSeconds = Long.parseLong(((CompoundVariable) values[0]).execute());
		}
		long randomTimeInMilis = (long) (1000 * numberSeconds * random.nextDouble());
		randomDate = new Date(System.currentTimeMillis() - randomTimeInMilis);
		log.debug(String.format("Random date in last %d seconds: %s", numberSeconds, randomDate.toString()));
		return FORMATTER.format(randomDate);
	}

}
