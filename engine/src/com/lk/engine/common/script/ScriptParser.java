package com.lk.engine.common.script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.google.gwt.event.shared.EventBus;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.script.instructions.*;
import com.lk.engine.common.script.instructions.coach.CoachPassMessage;
import com.lk.engine.common.script.instructions.console.ListFSMStatus;
import com.lk.engine.common.script.instructions.console.ListVariables;
import com.lk.engine.common.script.scanners.BlockScanner;
import com.lk.engine.common.script.scanners.BuildExecutable;
import com.lk.engine.common.script.scanners.ExecutableScanner;
import com.lk.engine.common.script.scanners.Scanner;

public class ScriptParser {
	private Environment enviroment;
	private final BlockScanner blockParser = new BlockScanner();
	private final List<Scanner> scanners = new ArrayList<Scanner>();
	private final EventBus eventBus;

	private ScriptParserListener listener = (name, block) -> {
  };

	public ScriptParser(Environment environment, EventBus eventBus) {
		this.enviroment = environment;
		this.eventBus = eventBus;

		scanners.add(blockParser);

		scanners.add(new ExecutableScanner("fsm.status", tokens -> new ListFSMStatus()));
		
		scanners.add(new ExecutableScanner("listvars", tokens -> new ListVariables(tokens.nextString())));

		scanners.add(new ExecutableScanner("onEnter", tokens -> new OnEnter(tokens.nextState(), tokens.nextInstruction())));

		scanners.add(new ExecutableScanner("onExit", tokens -> new OnExit(tokens.nextState(), tokens.nextInstruction())));

		scanners.add(new ExecutableScanner("before", tokens -> new Before(tokens.nextString(), tokens.nextState(), tokens.nextString(), tokens.nextStateArray())));

		scanners.add(new ExecutableScanner("after", tokens -> new After(tokens.nextString(), tokens.nextState(), tokens.nextString(), tokens.nextStateArray())));

		scanners.add(new ExecutableScanner("pass", tokens -> new CoachPassMessage(tokens.nextString(), tokens.nextPos())));

		scanners.add(new ExecutableScanner("change", tokens -> new Change(tokens.nextString(), tokens.nextString())));

		scanners.add(new ExecutableScanner("echo", tokens -> {
      if (tokens.isNext("\""))
        return new Echo(Level.SEVERE, tokens.consumeRange("\"", "\""));
      else
        tokens.nextString(); // TODO: consume level
      return new Echo(Level.SEVERE, tokens.consumeRange("\"", "\""));
    }));

		scanners.add(new ExecutableScanner("exit", tokens -> new Exit()));

		scanners.add(new ExecutableScanner("define", tokens -> new Define((Block) blockParser.scan(tokens))));

		scanners.add(new ExecutableScanner("run", tokens -> {
      if (tokens.isBlockNext())
        return new Run("anonymous", (Block) blockParser.scan(tokens));
      else
        return new Run(tokens.nextString(), (Block) blockParser.scan(tokens));
    }));

		scanners.add(new ExecutableScanner("goto", tokens -> new Goto(tokens.nextString())));

		scanners.add(new ExecutableScanner("spawn", tokens -> new Spawn(tokens.nextString(), tokens.nextString(), tokens.nextString(), tokens.nextString(),
        (int)tokens.nextDouble())));

		scanners.add(new ExecutableScanner("set", tokens -> new Set(tokens.nextString(), tokens.nextString())));

		scanners.add(new ExecutableScanner("spot", tokens -> new Spot(tokens.nextString(), tokens.nextString(), tokens.nextString(), tokens.nextString(), tokens
        .nextString())));

		scanners.add(new ExecutableScanner("listenForMessage", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new ListenForMessage(tokens.nextString(), tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("walk", tokens -> new WalkTo(tokens.nextString(), tokens.nextString())));

		scanners.add(new ExecutableScanner("PrepareToShoot", tokens -> new PrepareToShoot(tokens.nextString())));

		scanners.add(new ExecutableScanner("PrepareToCatch", tokens -> new PrepareToCatch(tokens.nextString())));

		scanners.add(new ExecutableScanner("catchPenalty", tokens -> new CatchPenalty(tokens.nextString())));

		scanners.add(new ExecutableScanner("ShootPenalty", tokens -> new ShootPenalty(tokens.nextString())));

		scanners.add(new ExecutableScanner("IsListenForMessage", tokens -> new IsListenenForMessage(tokens.nextString(), tokens.nextString())));

		scanners.add(new ExecutableScanner("SendGlobalMessage", tokens -> new SendGlobalMessage(tokens.nextString())));

		scanners.add(new ExecutableScanner("csv.load", tokens -> new CsvLoad(eventBus, Arrays.stream(tokens.consumeRange("\"", "\""))
        .collect(Collectors.joining(",")))));
	}

	public void setListener(final ScriptParserListener listener) {
		this.listener = listener;
	}

	public Block parse(final String script) {
		String noComment = removeComments(script);
		noComment = preProcess(noComment);
		final Tokens tokens = new Tokens(noComment.replaceAll(" to ", " ").split("\\s+"));
		final Block block = new Block("main");
		for (; tokens.hasNext();) {
			block.addInstruction(tokens.nextInstruction());
		}
		return block;
	}

	final String spaces = "()[]{}\"";
	final String remove = ",";

	/**
	 * TODO: Split the string instead of this.
	 */
	private String preProcess(String script) {
		final StringBuilder buffer = new StringBuilder(script);
		buffer.append(' ');
		for (int i = buffer.length() - 1; i >= 0; i--) {
			if (spaces.indexOf(buffer.charAt(i)) >= 0) {
				buffer.insert(i + 1, ' ');
				buffer.insert(i, ' ');
			} else if (remove.indexOf(buffer.charAt(i)) >= 0) {
				buffer.setCharAt(i, ' ');
			}
		}
		return buffer.toString();
	}

	// This should work, not sure why not
	// script.replaceAll("//.*\\n|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/",
	// " ")
	private String removeComments(String script) {
		if (!script.contains("//"))
			return script;

		final StringBuilder noComments = new StringBuilder();
		for (String l : script.split("\\r?\\n")) {
			String noCommentLine = l.trim();
			final int indexOffComment = l.indexOf("//", 0);
			if (indexOffComment >= 0) {
				noCommentLine = l.substring(0, indexOffComment).trim();
			}

			if (!noCommentLine.isEmpty())
				noComments.append(noCommentLine).append(' ');
		}

		return noComments.toString();
	}

	public class Tokens {
		private final String[] tokens;
		private int current = 0;

		private Tokens(final String[] tokens) {
			this.tokens = tokens;
		}

		public String[] consumeRange(String start, String end) {
			consume(start);
			final String[] strings = consumeUntil(end);
			consume(end);
			return strings;
		}

		public String[] consumeUntil(String delimiter) {
			final List<String> strings = new ArrayList<String>();
			for (; !isNext(delimiter);) {
				strings.add(nextString());
			}
			return strings.toArray(new String[strings.size()]);
		}

		public String consume(String string) {
			if (isNext(string))
				return nextString();
			else
				throw new RuntimeException("Missing token " + string);
		}

		public List<State> nextStateArray() {
			final String[] strings = consumeRange("[", "]");
			final List<State> result = new ArrayList<State>();
			for (String name : strings) {
				result.add(enviroment.getState(name));
			}
			return result;
		}

		public State nextState() {
			return enviroment.getState(nextString());
		}

		public Block nextBlock() {
			final String name = nextString();
			if (name.equals("{"))
				return new Block("anonymous");
			else
				nextString(); // consume {

			final Block block = new Block(name);
			listener.onBlock(name, block);
			return block;
		}

		public boolean isCloseBlockNext() {
			return tokens[current].equals("}");
		}

		public void nextCloseBlock() {
			nextString();// consume }
		}

		public String nextString() {
			return tokens[current++];
		}

		public boolean hasNext() {
			return current < tokens.length;
		}

		public boolean isBlockNext() {
			return tokens[current].equals("{") || current + 1 < tokens.length && tokens[current + 1].equals("{");
		}

		public Vector2D nextPos() {
			return new Vector2D(nextDouble(), nextDouble());
		}

		private double nextDouble() {
			return Double.parseDouble(nextString());
		}

		public boolean isNext(final String token) {
			return hasNext() && tokens[current].equalsIgnoreCase(token);
		}

		public Executable nextInstruction() {
			if (!hasNext())
				throw new RuntimeException("No more instructions!");

			for (final Scanner s : scanners) {
				if (s.isNext(this)) {
					final Executable instruction = s.scan(this);
					if (isNextOnExit()) {
						nextString();
						((Instruction) instruction).setOnExit(nextInstruction());
					}
					return instruction;
				}
			}

			throw new RuntimeException("Instruction not found: " + tokens[current]);
		}

		private boolean isNextOnExit() {
			return isNext("onExit");
		}
	}
}
