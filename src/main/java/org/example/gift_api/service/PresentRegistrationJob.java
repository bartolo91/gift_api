package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@RequiredArgsConstructor
@Slf4j
public class PresentRegistrationJob {

    /**
     * napisz mechanizm który będzie cyklicznie przetwarzał informację na temat prezentów, które ceną przekraczają 100zł.
     * <p>
     * dla uproszczenia możemy przyjąć, że przetwarzane mają być za każdym razem wszystkie prezenty z bazy
     * (na zajęciach możemy przegadać ograniczanie takiego zbioru)
     * <p>
     * informacje o prezentach mają być przetwarzane w taki sposób by dla każdego dziecka informacja była podawana tylko raz
     * (o wszystkich jego spełniających warunki prezentach na raz)
     * <p>
     * normalnie byśmy chcieli, aby taka informacja była przekazywana gdzieś mailem albo coś, ale możemy zrobić logowanie takiej informacji,
     * żeby mieć uproszczenie, a jednak widzieć co się dzieje
     * <p>
     * ostateczny test powinien obejmować ograniczenie pamięci aplikacji do 200MB oraz przetwarzanie z bazy informacji dla przynajmniej
     * 2mln dzieciaków, po 2-3 prezenty każdy, z czego połowa prezentów powinna być powyżej wskazanej granicy
     **/

    private final PresentRegistrationService presentRegistrationService;

    @SchedulerLock(
            name = "${scheduler.present-processing.scheduler-lock-name}",
            lockAtMostFor = "${scheduler.present-processing.lock-at-most-for}",
            lockAtLeastFor = "${scheduler.present-processing.lock-at-least-for}")
    @Scheduled(cron = "${scheduler.present-processing.cron}")
    public void startProcessing() throws UnknownHostException {

        String host = InetAddress.getLocalHost().getHostName();

        log.info("Processing started...");
        log.info("Processing started on instance: {}", host);

        presentRegistrationService.process();
    }
}
