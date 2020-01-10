package com.lk.engine.common.telegraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lk.engine.common.core.Updatable;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.telegraph.exceptions.TelegramHandlerAlredyRegister;
import com.lk.engine.common.time.FrameCounter;

public class Telegraph implements Updatable {
	private final FrameCounter frameCounter;
	private final List<Delayed> delayed = new ArrayList<Telegraph.Delayed>();
	private final Map<Enum<Message>, List<TelegramHandler>> handlers = new HashMap<Enum<Message>, List<TelegramHandler>>();
	private final Map<Object, HandlerByID> handlersById = new HashMap<Object, HandlerByID>();

	public Telegraph(final FrameCounter frameCounter) {
		this.frameCounter = frameCounter;
		for (Message msg : Message.values())
			handlers.put(msg, new ArrayList<TelegramHandler>());
	}

	public void checking(TelegramCheckin handlers) {
		handlers.checkin(this);
	}

	public void register(final Message message, final TelegramHandler handler, int id) {
		if (!handlersById.containsKey(id))
			handlersById.put(id, new HandlerByID());

		handlersById.get(id).register(message, handler);
	}

	public void checkin(final Message message, final TelegramHandler handler) {
		if (handlers.get(message).contains(handler))
			throw new TelegramHandlerAlredyRegister();

		handlers.get(message).add(handler);
	}

	public void post(final Telegram telegram) {
		if (telegram.getReceiver() > 0 && handlersById.containsKey(telegram.getReceiver()))
			handlersById.get(telegram.getReceiver()).post(telegram);
		else
			for (TelegramHandler handler : handlers.get(telegram.getMsg()))
				handler.handle(telegram);
	}

	public void post(final double delay, final Telegram telegram) {
		final double currentTime = frameCounter.getCurrentFrame();
		final Delayed delayed = new Delayed(currentTime + delay, telegram);
		for (int i = 0; i < this.delayed.size(); i++) {
			if (delayed.postTime < this.delayed.get(i).postTime) {
				this.delayed.add(i, delayed);
				return;
			}
		}
		this.delayed.add(delayed);
	}

	@Override
	public Active update() {
		final double currentTime = frameCounter.getCurrentFrame();
		for (;;) {
			if (this.delayed.get(0).postTime < currentTime)
				this.delayed.remove(0).post();
			else
				break;
		}

		return Active.Yes;
	}

	private class HandlerByID {
		private final Map<Enum<Message>, TelegramHandler> handlers = new HashMap<Enum<Message>, TelegramHandler>();

		public void register(final Enum<Message> message, final TelegramHandler telegram) {
			if (handlers.containsKey(message))
				throw new TelegramHandlerAlredyRegister();

			handlers.put(message, telegram);
		}

		public void post(final Telegram telegram) {
			if (handlers.containsKey(telegram.getMsg()))
				handlers.get(telegram.getMsg()).handle(telegram);
		}
	}

	private class Delayed {
		private final double postTime;
		private final Telegram telegram;

		public Delayed(double postTime, Telegram telegram) {
			super();
			this.postTime = postTime;
			this.telegram = telegram;
		}

		public void post() {
			Telegraph.this.post(telegram);
		}
	}
}
