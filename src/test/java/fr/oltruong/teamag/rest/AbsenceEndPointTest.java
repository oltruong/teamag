package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.service.AbsenceService;
import fr.oltruong.teamag.transformer.AbsenceWebBeanTransformer;
import fr.oltruong.teamag.utils.TestUtils;
import fr.oltruong.teamag.webbean.AbsenceWebBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbsenceEndPointTest extends AbstractEndPointTest {


    @Mock
    private AbsenceService mockAbsenceService;


    private AbsenceEndPoint absenceEndPoint;

    @Before
    public void prepare() {

        super.setup();
        absenceEndPoint = new AbsenceEndPoint();

        TestUtils.setPrivateAttribute(absenceEndPoint, mockAbsenceService, "absenceService");
    }

    @Test
    public void testGetAbsences() throws Exception {
        Long randomId = EntityFactory.createRandomLong();

        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);

        List<AbsenceWebBean> absenceWebBeanTransformed = AbsenceWebBeanTransformer.transformList(absenceList);

        when(mockAbsenceService.findAbsencesByMemberId(eq(randomId))).thenReturn(absenceList);


        Response response = absenceEndPoint.getAbsences(randomId);

        checkResponseOK(response);


        assertThat(response.getEntity()).isOfAnyClassIn(ArrayList.class);

        List<AbsenceWebBean> absenceWebBeans = (List<AbsenceWebBean>) response.getEntity();


        assertThat(absenceWebBeans).isNotNull().usingFieldByFieldElementComparator().containsExactlyElementsOf(absenceWebBeanTransformed);

        verify(mockAbsenceService).findAbsencesByMemberId(eq(randomId));

    }
}