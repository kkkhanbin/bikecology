package kz.bikecology.controllers;

import kz.bikecology.data.dao.facility.FacilityDAO;
import kz.bikecology.data.dao.facilityFuels.FacilityFuelsDAO;
import kz.bikecology.data.dao.record.RecordDAO;
import kz.bikecology.services.reportBuilder.ReportBuilder;

public class BaseController {
    public final ReportBuilder reportBuilder = new ReportBuilder();

    public final FacilityDAO facilityDAO = new FacilityDAO();
    public final RecordDAO recordDAO = new RecordDAO();
    public final FacilityFuelsDAO facilityFuelsDAO = new FacilityFuelsDAO();
}
