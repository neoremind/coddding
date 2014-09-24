package net.neoremind.mycode.nio.httpserver;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NioHttpServer implements Runnable {

	public static void main(String[] args) throws IOException {
		// Defines the html server root file path, html pages will be loaded from here
		String root = new File("src/main/java/net/neoremind/mycode/nio/httpserver/htmlpages").getAbsolutePath();
		int port = 8080;
		
		// Init a nio http server instance
		NioHttpServer server = new NioHttpServer(null, port);
		
		// Init a soft reference cache to cache the html pages loaded
		ButterflySoftCache cache = new ButterflySoftCache();
		
		//Create multiple request handler
		for (int i = 0; i < Runtime.getRuntime().availableProcessors(); ++i) {
			RequestHandler handler = new RequestHandler(server, root, cache);
			server.addRequestHanlder(handler);
			new Thread(handler, "worker" + i).start();
		}

		//Create main thread to handle request in async way, this will delegate
		//request handling to specific worker that created above.
		new Thread(server, "selector").start();
	}

	private ServerSocketChannel serverChannel;
	private Selector selector;
	
	// ByteBuffer is not thread safe, but it is only used by read() method in
	// the selector's loop
	private ByteBuffer readBuffer = ByteBuffer.allocate(8912);
	
	
	private List<ChangeRequest> changeRequests = new LinkedList<ChangeRequest>();
	
	private Map<SocketChannel, List<ByteBuffer>> pendingSent = new HashMap<SocketChannel, List<ByteBuffer>>();
	
	// Multiple request handler list, socket channel will be mapped into one
	// handler according to the mod hash result
	private List<RequestHandler> requestHandlers = new ArrayList<RequestHandler>();

	public NioHttpServer(InetAddress address, int port) throws IOException {
		selector = Selector.open();
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(new InetSocketAddress(address, port));
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	@Override
	public void run() {
		SelectionKey key = null;
		while (true) {
			try {
				synchronized (changeRequests) {
					for (ChangeRequest request : changeRequests) {
						switch (request.type) {
						case ChangeRequest.CHANGEOPS:
							key = request.socket.keyFor(selector);
							if (key != null && key.isValid()) {
								key.interestOps(request.ops);
							}
							break;
						}
					}
					changeRequests.clear();
				}

				selector.select();
				Iterator<SelectionKey> selectedKeys = selector.selectedKeys()
						.iterator();
				while (selectedKeys.hasNext()) {
					key = selectedKeys.next();
					selectedKeys.remove();
					if (!key.isValid()) {
						continue;
					}
					if (key.isAcceptable()) {
						accept(key);
					} else if (key.isReadable()) {
						read(key);
					} else if (key.isWritable()) {
						write(key);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (key != null) {
					key.cancel();
					Utils.closeQuietly(key.channel());
				}
			}
		}
	}

	private void accept(SelectionKey key) throws IOException {
		SocketChannel socketChannel = serverChannel.accept();
		System.out.println("new connection:\t" + socketChannel);
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		readBuffer.clear();
		int numRead;
		try {
			numRead = socketChannel.read(readBuffer);
		} catch (IOException e) {
			// the remote forcibly closed the connection
			key.cancel();
			socketChannel.close();
			System.out.println("closed by exception" + socketChannel);
			return;
		}

		if (numRead == -1) {
			// remote entity shut the socket down cleanly.
			socketChannel.close();
			key.cancel();
			System.out.println("closed by shutdown" + socketChannel);
			return;
		}

		int worker = socketChannel.hashCode() % requestHandlers.size();
		System.out.println(selector.keys().size() + "\t" + worker + "\t"
				+ socketChannel);
		requestHandlers.get(worker).processData(socketChannel,
				readBuffer.array(), numRead);
	}

	public void send(SocketChannel socket, byte[] data) {
		synchronized (changeRequests) {
			changeRequests.add(new ChangeRequest(socket,
					ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));
			synchronized (pendingSent) {
				List<ByteBuffer> queue = pendingSent.get(socket);
				if (queue == null) {
					queue = new ArrayList<ByteBuffer>();
					pendingSent.put(socket, queue);
				}
				queue.add(ByteBuffer.wrap(data));
			}
		}

		selector.wakeup();
	}

	private void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		synchronized (pendingSent) {
			List<ByteBuffer> queue = pendingSent.get(socketChannel);
			while (!queue.isEmpty()) {
				ByteBuffer buf = queue.get(0);
				socketChannel.write(buf);
				// have more to send
				if (buf.remaining() > 0) {
					break;
				}
				queue.remove(0);
			}
			if (queue.isEmpty()) {
				key.interestOps(SelectionKey.OP_READ);
			}
		}
	}
	
	public void addRequestHanlder(RequestHandler handler) {
		requestHandlers.add(handler);
	}
}
