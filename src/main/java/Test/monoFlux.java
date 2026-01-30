package Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class monoFlux {

    public Mono<String> getData() {
        return Mono.just("Hello, MONO Reactive World!");//.log(); //log will give detailed info about the data flow
    }
    public Flux<String> getMultipleData() {
       /* List<String> listOfString = List.of("Hello", "from", "Reactive", "Programming");
        return Flux.fromIterable(listOfString);*/
        Flux<String> stringFlux = Flux.just("Hello", "from", "Reactive", "Programming");
        return stringFlux.map(String::toUpperCase);
    }
    public static void main(String[] args) {
        System.out.println("Hello World from FLUX Reactive Programming Tutorial!");

        monoFlux tutorial = new monoFlux();
        tutorial.getData().subscribe(System.out::println);
        tutorial.getMultipleData().subscribe(System.out::println);
    }
}
