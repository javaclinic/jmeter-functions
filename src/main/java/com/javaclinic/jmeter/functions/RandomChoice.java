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
 * JMeter function to return random choice.
 *
 *   Used in JMeter as:
 *     __RandomChoice(Home,Work,Fax,Cell,Other) - returns a random choice between listed values 
 *
 * @author nevenc
 *
 */
public class RandomChoice extends AbstractFunction {

	private static final String KEY = "__RandomChoice";
	private static final int MIN_PARAM_COUNT = 2;

	private static final List<String> description = new LinkedList<String>();
	private static final Logger log = LoggingManager.getLoggerForClass();
	private static final Random random = new Random(System.currentTimeMillis());

	private Object[] values;

	static {
		description.add("Returns a random choice from comma separated list of choices, e.g. __RandomChoice(Home,Work,Fax,Cell,Other)");
	}

	public RandomChoice() {
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
		checkMinParameterCount(parameters, MIN_PARAM_COUNT);
		values = parameters.toArray();
	}

	@Override
	public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
		int index = random.nextInt(values.length - 1);
		log.debug(String.format("Number of options: %d, Random index: %d", values.length, index));
		String choice = ((CompoundVariable) values[index]).execute();
		return choice;
	}

}
