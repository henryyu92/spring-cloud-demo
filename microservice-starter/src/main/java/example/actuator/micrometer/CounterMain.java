package example.actuator.micrometer;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.search.Search;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CounterMain {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        Metrics.addRegistry(new SimpleMeterRegistry());
    }

    public static void main(String[] args) {

        Order order1 = new Order("ORDER_ID_1", 100, "CHANNEL_A", LocalDateTime.now());
        Order order2 = new Order("ORDER_ID_2", 200, "CHANNEL_B", LocalDateTime.now());
        Order order3 = new Order("ORDER_ID_2", 200, "CHANNEL_B", LocalDateTime.now());

        createOrder(order1);
        createOrder(order2);
        createOrder(order3);

        Search.in(Metrics.globalRegistry).meters().forEach(meter -> {
            StringBuilder builder = new StringBuilder();
            builder.append("name:")
                    .append(meter.getId().getName())
                    .append(",tags:")
                    .append(meter.getId().getTags())
                    .append(",type:").append(meter.getId().getType())
                    .append(",value:").append(meter.measure());
            System.out.println(builder.toString());
        });

    }

    private static void createOrder(Order order){
        Metrics.counter("order.create",
                "channel", order.getChannel(),
                "createTime", FORMATTER.format(order.getCreateTime()))
                .increment();
    }


    static class Order {
        private String orderId;
        private Integer amount;
        private String channel;
        private LocalDateTime createTime;

        Order(String orderId, Integer amount, String channel, LocalDateTime createTime){
            this.orderId = orderId;
            this.amount = amount;
            this.channel = channel;
            this.createTime = createTime;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }
    }
}
