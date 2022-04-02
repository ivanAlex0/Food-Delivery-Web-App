package foodPanda.service.impl;

import foodPanda.model.Zone;
import foodPanda.repository.ZoneRepository;
import foodPanda.service.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneServiceImpl{

    @Autowired
    private ZoneRepository zoneRepository;

    public List<Zone> fetchAll() {
        return zoneRepository.findAll();
    }
}
