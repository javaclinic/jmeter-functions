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
 *     __RandomInteger()        - returns a random integer 
 *     __RandomInteger(max)     - returns a positive random integer less than max
 *     __RandomInteger(min,max) - returns a random integer in min..max range
 *
 * @author nevenc
 *
 */
public class RandomInteger extends AbstractFunction {

	private static final String KEY = "__RandomInteger";
	private static final int DEFAULT_MAXIMUM_NUMBER = Integer.MAX_VALUE;

	private static final List<String> description = new LinkedList<String>();
	private static final Logger log = LoggingManager.getLoggerForClass();
	private static final Random random = new Random(System.currentTimeMillis());

	private Object[] values;

	static {
		description.add("Returns a random integer number, e.g. __RandomInteger() or __RandomInteger(max) or __RandomInteger(min,max)");
	}

	public RandomInteger() {
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
		int maximum = DEFAULT_MAXIMUM_NUMBER;
		int minimum = 0;
		Integer randomInteger = 0;
		if ( values.length == 2) {
			maximum = Integer.parseInt(((CompoundVariable) values[1]).execute());
			minimum = Integer.parseInt(((CompoundVariable) values[0]).execute());
			randomInteger = minimum + ((int) random.nextDouble() * (maximum-minimum));
		} else if (values.length == 1) {
			maximum = Integer.parseInt(((CompoundVariable) values[1]).execute());
			randomInteger = random.nextInt(maximum);
		} else {
			randomInteger = random.nextInt();
		}
		log.debug(String.format("Maximum number %d, Random %d", maximum, randomInteger));
		return randomInteger.toString();
	}

}
