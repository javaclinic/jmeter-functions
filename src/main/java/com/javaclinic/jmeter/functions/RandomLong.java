package com.javaclinic.jmeter.functions;

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
 *     __RandomLong()        - returns a random long number 
 *     __RandomLong(max)     - returns a positive random long number less than max
 *     __RandomLong(min,max) - returns a random long number in min..max range
 *
 * @author nevenc
 *
 */
public class RandomLong extends AbstractFunction {

	private static final String KEY = "__RandomLong";
	private static final long DEFAULT_MAXIMUM_NUMBER = Long.MAX_VALUE;

	private static final List<String> description = new LinkedList<String>();
	private static final Logger log = LoggingManager.getLoggerForClass();
	private static final Random random = new Random(System.currentTimeMillis());

	private Object[] values;

	static {
		description.add("Returns a random long number, e.g. __RandomLong() or __RandomLong(max) or __RandomLong(min,max)");
	}

	public RandomLong() {
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
		long maximum = DEFAULT_MAXIMUM_NUMBER;
		long minimum = 0L;
		Long randomLong = 0L;
		if ( values.length == 2) {
			maximum = Long.parseLong(((CompoundVariable) values[1]).execute());
			minimum = Long.parseLong(((CompoundVariable) values[0]).execute());
			randomLong = minimum + ((long) random.nextDouble() * (maximum-minimum));
		} else if (values.length == 1) {
			maximum = Long.parseLong(((CompoundVariable) values[1]).execute());
			randomLong = (long) (random.nextDouble() * maximum);
		} else {
			randomLong = random.nextLong();
		}
		log.debug(String.format("Maximum number %d, Random %d", maximum, randomLong));
		return randomLong.toString();
	}

}
