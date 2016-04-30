package org.disruptor;

import java.nio.ByteBuffer;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class LongEventProducer {
	private final RingBuffer<LongEvent> ringBuffer;

	public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void onReceive(ByteBuffer byteBuffer) {
		ringBuffer.publishEvent(TRANSLATOR, byteBuffer);
	}

	private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
		public void translateTo(LongEvent event, long sequence, ByteBuffer byteBuffer) {
			event.setValue(byteBuffer.getLong(0));
		}
	};
}
