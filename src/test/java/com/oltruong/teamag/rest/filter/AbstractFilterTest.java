package com.oltruong.teamag.rest.filter;

import com.google.common.collect.Maps;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.model.Member;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractFilterTest {


    @Mock
    protected ContainerRequestContext mockContainerRequestContext;


    protected SecurityFilter securityFilter;

    protected Member member;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        securityFilter = getSecurityFilter();

        MultivaluedMap<String, String> headersMap = new MultivaluedHashMap<>();
        final String password = "PASSWORD";
        headersMap.putSingle("Authorization", password);
        headersMap.putSingle("userid", "1");

        when(mockContainerRequestContext.getHeaders()).thenReturn(headersMap);
        Request mockRequest = mock(Request.class);
        when(mockContainerRequestContext.getRequest()).thenReturn(mockRequest);

        member = EntityFactory.createMember();
        member.setId(1L);
        member.setPassword(password);

        Map<Long, Member> memberMap = Maps.newHashMapWithExpectedSize(1);
        memberMap.put(1L, member);

        TestUtils.setPrivateAttribute(new MemberService(), memberMap, "memberMap");
    }

    protected abstract SecurityFilter getSecurityFilter();


}