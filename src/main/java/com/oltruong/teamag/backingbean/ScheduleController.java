package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.service.AbsenceService;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.transformer.ScheduleEventTransformer;
import com.oltruong.teamag.model.Member;
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
    private AbsenceService absenceService;

    @Inject
    private MemberService memberService;

    private static final String VIEWNAME = "schedule";

    private List<Member> members;

    public String init() {

        fillEventModel();
        return VIEWNAME;
    }

    private void fillEventModel() {

        eventModel = new DefaultScheduleModel();
        members = memberService.findMembers();
        fillAbsences();
        fillDaysOff();

    }

    private void fillDaysOff() {
        List<ScheduleEvent> eventDaysOffList = ScheduleEventTransformer.getDaysOff(getMessageManager());
        fillEventList(eventDaysOffList);
    }

    private void fillAbsences() {
        List<Absence> absenceList = absenceService.findAllAbsences();
        List<ScheduleEvent> eventList = ScheduleEventTransformer.convertAbsenceList(absenceList, getMessageManager());
        fillEventList(eventList);
    }

    private void fillEventList(List<ScheduleEvent> eventList) {
        eventList.forEach(event -> eventModel.addEvent(event));
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
