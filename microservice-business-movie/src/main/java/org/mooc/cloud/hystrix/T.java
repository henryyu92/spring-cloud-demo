package org.mooc.cloud.hystrix;

import com.netflix.hystrix.*;
import rx.Observable;

public class T extends HystrixObservableCommand<Integer> {
    protected T(HystrixCommandGroupKey group) {
        super(group);
    }

    @Override
    protected Observable<Integer> construct() {
        return null;
    }
}
