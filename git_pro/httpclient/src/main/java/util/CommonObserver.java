package util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CommonObserver implements Observer {
    Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable e) {
        disposable();
    }

    @Override
    public void onComplete() {
        disposable();
    }

    protected void disposable() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
