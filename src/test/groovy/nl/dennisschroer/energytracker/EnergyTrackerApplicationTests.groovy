package nl.dennisschroer.energytracker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest
class EnergyTrackerApplicationTest extends Specification {
	@Autowired
	ApplicationContext context

    def "context loads"() {
        expect:
		context != null
    }
}
