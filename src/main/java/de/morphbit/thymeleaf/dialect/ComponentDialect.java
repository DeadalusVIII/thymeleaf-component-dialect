/* 
 * Copyright 2017, Danny Rottstegge
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.morphbit.thymeleaf.dialect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import de.morphbit.thymeleaf.model.ThymeleafComponent;
import de.morphbit.thymeleaf.parser.IThymeleafComponentParser;
import de.morphbit.thymeleaf.processor.ComponentNamedElementProcessor;
import de.morphbit.thymeleaf.processor.OnceAttributeTagProcessor;

public class ComponentDialect extends AbstractProcessorDialect {

	public static final String NAME = "Component Dialect";
	public static final String PREFIX = "tc";
	public static final int PRECEDENCE = 1000;

	private final Set<ThymeleafComponent> components;
	private final List<IThymeleafComponentParser> parsers = new ArrayList<>();

	public ComponentDialect() {
		this(null);
	}

	public ComponentDialect(Set<ThymeleafComponent> components) {
		super(NAME, PREFIX, PRECEDENCE);
		this.components = components;
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		Set<IProcessor> processors = new HashSet<>();
		processors.add(new OnceAttributeTagProcessor(dialectPrefix));

		if (this.components != null) {
			for (ThymeleafComponent comp : this.components) {
				processors.add(new ComponentNamedElementProcessor(dialectPrefix,
				    comp.getName(), comp.getFragmentTemplate()));
			}
		}

		for (ThymeleafComponent comp : parseComponents()) {
			processors.add(new ComponentNamedElementProcessor(dialectPrefix,
			    comp.getName(), comp.getFragmentTemplate()));
		}

		return processors;
	}

	private Set<ThymeleafComponent> parseComponents() {
		Set<ThymeleafComponent> parsedComponents = new HashSet<>();
		for (IThymeleafComponentParser parser : this.parsers) {
			parsedComponents.addAll(parser.parse());
		}

		return parsedComponents;
	}

	public void addParser(IThymeleafComponentParser parser) {
		this.parsers.add(parser);
	}
}
