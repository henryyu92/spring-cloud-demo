package example.listener;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

public class AnnotatedBlockedListNotifier {

    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    /**
     *  注解形式的事件监听
     */
    @EventListener
    public void processBlockedListEvent(BlockedListEvent event) {

        String address = event.getAddress();
        String content = event.getContent();

        String message = String.format("send notification to %s, address: %s, content: %s", notificationAddress, address, content);

        System.out.println(message);
    }

    /**
     * 异步事件监听器
     */
    @Async
    @EventListener
    public void asyncProcessBlockedListEvent(BlockedListEvent event){

        String address = event.getAddress();
        String content = event.getContent();

        String message = String.format("send notification to %s, address: %s, content: %s", notificationAddress, address, content);

        System.out.println(message);
    }

    /**
     * 有序的事件监听
     */
    @Order(10)
    @EventListener
    public void orderedProcessBlockedListEvent(BlockedListEvent event){

        String address = event.getAddress();
        String content = event.getContent();

        String message = String.format("send notification to %s, address: %s, content: %s", notificationAddress, address, content);

        System.out.println(message);
    }
}
