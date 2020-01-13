/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.telegraph;

public class TelegramPackage implements Comparable<Object>, Telegram {
	private static final double SEND_MSG_IMMEDIATELY = 0.0;
	private static final int SENDER_ID_IRRELEVANT = -1;
	private static final int ALL = -1;

	private final int sender;
	private final int receiver;
	private final Message msg;
	private final Object extraInfo;

	private double dispatchTime;

	public TelegramPackage(final double time, final int sender, final int receiver, final Message msg) {
		this(time, sender, receiver, msg, null);
	}

	public TelegramPackage(final double dispatchTime, final int sender, final int receiver, final Message msg,
	    final Object extraInfo) {
		this.dispatchTime = dispatchTime;
		this.sender = sender;
		this.receiver = receiver;
		this.msg = msg;
		this.extraInfo = extraInfo;
	}

	public TelegramPackage(Message msg) {
		this(SEND_MSG_IMMEDIATELY, SENDER_ID_IRRELEVANT, ALL, msg, null);
	}

	public TelegramPackage(Message msg, Object extraInfo) {
		this(SEND_MSG_IMMEDIATELY, SENDER_ID_IRRELEVANT, ALL, msg, extraInfo);
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof TelegramPackage)) {
			return false;
		}

		final TelegramPackage t2 = (TelegramPackage) o;
		return (Math.abs(dispatchTime - t2.dispatchTime) < SMALLEST_DELAY) && (sender == t2.sender)
		    && (getReceiver() == t2.getReceiver()) && (getMsg() == t2.getMsg() || (getMsg() == null && t2.getMsg() == null));
	}

	/**
	 * It is generally necessary to override the hashCode method whenever equals
	 * method is overridden, so as to maintain the general contract for the
	 * hashCode method, which states that equal objects must have equal hash
	 * codes.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + this.sender;
		hash = 53 * hash + this.getReceiver();
		hash = 53 * hash + (this.getMsg() != null ? this.getMsg().hashCode() : 0);
		final double DispatchTime = this.dispatchTime - (this.dispatchTime % SMALLEST_DELAY);
		hash = 53 * hash + (int) (Double.doubleToLongBits(DispatchTime) ^ (Double.doubleToLongBits(DispatchTime) >>> 32));
		hash = 97 * hash + (this.getExtraInfo() == null ? 0 : this.getExtraInfo().hashCode());
		return hash;
	}

	@Override
	public int compareTo(final Object o2) {
		final TelegramPackage t2 = (TelegramPackage) o2;
		if (Math.abs(dispatchTime - t2.dispatchTime) < SMALLEST_DELAY) {
			return hashCode() - t2.hashCode(); // equals objects return 0
		} else {
			return (dispatchTime < t2.dispatchTime) ? -1 : 1;
		}
	}

	@Override
	public String toString() {
		return "time: " + dispatchTime + "  Sender: " + sender + "   Receiver: " + getReceiver() + "   Msg: " + getMsg();
	}

	/**
	 * handy helper function for dereferencing the ExtraInfo field of the Telegram
	 * to the required type.
	 */
	public static <T> T dereferenceToType(final T p) {
		return p;
	}

	/* (non-Javadoc)
	 * @see com.lk.engine.common.telegraph.Telegram#getReceiver()
	 */
	@Override
  public int getReceiver() {
	  return receiver;
  }

	/* (non-Javadoc)
	 * @see com.lk.engine.common.telegraph.Telegram#getMsg()
	 */
	@Override
  public Message getMsg() {
	  return msg;
  }

	/* (non-Javadoc)
	 * @see com.lk.engine.common.telegraph.Telegram#getExtraInfo()
	 */
	@Override
  public Object getExtraInfo() {
	  return extraInfo;
  }
}