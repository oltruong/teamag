package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.exception.DateOverlapException;
import fr.oltruong.teamag.exception.InconsistentDateException;
import fr.oltruong.teamag.model.Absence;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.service.AbsenceService;
import fr.oltruong.teamag.transformer.AbsenceWebBeanTransformer;
import fr.oltruong.teamag.utils.TestUtils;
import fr.oltruong.teamag.webbean.AbsenceWebBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbsenceEndPointTest extends AbstractEndPointTest {


    @Mock
    private Logger mockLogger;

    @Mock
    private AbsenceService mockAbsenceService;

    private AbsenceEndPoint absenceEndPoint;

    @Before
    public void prepare() {

        super.setup();
        absenceEndPoint = new AbsenceEndPoint();

        TestUtils.setPrivateAttribute(absenceEndPoint, mockAbsenceService, "absenceService");
        TestUtils.setPrivateAttribute(absenceEndPoint, mockLogger, "logger");
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


    @Test
    public void testCreateAbsence() throws Exception {
        AbsenceWebBean absenceWebBean = AbsenceWebBeanTransformer.transform(EntityFactory.createAbsence());
        Response response = absenceEndPoint.createAbsence(randomId, absenceWebBean);
        verify(mockAbsenceService).addAbsence(isA(Absence.class), eq(randomId));
        checkResponseCreated(response);
    }

    @Test
    public void testCreateAbsence_overlap() throws Exception {
        testCreateAbsence_exception(new DateOverlapException(), Response.Status.FORBIDDEN);
    }

    @Test
    public void testCreateAbsence_badRequest() throws Exception {
        testCreateAbsence_exception(new InconsistentDateException(), Response.Status.BAD_REQUEST);
    }


    private void testCreateAbsence_exception(Exception exception, Response.StatusType expectedStatus) throws Exception {
        AbsenceWebBean absenceWebBean = AbsenceWebBeanTransformer.transform(EntityFactory.createAbsence());

        doThrow(exception).when(mockAbsenceService).addAbsence(isA(Absence.class), anyLong());
        Response response = absenceEndPoint.createAbsence(randomId, absenceWebBean);

        verify(mockAbsenceService).addAbsence(isA(Absence.class), eq(randomId));
        verify(mockLogger).warn(isA(String.class), eq(exception));
        checkResponse(response, expectedStatus);
    }


    @Test
    public void testDeleteAbsence() throws Exception {

        Absence absence = EntityFactory.createAbsence();
        Long memberId = EntityFactory.createRandomLong();
        absence.getMember().setId(memberId);
        when(mockAbsenceService.find(eq(randomId))).thenReturn(absence);

        Response response = absenceEndPoint.deleteAbsence(memberId, randomId);
        verify(mockAbsenceService).deleteAbsence(eq(absence));
        checkResponseNoContent(response);
    }

    @Test
    public void testDeleteAbsence_null() throws Exception {
        Response response = absenceEndPoint.deleteAbsence(randomId, randomId);
        verify(mockAbsenceService, never()).deleteAbsence(isA(Absence.class));
        checkResponseNotFound(response);
    }

    @Test
    public void testDeleteAbsence_notauthorized() throws Exception {

        Absence absence = EntityFactory.createAbsence();
        Long memberId = EntityFactory.createRandomLong();
        absence.getMember().setId(memberId);
        when(mockAbsenceService.find(eq(randomId))).thenReturn(absence);

        Response response = absenceEndPoint.deleteAbsence(randomId, randomId);
        verify(mockAbsenceService, never()).deleteAbsence(isA(Absence.class));
        checkResponseForbidden(response);
    }


}