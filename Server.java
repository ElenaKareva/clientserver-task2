import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Редактор строк ");
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 23338));

        while (true) {

            try (SocketChannel socketChannel = serverChannel.accept()) {

                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                while (socketChannel.isConnected()) {

                    int bytesCount = socketChannel.read(inputBuffer);

                    if (bytesCount == -1) break;


                    final String editMsg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    socketChannel.write(ByteBuffer.wrap((editMsg.replaceAll("\\s+", "")).getBytes(StandardCharsets.UTF_8)));
                    inputBuffer.clear();
                }
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }
}
