package example.actuator.source;

/**
 * 监控源
 */

@FunctionalInterface
public interface Source<T> {
    /**
     * 收集监控信息
     * @return
     */
    T collect();
}
