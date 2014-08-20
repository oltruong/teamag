package fr.oltruong.teamag.service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.model.Member;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.WorkRealized;
import fr.oltruong.teamag.utils.TeamagConstants;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * @author Olivier Truong
 */
@Stateless
public class WorkRealizedService extends AbstractService {

    @Inject
    private WorkLoadService workLoadService;

    @SuppressWarnings("unchecked")
    public List<WorkRealized> getAllWorkRealized() {
        return getNamedQueryList("findAllWorkRealized");
    }

    @SuppressWarnings("unchecked")
    public List<WorkRealized> getWorkRealizedbyMember(Long memberId) {
        Query query = createNamedQuery("findAllWorkRealizedByMember");
        query.setParameter("fMemberId", memberId);
        return query.getResultList();
    }

    public void createOrUpdate(List<WorkRealized> workRealizedList) {
        if (workRealizedList != null) {

            List<Long> idMemberChangedList = Lists.newArrayList();

            workRealizedList.forEach(workRealized -> {


                if (!idMemberChangedList.contains(workRealized.getMemberId())) {
                    idMemberChangedList.add(workRealized.getMemberId());
                }

                if (workRealized.getRealized() == 0) {
                    if (workRealized.getId() != null) {
                        remove(WorkRealized.class, workRealized.getId());
                    }
                } else if (workRealized.getId() != null) {
                    merge(workRealized);
                } else {
                    persist(workRealized);
                }
            });

            updateRealizedWorkLoad(idMemberChangedList);
        }

    }

    private void updateRealizedWorkLoad(List<Long> idMemberChangedList) {


        Table<Member, BusinessCase, Double> values = HashBasedTable.create();

        Map<Long, Member> memberMap = MemberService.getMemberMap();

        if (idMemberChangedList != null) {
            for (Long aLong : idMemberChangedList) {
                Member member = memberMap.get(aLong);
                Map<BusinessCase, Double> bcMap = buildBusinessCaseMap(aLong);
                bcMap.forEach((bc, value) -> values.put(member, bc, value));
            }
        }

        workLoadService.updateWorkLoadWithRealized(values);

    }

    private Map<BusinessCase, Double> buildBusinessCaseMap(Long aLong) {
        Map<BusinessCase, Double> bcMap = Maps.newHashMap();

        List<WorkRealized> workRealizedList = getWorkRealizedbyMember(aLong);
        for (WorkRealized workRealized : workRealizedList) {
            Task task = find(Task.class, workRealized.getTaskId());
            BusinessCase businessCase = findBusinessCase(task);
            if (businessCase != null) {

                if (!bcMap.containsKey(businessCase)) {
                    bcMap.put(businessCase, workRealized.getRealized() / TeamagConstants.MONTH_DAYS_RATIO);
                } else {
                    Double value = bcMap.get(businessCase);
                    value += workRealized.getRealized() / TeamagConstants.MONTH_DAYS_RATIO;
                    bcMap.put(businessCase, value);
                }
            }
        }
        return bcMap;
    }

    private BusinessCase findBusinessCase(Task task) {

        if (task.getActivity() != null) {
            return task.getActivity().getBc();
        } else if (task.getTask() != null) {
            return findBusinessCase(task.getTask());
        }
        return null;
    }
}
