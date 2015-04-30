package com.oltruong.teamag.service;

import com.google.common.base.Preconditions;
import com.oltruong.teamag.exception.DateOverlapException;
import com.oltruong.teamag.exception.InconsistentDateException;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.AbsenceDay;
import com.oltruong.teamag.transformer.AbsenceDayTransformer;
import com.oltruong.teamag.validation.AbsenceValidator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AbsenceService extends AbstractService<Absence> {

    private static final String DATE_FORMAT = "EEEE dd MMMM";

    @Inject
    private AbsenceDayService absenceDayService;

    @Inject
    private EmailService emailService;

    @Inject
    private WorkService workService;


    public List<Absence> findAllAbsences() {
        return getTypedQueryList("findAllAbsences");
    }

    public List<Absence> findAbsencesByMember(Long memberId) {
        Preconditions.checkArgument(memberId != null);
        TypedQuery<Absence> query = createTypedQuery("findAbsencesByMember");
        query.setParameter("fmemberId", memberId);
        return query.getResultList();
    }

    public void addAbsence(Absence absence, Long memberId) throws DateOverlapException, InconsistentDateException {
        List<Absence> absencesList = findAbsencesByMember(memberId);
        format(absence);
        AbsenceValidator.validate(absence, absencesList);
        absence.setMember(MemberService.getMemberMap().get(memberId));
        persist(absence);

        registerAbsence(absence);
        emailService.sendEmailAdministrator(buildEmailAdd(absence, memberId));

    }


    public void registerAbsence(Absence newAbsence) {
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(newAbsence);
        absenceDayList.forEach(absenceDay -> {
            absenceDayService.persist(absenceDay);
            workService.updateWorkAbsence(absenceDay);
        });
    }


    public void reloadAllAbsenceDay() {
        absenceDayService.removeAll();
        List<Absence> absenceList = findAllAbsences();
        if (absenceList != null) {
            absenceList.forEach(absence -> registerAbsence(absence));
        }
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

    @Override
    public void remove(Long absenceId) {
        Absence absence = find(absenceId);
        if (absence != null) {
            absenceDayService.remove(absenceId);
            super.remove(absence);
            emailService.sendEmailAdministrator(buildEmailDelete(absence));
        }
    }

    @Override
    Class<Absence> entityProvider() {
        return Absence.class;
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
