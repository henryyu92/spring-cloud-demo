package example.quartz.configuration;

//@ConfigurationProperties(prefix = "quartz")
public class QuartzConfiguration {

    private MainConfiguration main;

    private ThreadPoolConfiguration threadPool;

    private ClusterConfiguration cluster;

    static class MainConfiguration{

    }

    static class ThreadPoolConfiguration{

    }

    static class ClusterConfiguration{

    }
}
