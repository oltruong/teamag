package fr.oltruong.teamag.rest.filter;

import fr.oltruong.teamag.ejb.MemberEJB;
import fr.oltruong.teamag.interfaces.SecurityChecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

/**
 * @author Olivier Truong
 */
@SecurityChecked
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private final String AUTHORIZATION_PROPERTY = "Authorization";


    private Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        MultivaluedMap<String, String> map = containerRequestContext.getHeaders();

        for (String mapString : map.keySet()) {

            StringBuffer stringBuffer = new StringBuffer();
            for (String value : map.get(mapString)) {
                stringBuffer.append(value);
            }
            logger.info(mapString + " " + stringBuffer.toString());
        }

        logger.info("sizer size" + MemberEJB.getMemberList().size());

        final List<String> authorization = containerRequestContext.getHeaders().get(AUTHORIZATION_PROPERTY);
        // final List<String> authorization = headers.getRequestHeader(AUTHORIZATION_PROPERTY);

        if (authorization != null) {
            for (String string : authorization) {
                System.out.print(string);
            }

        } else {
            logger.warn("pas autorise");

//            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        logger.info("fini");
    }
}
