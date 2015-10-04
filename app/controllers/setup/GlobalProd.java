package controllers.setup;

import com.google.inject.Guice;

public class GlobalProd extends Global {

    public GlobalProd() {
        super(Guice.createInjector(new ProdModule()));
    }

}
