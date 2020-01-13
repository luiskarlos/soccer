package com.lk.engine.soccer.script;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.soccer.console.Console.Level;
import com.lk.engine.soccer.script.instructions.Block;
import com.lk.engine.soccer.script.instructions.CatchPenalty;
import com.lk.engine.soccer.script.instructions.Change;
import com.lk.engine.soccer.script.instructions.Define;
import com.lk.engine.soccer.script.instructions.Echo;
import com.lk.engine.soccer.script.instructions.Exit;
import com.lk.engine.soccer.script.instructions.Goto;
import com.lk.engine.soccer.script.instructions.Instruction;
import com.lk.engine.soccer.script.instructions.IsListenenForMessage;
import com.lk.engine.soccer.script.instructions.ListenForMessage;
import com.lk.engine.soccer.script.instructions.PrepareToCatch;
import com.lk.engine.soccer.script.instructions.PrepareToShoot;
import com.lk.engine.soccer.script.instructions.Run;
import com.lk.engine.soccer.script.instructions.SendGlobalMessage;
import com.lk.engine.soccer.script.instructions.Set;
import com.lk.engine.soccer.script.instructions.ShootPenalty;
import com.lk.engine.soccer.script.instructions.Spawn;
import com.lk.engine.soccer.script.instructions.Spot;
import com.lk.engine.soccer.script.instructions.WalkTo;
import com.lk.engine.soccer.script.instructions.coach.CoachPassMessage;
import com.lk.engine.soccer.script.scanners.BlockScanner;
import com.lk.engine.soccer.script.scanners.BuildExecutable;
import com.lk.engine.soccer.script.scanners.ExecutableScanner;
import com.lk.engine.soccer.script.scanners.Scanner;

public class ScriptParser {
	private final BlockScanner blockParser = new BlockScanner();
	private final List<Scanner> scanners = new ArrayList<Scanner>();
	private ScriptParserListener listener = new ScriptParserListener() {
		@Override
		public void onBlock(final String name, final Block block) {
		}
	};

	public ScriptParser() {

		scanners.add(blockParser);

		scanners.add(new ExecutableScanner("pass", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new CoachPassMessage(tokens.nextString(), tokens.nextPos());
			}
		}));

		scanners.add(new ExecutableScanner("change", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new Change(tokens.nextString(), tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("echo", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				if (tokens.isNext("\""))
					return new Echo(Level.MSG, tokens.consumeRange("\""));
				else
					return new Echo(Level.valueOf(tokens.nextString().toUpperCase()), tokens.consumeRange("\""));
			}
		}));

		scanners.add(new ExecutableScanner("exit", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new Exit();
			}
		}));

		scanners.add(new ExecutableScanner("define", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new Define((Block) blockParser.scan(tokens));
			}
		}));

		scanners.add(new ExecutableScanner("run", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				if (tokens.isBlockNext())
					return new Run("anonymous", (Block) blockParser.scan(tokens));
				else
					return new Run(tokens.nextString(), (Block) blockParser.scan(tokens));
			}
		}));

		scanners.add(new ExecutableScanner("goto", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new Goto(tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("spawn", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new Spawn(tokens.nextString(), tokens.nextString(), tokens.nextString(), tokens.nextString(), tokens
				    .nextString(), tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("set", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new Set(tokens.nextString(), tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("spot", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new Spot(tokens.nextString(), tokens.nextString(), tokens.nextString(), tokens.nextString(), tokens
				    .nextString());
			}
		}));

		scanners.add(new ExecutableScanner("listenForMessage", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new ListenForMessage(tokens.nextString(), tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("walk", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new WalkTo(tokens.nextString(), tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("PrepareToShoot", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new PrepareToShoot(tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("PrepareToCatch", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new PrepareToCatch(tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("catchPenalty", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new CatchPenalty(tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("ShootPenalty", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new ShootPenalty(tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("IsListenForMessage", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new IsListenenForMessage(tokens.nextString(), tokens.nextString());
			}
		}));

		scanners.add(new ExecutableScanner("SendGlobalMessage", new BuildExecutable() {
			@Override
			public Executable newExec(final Tokens tokens) {
				return new SendGlobalMessage(tokens.nextString());
			}
		}));

	}

	public void setListener(final ScriptParserListener listener) {
		this.listener = listener;
	}

	public Block parse(final String script) {
		final String noComment = removeComments(script);
		final Tokens tokens = new Tokens(noComment.replace(',', ' ').replaceAll(" to ", " ").replaceAll("\"", " \" ")
		    .split("\\s+"));
		final Block block = new Block("main");
		for (; tokens.hasNext();) {
			block.addInstruction(tokens.nextInstruction());
		}
		return block;
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

		public String[] consumeRange(String delimiter) {
			cosunme(delimiter);
			final String[] strings = consumeUntil(delimiter);
			cosunme(delimiter);
			return strings;
		}

		public String[] consumeUntil(String string) {
			final List<String> strings = new ArrayList<String>();
			for (; !isNext("\"");) {
				strings.add(nextString());
			}
			return strings.toArray(new String[strings.size()]);
		}

		public String cosunme(String string) {
			if (isNext(string))
				return nextString();
			else
				throw new RuntimeException("Missing token " + string);
		}

		public boolean isCloseBlockNext() {
			return tokens[current].equals("}");
		}

		public void nextCloseBlock() {
			nextString();// consume }
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
					final Instruction instruction = (Instruction) s.scan(this);
					if (isNextOnExit()) {
						nextString();
						instruction.setOnExit(nextInstruction());
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
