package net.neoremind.mycode.rabbitmq.queue;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 当存在多个消费者时，会进行轮训分发。
 * Round-robin dispatching
 * <p/>
 * consumer挂了，比如channel关闭，连接断了，tcp断了等，没有发送ack回去，
 * rmq会记住这个消息，重新分发给另外的consumer。这样会保证不会丢消息。
 * If a consumer dies (its channel is closed, connection is closed, or TCP connection is lost) without sending an
 * ack, RabbitMQ will understand that a message wasn't processed fully and will re-queue it. If there are other
 * consumers online at the same time, it will then quickly redeliver it to another consumer. That way you can be sure
 * that no message is lost, even if the workers occasionally die.
 * <p/>
 * 消息没有超时的说法，consumer消费多长时间都行。
 * There aren't any message timeouts; RabbitMQ will redeliver the message when the consumer dies. It's fine even if
 * processing a message takes a very, very long time.
 * <p/>
 * 如果没有ack，rmq会一直存在消息不会释放。
 * It's a common mistake to miss the basicAck. It's an easy error, but the consequences are serious. Messages will be
 * redelivered when your client quits (which may look like random redelivery), but RabbitMQ will eat more and more
 * memory as it won't be able to release any unacked messages.
 * <p/>
 * <p/>
 * 通过如下命令，查看queue中的准备分发的消息和未ack的数量。
 * <pre>
 * ./rabbitmqctl list_queues name messages_ready messages_unacknowledged
 * Listing queues ...
 * hello 1 0
 * </pre>
 * <p/>
 * RMQ不保证不丢消息，很短的一个时间窗口，queue没有持久化，消息还在内存中的时候进程挂了，那么消息就丢了。
 * 保证不丢可以使用publisher confirms。//TODO
 * Marking messages as persistent doesn't fully guarantee that a message won't be lost. Although it tells RabbitMQ to
 * save the message to disk, there is still a short time window when RabbitMQ has accepted a message and hasn't saved
 * it yet. Also, RabbitMQ doesn't do fsync(2) for every message -- it may be just saved to cache and not really
 * written to the disk. The persistence guarantees aren't strong, but it's more than enough for our simple task queue
 * . If you need a stronger guarantee then you can use publisher confirms.
 *
 * @author zhangxu
 * @see <a href="http://www.rabbitmq.com/tutorials/tutorial-two-java.html">tutorial-two-java</a>
 */
public class Recv {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // 保证不会不均匀的分发给下游，处理完一个再接一个，不按照轮训
        // 这个叫做Fair dispatch
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                } finally {
                    System.out.println(" [x] Done");
                    // 这里必须ack回去，否则rmq会一直保存住这个消息
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        // autoAck=false
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }

}
