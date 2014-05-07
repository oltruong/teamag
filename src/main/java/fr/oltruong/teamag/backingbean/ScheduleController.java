package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.ejb.AbsenceEJB;
import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.transformer.ScheduleEventTransformer;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Olivier Truong
 */
@SessionScoped
@ManagedBean
public class ScheduleController extends Controller {

    private ScheduleModel eventModel;

    @Inject
    private AbsenceEJB absenceEJB;

    @Inject
    private MemberEJB memberEJB;

    private static final String VIEWNAME = "schedule";

    private List<Member> members;

    public String init() {

        fillEventModel();
        return VIEWNAME;
    }

    private void fillEventModel() {

        eventModel = new DefaultScheduleModel();
        members = memberEJB.findMembers();
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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
