package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.transformer.ScheduleEventTransformer;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 * @author Olivier Truong
 */
@SessionScoped
@ManagedBean
public class ScheduleController extends Controller {

    private ScheduleModel eventModel;

    @Inject
    private AbsenceEJB absenceEJB;

    private static final String VIEWNAME = "schedule";

    public String init() {

        fillEventModel();
        return VIEWNAME;
    }

    private void fillEventModel() {

        eventModel = new DefaultScheduleModel();
        fillAbsences();
        fillDaysOff();

    }

    private void fillDaysOff() {
        List<ScheduleEvent> eventDaysOffList = ScheduleEventTransformer.getDaysOff(getMessageManager());
        fillEventList(eventDaysOffList);
    }

    private void fillAbsences() {
        List<Absence> absenceList = absenceEJB.findAllAbsences();
        List<ScheduleEvent> eventList = ScheduleEventTransformer.convertAbsenceList(absenceList, getMessageManager());
        fillEventList(eventList);
    }

    private void fillEventList(List<ScheduleEvent> eventList) {
        for (ScheduleEvent event : eventList) {
            eventModel.addEvent(event);
        }
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

}
