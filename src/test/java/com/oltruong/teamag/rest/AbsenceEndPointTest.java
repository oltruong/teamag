package com.oltruong.teamag.rest;

import com.oltruong.teamag.exception.DateOverlapException;
import com.oltruong.teamag.exception.InconsistentDateException;
import com.oltruong.teamag.model.Absence;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.AbsenceService;
import com.oltruong.teamag.transformer.AbsenceWebBeanTransformer;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.AbsenceWebBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
        TestUtils.setPrivateAttribute(absenceEndPoint, AbstractEndPoint.class, mockLogger, "logger");
        TestUtils.setPrivateAttribute(absenceEndPoint, AbstractEndPoint.class, mockLogger, "LOGGER");

    }


    @Test
    public void testGetAllAbsences() throws Exception {


        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);

        List<AbsenceWebBean> absenceWebBeanTransformed = AbsenceWebBeanTransformer.transformList(absenceList);
        when(mockAbsenceService.findAllAbsences()).thenReturn(absenceList);

        Response response = absenceEndPoint.getAllAbsences();
        checkResponseOK(response);

        assertThat(response.getEntity()).isOfAnyClassIn(ArrayList.class);
        List<AbsenceWebBean> absenceWebBeans = (List<AbsenceWebBean>) response.getEntity();

        assertThat(absenceWebBeans).isNotNull().usingFieldByFieldElementComparator().containsExactlyElementsOf(absenceWebBeanTransformed);
        verify(mockAbsenceService).findAllAbsences();

    }

    @Test
    public void testGetDaysOff() {
        Response response = absenceEndPoint.getDaysOff();
        checkResponseOK(response);

        assertThat(response.getEntity()).isOfAnyClassIn(ArrayList.class);

        List<AbsenceWebBean> absenceWebBeans = (List<AbsenceWebBean>) response.getEntity();
        assertThat(absenceWebBeans).usingFieldByFieldElementComparator().containsExactlyElementsOf(AbsenceWebBeanTransformer.transformListfromDays(CalendarUtils.getListDaysOff()));
    }

    @Test
    public void testGetAbsences() throws Exception {


        List<Absence> absenceList = EntityFactory.createList(EntityFactory::createAbsence);

        List<AbsenceWebBean> absenceWebBeanTransformed = AbsenceWebBeanTransformer.transformList(absenceList);
        when(mockAbsenceService.findAbsencesByMember(eq(randomId))).thenReturn(absenceList);

        Response response = absenceEndPoint.getAbsences(randomId);
        checkResponseOK(response);

        assertThat(response.getEntity()).isOfAnyClassIn(ArrayList.class);
        List<AbsenceWebBean> absenceWebBeans = (List<AbsenceWebBean>) response.getEntity();

        assertThat(absenceWebBeans).isNotNull().usingFieldByFieldElementComparator().containsExactlyElementsOf(absenceWebBeanTransformed);
        verify(mockAbsenceService).findAbsencesByMember(eq(randomId));

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
        testCreateAbsence_exception(new DateOverlapException(), (response) -> checkResponseForbidden(response));
    }

    @Test
    public void testCreateAbsence_badRequest() throws Exception {
        testCreateAbsence_exception(new InconsistentDateException(), (response) -> checkResponseBadRequest(response));
    }


    private void testCreateAbsence_exception(Exception exception, Consumer<Response> consumer) throws Exception {
        AbsenceWebBean absenceWebBean = AbsenceWebBeanTransformer.transform(EntityFactory.createAbsence());

        doThrow(exception).when(mockAbsenceService).addAbsence(isA(Absence.class), anyLong());
        Response response = absenceEndPoint.createAbsence(randomId, absenceWebBean);

        verify(mockAbsenceService).addAbsence(isA(Absence.class), eq(randomId));
        verify(mockLogger).warn(isA(String.class), eq(exception));
        consumer.accept(response);
    }


    @Test
    public void testDeleteAbsence() throws Exception {

        Absence absence = EntityFactory.createAbsence();
        Long memberId = EntityFactory.createRandomLong();
        absence.getMember().setId(memberId);
        when(mockAbsenceService.find(eq(randomId))).thenReturn(absence);

        Response response = absenceEndPoint.deleteAbsence(memberId, randomId);
        verify(mockAbsenceService).remove(eq(absence));
        checkResponseNoContent(response);
    }

    @Test
    public void testDeleteAbsence_null() throws Exception {
        Response response = absenceEndPoint.deleteAbsence(randomId, randomId);
        verify(mockAbsenceService, never()).remove(isA(Absence.class));
        checkResponseNotFound(response);
    }

    @Test
    public void testDeleteAbsence_notauthorized() throws Exception {

        Absence absence = EntityFactory.createAbsence();
        Long memberId = EntityFactory.createRandomLong();
        absence.getMember().setId(memberId);
        when(mockAbsenceService.find(eq(randomId))).thenReturn(absence);

        Response response = absenceEndPoint.deleteAbsence(randomId, randomId);
        verify(mockAbsenceService, never()).remove(isA(Absence.class));
        checkResponseForbidden(response);
    }


}