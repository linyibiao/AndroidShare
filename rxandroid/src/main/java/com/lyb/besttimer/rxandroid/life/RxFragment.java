package com.lyb.besttimer.rxandroid.life;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * rx fragment
 * Created by linyibiao on 2017/4/6.
 */

public class RxFragment extends Fragment {

    protected enum FragmentEvent {
        ATTACH,
        CREATE,
        CREATE_VIEW,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTORY_VIEW,
        DESTORY,
        DETACH,
    }

    protected final BehaviorSubject<FragmentEvent> lifeSubject = BehaviorSubject.create();


    public <T> ObservableTransformer<T, T> bindUntilEvent(Class<T> tClass, final FragmentEvent bindEvent) {

        final Observable<FragmentEvent> observable = lifeSubject.filter(new Predicate<FragmentEvent>() {
            @Override
            public boolean test(FragmentEvent event) throws Exception {
                return event.equals(bindEvent);
            }
        });

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.takeUntil(observable);
            }
        };
    }

    private Disposable bindSubscription;

    private void bindEvent() {
        if (bindSubscription != null && !bindSubscription.isDisposed()) {
            unBindEvent();
        }
        bindSubscription = lifeSubject.subscribe(new Consumer<FragmentEvent>() {
            @Override
            public void accept(FragmentEvent fragmentEvent) throws Exception {
                eventCall(fragmentEvent);
            }
        });
    }

    private void unBindEvent() {
        if (bindSubscription != null && !bindSubscription.isDisposed()) {
            bindSubscription.dispose();
        }
    }

    protected void eventCall(RxFragment.FragmentEvent bindEvent) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bindEvent();
        lifeSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifeSubject.onNext(FragmentEvent.CREATE_VIEW);

    }

    @Override
    public void onStart() {
        super.onStart();
        lifeSubject.onNext(FragmentEvent.START);

    }

    @Override
    public void onResume() {
        super.onResume();
        lifeSubject.onNext(FragmentEvent.RESUME);

    }


    @Override
    public void onPause() {
        super.onPause();
        lifeSubject.onNext(FragmentEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifeSubject.onNext(FragmentEvent.STOP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifeSubject.onNext(FragmentEvent.DESTORY);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifeSubject.onNext(FragmentEvent.DESTORY_VIEW);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifeSubject.onNext(FragmentEvent.DETACH);
        unBindEvent();
    }

}
