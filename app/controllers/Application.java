package controllers;

import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public Result swagger() {
        return ok(swaggerui.render());
    }

    public Result ws() {
        return ok(ws.render());
    }

}
