package com.talos.javatraining.lesson9.factories.impl;

import com.talos.javatraining.lesson9.ExitException;
import com.talos.javatraining.lesson9.commands.AppCommand;
import com.talos.javatraining.lesson9.commands.impl.*;
import com.talos.javatraining.lesson9.events.EventBus;
import com.talos.javatraining.lesson9.events.EventType;
import com.talos.javatraining.lesson9.factories.CommandFactory;

import javax.inject.Inject;
import java.util.Optional;


public class CommandFactoryImpl implements CommandFactory
{
	private static final String EXIT = "x";
	private static final String MODE = "=>";
	private static final String ADD = "+";
	private static final String SUBTRACT = "-";
	private static final String MULTIPLY = "*";
	private static final String DIVIDE = "/";

	@Inject
	private EventBus eventBus;


	@Override
	public Optional<AppCommand> createCommand(String line)
	{
		String[] parts = line.trim().split("\\s+");
		Optional<AppCommand> result;
		int size = parts.length;
		if (size == 1 && EXIT.equals(parts[0]))
		{
			result = Optional.of(() -> {
				throw new ExitException();
			});
		}
		else if (size == 2 && MODE.equals(parts[0]))
		{
			result = Optional.of(new CommandTemplate(EventType.CHANGE_MODE, eventBus, parts[1]));
		}
		else if (size == 3)
		{
			AppCommand command = null;
			switch (parts[1])
			{
				case ADD:
					command = new CommandTemplate(EventType.ADD, eventBus, parts[0], parts[2]);
					break;
				case SUBTRACT:
					command = new CommandTemplate(EventType.SUBTRACT, eventBus, parts[0], parts[2]);
					break;
				case MULTIPLY:
					command = new CommandTemplate(EventType.MULTIPLY, eventBus, parts[0], parts[2]);
					break;
				case DIVIDE:
					command = new CommandTemplate(EventType.DIVIDE, eventBus, parts[0], parts[2]);
					break;
			}
			result = Optional.ofNullable(command);
		}
		else
		{
			result = Optional.empty();
		}
		return result;
	}
}
