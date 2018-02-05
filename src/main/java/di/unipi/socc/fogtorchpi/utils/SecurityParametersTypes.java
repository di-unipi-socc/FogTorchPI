package di.unipi.socc.fogtorchpi.utils;

import java.util.List;
import static java.util.Arrays.asList;

public class SecurityParametersTypes {
    // security countermeasures types
    public static final String VIRTUALISATION = "Virtualisation";
    public static final String COMMUNICATIONS = "Communications";
    public static final String DATA = "Data";
    public static final String SECURITY_AUDIT = "Security_audit";
    public static final String PHYSICAL = "Physical";

    public static List<String> getParametersTypes(){
        return asList(VIRTUALISATION, COMMUNICATIONS, DATA, SECURITY_AUDIT, PHYSICAL);
    }
}

