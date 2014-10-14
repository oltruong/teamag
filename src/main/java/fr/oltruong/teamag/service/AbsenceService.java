package fr.oltruong.teamag.service;

import com.google.common.base.Preconditions;
import fr.oltruong.teamag.exception.DateOverlapException;
import fr.oltruong.teamag.exception.InconsistentDateException;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.validation.AbsenceValidator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class AbsenceService extends AbstractService {


    private static final String DATE_FORMAT = "EEEE dd MMMM";

    @Inject
    private WorkLoadService workLoadService;

    @Inject
    private EmailService emailService;

    public List<Absence> findAllAbsences() {
        return getNamedQueryList("findAllAbsences");
    }

    @SuppressWarnings("unchecked")
    public List<Absence> findAbsencesByMember(Member member) {

        Preconditions.checkArgument(member != null);
        Query query = createNamedQuery("findAbsencesByMember");
        query.setParameter("fmemberId", member.getId());

        return (List<Absence>) query.getResultList();
    }


    @SuppressWarnings("unchecked")
    public List<Absence> findAbsencesByMemberId(Long memberId) {

        Preconditions.checkArgument(memberId != null);
        Query query = createNamedQuery("findAbsencesByMember");
        query.setParameter("fmemberId", memberId);

        return (List<Absence>) query.getResultList();
    }


    public void addAbsence(Absence absence, Long memberId) throws DateOverlapException, InconsistentDateException {


        List<Absence> absencesList = findAbsencesByMemberId(memberId);
        format(absence);
        AbsenceValidator.validate(absence, absencesList);
        absence.setMember(MemberService.getMemberMap().get(memberId));
        persist(absence);

        workLoadService.registerAbsence(absence);
        emailService.sendEmailAdministrator(buildEmailAdd(absence, memberId));

    }


    private void format(Absence absence) {
        if (absence.getEndDate() == null) {
            absence.setEndDate(absence.getBeginDate());
            absence.setEndType(absence.getBeginType());
        }

        if (absence.getBeginDate() == null) {
            absence.setBeginDate(absence.getEndDate());
            absence.setBeginType(absence.getEndType());
        }
    }

    private MailBean buildEmailAdd(Absence absence, Long memberId) {

        MailBean mailBean = new MailBean();
        mailBean.setSubject(MemberService.getMemberMap().get(memberId).getName() + ": ajout d'absence " + buildEmailContent(absence));

        mailBean.setContent(buildEmailContent(absence));
        return mailBean;
    }

    public void deleteAbsence(Absence absence) {
        workLoadService.removeAbsence(absence.getId());
        remove(Absence.class, absence.getId());
        emailService.sendEmailAdministrator(buildEmailDelete(absence));


    }

    public Absence find(Long absenceId) {
        return find(Absence.class, absenceId);
    }


    private MailBean buildEmailDelete(Absence selectedAbsence) {
        MailBean mailBean = new MailBean();
        mailBean.setSubject(selectedAbsence.getMember().getName() + ": suppression d'absence " + buildEmailContent(selectedAbsence));
        mailBean.setContent(buildEmailContent(selectedAbsence));
        return mailBean;
    }

    private String buildEmailContent(Absence absence) {
        return "du " + absence.getBeginDate().toString(DATE_FORMAT) + decrypt(absence.getBeginType()) + " au " + absence.getEndDate().toString(DATE_FORMAT) + decrypt(absence.getBeginType());
    }

    private String decrypt(Integer type) {
        String result = null;
        if (Absence.AFTERNOON_ONLY.equals(type)) {
            result = " apres-midi";
        } else if (Absence.MORNING_ONLY.equals(type)) {
            result = " matin";
        } else {
            result = "";
        }


        return result;
    }

}
