package org.disruptor;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class LongEventMain {
	private static final Logger logger;

	static {
		System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
		logger = LoggerFactory.getLogger(LongEventMain.class);
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Usage run <counts>");
			return;
		}

		long counts = 0;
		try {
			counts = Long.parseLong(args[0]);
		} catch (Exception e) {
			System.out.println("Usage run <counts>");
			return;
		}

		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent.EVENT_FACTORY, 1 << 16, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BusySpinWaitStrategy());
		disruptor.handleEventsWith(new LongEventHandler());

		final long startTime = System.currentTimeMillis();
		disruptor.start();

		final LongEventProducer producer = new LongEventProducer(disruptor.getRingBuffer());

		ByteBuffer byteBuffer = ByteBuffer.allocate(8);
		for (long value = 0; value < counts;) {
			byteBuffer.putLong(0, value++);
			producer.onReceive(byteBuffer);
		}

		disruptor.shutdown();
		final long stopTime = System.currentTimeMillis();

		logger.info("Duration {} milliseconds.", stopTime - startTime);
	}
}
