package foodPanda.service.impl;

import foodPanda.model.Zone;
import foodPanda.repository.ZoneRepository;
import foodPanda.service.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class implements the methods declared in the {@link ZoneService}
 */
@Service
public class ZoneServiceImpl {

    @Autowired
    private ZoneRepository zoneRepository;

    /**
     * Fetches all {@link Zone}s from the DB
     *
     * @return A List of Zones
     */
    public List<Zone> fetchAll() {
        return zoneRepository.findAll();
    }
}
