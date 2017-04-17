package com.lyb.besttimer.rxandroid.life;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

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


    public <T> Observable.Transformer<T, T> bindUntilEvent(Class<T> tClass, final FragmentEvent bindEvent) {
        final Observable<FragmentEvent> observable = lifeSubject.takeFirst(new Func1<FragmentEvent, Boolean>() {
            @Override
            public Boolean call(FragmentEvent event) {
                return event.equals(bindEvent);
            }
        });

        return new Observable.Transformer<T, T>() {

            @Override
            public Observable<T> call(Observable<T> sourceOb) {
                return sourceOb.takeUntil(observable);
            }
        };
    }

    private Subscription bindSubscription;

    private void bindEvent() {
        if (bindSubscription != null && !bindSubscription.isUnsubscribed()) {
            unBindEvent();
        }
        bindSubscription = lifeSubject.subscribe(new Action1<RxFragment.FragmentEvent>() {
            @Override
            public void call(RxFragment.FragmentEvent fragmentEvent) {
                eventCall(fragmentEvent);
            }
        });
    }

    private void unBindEvent() {
        if (bindSubscription != null && !bindSubscription.isUnsubscribed()) {
            bindSubscription.unsubscribe();
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
