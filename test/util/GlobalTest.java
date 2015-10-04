package util;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.util.Modules;
import controllers.setup.Global;
import controllers.setup.ProdModule;

public class GlobalTest extends Global {

    public GlobalTest(AbstractModule...testModule) {
        super(Guice.createInjector(Modules.override(new ProdModule()).with(testModule)));
    }
}