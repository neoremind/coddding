package net.neoremind.mycode.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息存储在queue中，持久化是对queue而言的。
 * Although messages flow through RabbitMQ and your applications,
 * they can be stored only inside a queue.
 * <p/>
 * queue是无界的
 * A queue is not bound by any limits, it can store as many messages as you like
 * - it's essentially an infinite buffer.
 * <p/>
 * 声明一个queue是幂等的，可以重复声明，只会存在一个queue，默认有个hello的queue是不会被delete掉的。
 * Declaring a queue is idempotent -
 * it will only be created if it doesn't exist already.
 * <p/>
 * 如果发送的消息到一个不存在的queue，则会trash掉这个消息。
 * If we send a message to non-existing location, RabbitMQ will just trash the message.
 * <p/>
 * 消息都是从exchange再到queue的。
 * In RabbitMQ a message can never be sent directly to the queue, it always needs to go through an exchange.
 *
 * @author zhangxu
 * @see <a href="http://www.rabbitmq.com/tutorials/tutorial-one-java.html">tutorial-one-java</a>
 */
public class Send {

    private final static String QUEUE_NAME = "hello";

    /**
     * 发送完之后通过这个命令查看rmq：
     * <pre>
     * baidu@localhost:~/software/rabbitmq_server-3.6.0/sbin$ ./rabbitmqctl list_queuesListing queues ...
     * hello	1
     * </pre>
     *
     * @param argv
     *
     * @throws Exception
     */
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // connect to a broker
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }

}
