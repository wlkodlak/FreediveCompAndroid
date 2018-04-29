package net.wilczak.freedivecomp.android.rxutils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class ValueCacheObservable<T, R> extends Observable<T> {
    private final CacheState<T> state;
    private final InvalidationHandler<T, R> invalidation;

    public ValueCacheObservable(Observable<T> upstream, Observable<R> onChange) {
        this.state = new CacheState<>(upstream);
        this.invalidation = new InvalidationHandler<>(onChange, state);
    }

    public static <T, R> ObservableTransformer<T, T> transform(PublishSubject<R> onChange) {
        return upstream -> new ValueCacheObservable<>(upstream, onChange);
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        ReplayDisposable<T> downstreamSubscription = new ReplayDisposable<>(state, observer);
        observer.onSubscribe(downstreamSubscription);
        if (downstreamSubscription.isDisposed()) return;

        state.addChild(downstreamSubscription);
        state.ensureConnected();
        invalidation.ensureConnected();
    }

    private static class CacheState<T> implements Observer<T> {
        private final Observable<T> upstream;
        private final AtomicBoolean connected;
        private final ConcurrentHashMap<Integer, ReplayDisposable<T>> downstreams;
        private final AtomicInteger id;
        private final AtomicReference<T> cachedValue;
        private final AtomicReference<Disposable> upstreamSubscription;

        public CacheState(Observable<T> upstream) {
            this.upstream = upstream;
            this.connected = new AtomicBoolean();
            this.downstreams = new ConcurrentHashMap<>();
            this.id = new AtomicInteger();
            this.cachedValue = new AtomicReference<>();
            this.upstreamSubscription = new AtomicReference<>();
        }

        public void ensureConnected() {
            if (connected.get() || connected.getAndSet(true)) return;
            upstream.subscribe(this);
        }

        public void invalidate() {
            if (!connected.get()) return;
            Disposable subscription = upstreamSubscription.getAndSet(null);
            if (subscription == null) return;
            subscription.dispose();
            upstream.subscribe(this);
        }

        public void addChild(ReplayDisposable<T> child) {
            Integer childId = id.incrementAndGet();
            child.setId(childId);
            T pendingValue = cachedValue.get();
            if (pendingValue != null) {
                child.onNext(pendingValue);
            }
            downstreams.put(childId, child);
        }

        public void removeChild(ReplayDisposable<T> child) {
            downstreams.remove(child.getId());
            if (downstreams.size() > 0) return;

            if (!connected.get()) return;
            Disposable subscription = upstreamSubscription.getAndSet(null);
            if (subscription == null) return;
            subscription.dispose();
            connected.set(false);

            if (downstreams.size() != 0) ensureConnected();
        }

        @Override
        public void onSubscribe(Disposable d) {
            if (!upstreamSubscription.compareAndSet(null, d)) {
                d.dispose();
            }
        }

        @Override
        public void onNext(T t) {
            cachedValue.set(t);
            for (ReplayDisposable<T> replay : downstreams.values()) {
                replay.onNext(t);
            }
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
        }
    }

    private static class InvalidationHandler<T, R> implements Observer<R> {
        private final CacheState<T> state;
        private final Observable<R> onChange;
        private final AtomicBoolean connected;

        public InvalidationHandler(Observable<R> onChange, CacheState<T> state) {
            this.state = state;
            this.onChange = onChange;
            this.connected = new AtomicBoolean();
        }

        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onNext(R r) {
            state.invalidate();
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
        }

        public void ensureConnected() {
            if (!connected.get() && !connected.getAndSet(true)) {
                onChange.subscribe(this);
            }
        }
    }

    private static class ReplayDisposable<T> implements Disposable {
        private final CacheState<T> state;
        private final Observer<? super T> observer;
        private final AtomicBoolean disposed;
        private Integer id;

        private ReplayDisposable(CacheState<T> state, Observer<? super T> observer) {
            this.state = state;
            this.observer = observer;
            this.disposed = new AtomicBoolean();
        }

        public Integer getId() {
            return id;
        }

        public ReplayDisposable<T> setId(Integer id) {
            this.id = id;
            return this;
        }

        public void onNext(T value) {
            if (!isDisposed()) {
                observer.onNext(value);
            }
        }

        @Override
        public void dispose() {
            if (!disposed.getAndSet(true)) {
                state.removeChild(this);
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }
    }
}
