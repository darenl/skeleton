package controllers;

import com.google.common.io.Resources;
import java.io.IOException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static java.nio.charset.StandardCharsets.UTF_8;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class StaticHtmlController {

    @GET
    public String getIndexPage() throws IOException {
        Resources.getResource("young.html");
        return Resources.toString(Resources.getResource("young.html"), UTF_8);
    }
}
