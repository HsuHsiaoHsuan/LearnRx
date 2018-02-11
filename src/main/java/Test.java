import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] freeman) {
        Observable<String> mString = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");
//        mString.subscribe(System.out::println);
        mString.map(s -> s.charAt(0)).subscribe(System.out::println);


        Observable<String> source = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                try {
                    e.onNext("Alpha");
                    e.onNext("Beta");
                    e.onNext("Gamma");
                    e.onNext("Delta");
                    e.onNext("Epsilon");
                    e.onComplete();
                } catch (Throwable t) {
                    e.onError(t);
                }
            }
        });

        source.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });

        Observable<Integer> lengths = source.map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return s.length();
            }
        });

        Observable<Integer> filtered = lengths.filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer >= 5;
            }
        });

        filtered.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer);
            }
        });


        ConnectableObservable<String> connSource = Observable.just("Alpha-", "Beta-", "Gamma-", "Delta-", "Epsilon-")
                .publish();
        connSource.subscribe(System.out::println);
        connSource.map(String::length).subscribe(System.out::println);
        connSource.connect();

        Maybe<Integer> presentSource = Maybe.just(100);
        presentSource.subscribe(s -> System.out.println("Process 1 received: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("Process 1 done!"));
    }
}
