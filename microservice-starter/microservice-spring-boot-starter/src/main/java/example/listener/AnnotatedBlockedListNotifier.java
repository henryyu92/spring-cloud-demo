package example.listener;

import org.springframework.context.event.EventListener;

public class AnnotatedBlockedListNotifier {

    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    @EventListener
    public void processBlockedListEvent(BlockedListEvent event) {

        String address = event.getAddress();
        String content = event.getContent();

        String message = String.format("send notification to %s, address: %s, content: %s", notificationAddress, address, content);

        System.out.println(message);
    }
}
