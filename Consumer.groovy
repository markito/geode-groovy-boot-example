
@GrabResolver(name='asf-snapshots', root="https://repository.apache.org/content/repositories/snapshots")
@Grab(group="org.apache.geode", module = "gemfire-core", version = "1.0.0-incubating-SNAPSHOT")


import com.gemstone.gemfire.cache.client.ClientCacheFactory
import com.gemstone.gemfire.cache.client.ClientRegionShortcut
import com.gemstone.gemfire.cache.query.QueryException
import com.gemstone.gemfire.pdx.JSONFormatter
import com.gemstone.gemfire.pdx.PdxInstance
import com.gemstone.gemfire.pdx.internal.PdxInstanceImpl
import com.gemstone.gemfire.pdx.ReflectionBasedAutoSerializer
import org.springframework.beans.factory.annotation.Value

import java.util.logging.Logger
import java.util.concurrent.* 

import groovy.transform.Canonical

/**
 * Created by wmarkito on 8/20/15.
 */

@Configuration
class Consumer implements CommandLineRunner {

    @Value("\${locatorHost}")
    def locatorHost

    @Value("\${locatorPort}")
    def int locatorPort

    def random = java.util.Random.newInstance()
    def logger = Logger.getLogger(Consumer.class.getName())

    final ENTRIES=100

    @Override
    void run(String... args) {
        logger.info("Locator host: $locatorHost")
        logger.info("Locator port: $locatorPort")

	def cache = new ClientCacheFactory()
                .addPoolLocator(locatorHost, locatorPort)
                .setPdxSerializer(new ReflectionBasedAutoSerializer("Consumer$Person"))
                .create()

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate( 
        { readAll(cache) }, 
        0,5, TimeUnit.SECONDS) 
  
    }

    def readAll(cache) {

        cache.with {
            def personRegion = createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY).create("Person")
            
            println "-----------------------"
            1.upto(ENTRIES) {
                println personRegion.get(it)
            }
        }
    }
}

@Canonical
class Person {

    int id    
    String name
    String lastname
    String email
    String city
}
