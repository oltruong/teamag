package fr.oltruong.teamag.ejb;

import com.google.common.base.Preconditions;
import fr.oltruong.teamag.entity.Absence;
import fr.oltruong.teamag.entity.AbsenceDay;
import fr.oltruong.teamag.transformer.AbsenceDayTransformer;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by m405640 on 11/03/14.
 */
@Stateless
public class WorkLoadEJB extends AbstractEJB {


    public List<AbsenceDay> getAllAbsenceDay() {
        return getEntityManager().createNamedQuery("findAllAbsenceDays").getResultList();

    }

    public void removeAbsence(Long id) {

        Preconditions.checkArgument(id != null);
        Query query = getEntityManager().createNamedQuery("findAbsenceDayByAbsenceId");
        query.setParameter("fAbsenceId", id);


        List<AbsenceDay> absenceDayList = query.getResultList();
        for (AbsenceDay absenceDay : absenceDayList) {
            getEntityManager().remove(absenceDay);
        }

    }

    public void registerAbsence(Absence newAbsence) {
        List<AbsenceDay> absenceDayList = AbsenceDayTransformer.transformAbsence(newAbsence);
        for (AbsenceDay absenceDay : absenceDayList) {
            getEntityManager().persist(absenceDay);
        }
    }

    public void reloadAllAbsenceDay() {
        List<AbsenceDay> absenceDayList = getAllAbsenceDay();
        for (AbsenceDay absenceDay : absenceDayList) {
            getEntityManager().remove(absenceDay);
        }

        List<Absence> absenceList = findAllAbsences();
        for (Absence absence : absenceList) {
            registerAbsence(absence);
        }
    }

    private List<Absence> findAllAbsences() {
        return getEntityManager().createNamedQuery("findAllAbsences").getResultList();
    }


}
