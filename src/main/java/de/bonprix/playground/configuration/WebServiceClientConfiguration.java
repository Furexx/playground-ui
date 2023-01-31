package de.bonprix.playground.configuration;

import de.bonprix.service.car.CarService;
import de.bonprix.service.parking.ParkingService;
import de.bonprix.service.parkingplace.ParkingPlaceService;
import de.bonprix.service.parkingzone.ParkingZoneService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.bonprix.jersey.ClientFactoryConfig;
import de.bonprix.jersey.ClientFactoryConfig.ClientSideLogLevel;
import de.bonprix.jersey.JaxRsClientFactory;


@Configuration
public class WebServiceClientConfiguration {

    @Value("${webservice.url.playground}")
    private String webservicePlayground = "playground";


    @Bean
    public ParkingService parkingService(JaxRsClientFactory jaxRsClientFactory) {
        ClientFactoryConfig config = new ClientFactoryConfig();
        config.setClientSideLogging(ClientSideLogLevel.METHOD_TIME);
        config.setAddAuthKeyAuthentication(true);
        config.setAddBasicAuthentication(true);

        return jaxRsClientFactory.createClient(webservicePlayground, ParkingService.class, config);
    }

    @Bean
    public ParkingZoneService parkingZoneService(JaxRsClientFactory jaxRsClientFactory) {
        ClientFactoryConfig config = new ClientFactoryConfig();
        config.setClientSideLogging(ClientSideLogLevel.METHOD_TIME);
        config.setAddAuthKeyAuthentication(true);
        config.setAddBasicAuthentication(true);

        return jaxRsClientFactory.createClient(webservicePlayground, ParkingZoneService.class, config);
    }

    @Bean
    public ParkingPlaceService parkingPlaceService(JaxRsClientFactory jaxRsClientFactory) {
        ClientFactoryConfig config = new ClientFactoryConfig();
        config.setClientSideLogging(ClientSideLogLevel.METHOD_TIME);
        config.setAddAuthKeyAuthentication(true);
        config.setAddBasicAuthentication(true);

        return jaxRsClientFactory.createClient(webservicePlayground, ParkingPlaceService.class, config);
    }

    @Bean
    public CarService carService(JaxRsClientFactory jaxRsClientFactory) {
        ClientFactoryConfig config = new ClientFactoryConfig();
        config.setClientSideLogging(ClientSideLogLevel.METHOD_TIME);
        config.setAddAuthKeyAuthentication(true);
        config.setAddBasicAuthentication(true);

        return jaxRsClientFactory.createClient(webservicePlayground, CarService.class, config);
    }

}
