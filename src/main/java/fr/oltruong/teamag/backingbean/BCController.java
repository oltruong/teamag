package fr.oltruong.teamag.backingbean;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.Activity;
import fr.oltruong.teamag.model.BusinessCase;
import fr.oltruong.teamag.service.ActivityService;
import fr.oltruong.teamag.service.BusinessCaseService;
import fr.oltruong.teamag.utils.MessageManager;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.RowEditEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Olivier Truong
 */
@ManagedBean(name = "bcController")
@SessionScoped
public class BCController extends Controller {


    @Inject
    private ActivityService activityService;

    @Inject
    private BusinessCaseService businessCaseService;

    @Inject
    private BusinessCase bc;

    @Inject
    private Activity activity;

    private List<BusinessCase> bcList = Lists.newArrayList();

    private List<Activity> activityList = Lists.newArrayList();

    private int tabIndex = 0;

    private static final String VIEWNAME = "businesscases";


    private Double total;

    public String init() {
        setBcList(this.businessCaseService.findAll());
        setActivityList(this.activityService.findActivities());
        bc = new BusinessCase();

        computeTotal();
        return VIEWNAME;
    }

    public String doCreateBC() {
        this.tabIndex = 0;

        getLogger().info("Creation of a business case");

        if (StringUtils.isBlank(this.bc.getName())) {
            getMessageManager().displayMessageWithDescription(MessageManager.ERROR, "impossibleAdd", "provideBCNumber");
        } else {
            try {
                this.businessCaseService.create(this.bc);
                getMessageManager().displayMessageWithDescription(MessageManager.INFORMATION, "updated", "businessCaseCreated", this.bc.getIdentifier(), this.bc.getName());
                this.bc = new BusinessCase();
            } catch (ExistingDataException e) {
                getLogger().warn("BusinessCase already exists");
                getMessageManager().displayMessageWithDescription(MessageManager.ERROR, "impossibleAdd", "existingBC", this.bc.getIdentifier());
            }


        }

        return init();

    }


    public Double getTotal() {
        return total;
    }

    private void computeTotal() {
        total = 0d;
        for (BusinessCase businessCase : bcList) {
            total += businessCase.getAmount();
        }

    }

    public String doCreateActivity() {
        this.tabIndex = 1;
        this.getLogger().info("Creation of an activity");

        if (StringUtils.isBlank(this.activity.getName()) || this.activity.getBc() == null || this.activity.getBc().getId() == null) {
            getMessageManager().displayMessageWithDescription(MessageManager.ERROR, "impossibleAdd", "provideNameAndBC");
        } else {

            try {
                this.activityService.createActivity(this.activity);
                getMessageManager().displayMessageWithDescription(MessageManager.INFORMATION, "updated", "activityCreated");
                this.activity = new Activity();
            } catch (ExistingDataException e) {
                this.getLogger().warn("Existing activity");
                getMessageManager().displayMessageWithDescription(MessageManager.ERROR, "impossibleAdd", "existingActivity");
            }


        }

        return init();
    }


    public void onEditBC(RowEditEvent event) {
        BusinessCase bcUpdated = (BusinessCase) event.getObject();

        businessCaseService.update(bcUpdated);
        getMessageManager().displayMessage(MessageManager.INFORMATION, "businessCaseUpdated", bcUpdated.getName());
        computeTotal();

    }

    public void onCancelBC(RowEditEvent event) {
    }

    public BusinessCase getBc() {
        return this.bc;
    }

    public void setBc(BusinessCase bc) {
        this.bc = bc;
    }

    public List<BusinessCase> getBcList() {
        return this.bcList;
    }

    public void setBcList(List<BusinessCase> bcList) {
        this.bcList = bcList;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Activity> getActivityList() {
        return this.activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public int getTabIndex() {
        return this.tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

}
