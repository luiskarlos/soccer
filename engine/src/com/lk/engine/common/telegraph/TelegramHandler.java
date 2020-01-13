package com.lk.engine.common.telegraph;

public interface TelegramHandler {
	enum Processed {
		YES, NO;
	}

	Processed handle(final Telegram telegram);
}
