package com.alessiodp.parties.commands;
 
public interface ICommand {
	/**
	 * Used to check player before async execution
	 */
	public boolean preRequisites(CommandData commandData);
	/**
	 * Async command
	 */
	public void onCommand(CommandData commandData);
}