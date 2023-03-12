package team.codemonsters.ddd.toolkit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
/**
 * Хороший-плохой пример
 */
//@Service
//public class Subscriber {
//    @Autowired private SubscriberGateway subscriberGateway;
//
//    public Optional<Subscriber> saveSubscriberWithMnp(Map<String, String> request) {
//        return saveSubscriber(request)
//                .flatMap(savedSubscriber -> andThenSaveMnp(savedSubscriber, request));
//    }
//
//    private Optional<Subscriber> andThenSaveMnp(Subscriber savedSubscriber, Map<String, String> request) {
//        if (needToSaveMnp(request)) {
//            return subscriberGateway.saveMnp(toMnp(request))
//                    .map(m -> savedSubscriber);
//        } else {
//            return Optional.of(savedSubscriber);
//        }
//    }
//
//    private Mnp toMnp(Map<String, String> context) {..}
//
//    private boolean needToSaveMnp(Map<String, String> context) {..}
//
//    private Optional<Subscriber> saveSubscriber(Map<String, String> context) {..}
//}
//
//class Mnp {}
//
//class SubscriberGateway {
//   Optional<Mnp> saveMnp(Mnp mnp) {}
//}