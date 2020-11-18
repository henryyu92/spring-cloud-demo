package example.listener;

import org.springframework.context.ApplicationListener;

/**
 * 事件监听器
 */
public class BlockedListNotifier implements ApplicationListener<BlockedListEvent> {

    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    @Override
    public void onApplicationEvent(BlockedListEvent event) {
        String address = event.getAddress();
        String content = event.getContent();

        String message = String.format("send notification to %s, address: %s, content: %s", notificationAddress, address, content);

        System.out.println(message);
    }
}
