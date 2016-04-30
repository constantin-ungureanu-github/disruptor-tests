package org.disruptor;

import com.lmax.disruptor.EventFactory;

public class LongEvent {
	private long value;

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public static final EventFactory<LongEvent> EVENT_FACTORY = new EventFactory<LongEvent>() {
		public LongEvent newInstance() {
			return new LongEvent();
		}
	};
}
