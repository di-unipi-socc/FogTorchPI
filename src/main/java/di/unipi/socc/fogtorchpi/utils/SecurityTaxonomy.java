package di.unipi.socc.fogtorchpi.utils;

import java.util.ArrayList;
import java.util.List;


import static di.unipi.socc.fogtorchpi.utils.SecurityParameters.*;
import static di.unipi.socc.fogtorchpi.utils.SecurityParametersTypes.*;
import static java.util.Arrays.asList;

public class SecurityTaxonomy {
    public static String getType(String securityMeasure){
        String result = null;
        switch (securityMeasure) {
            case AUTHENTICATION: case IDS_HOST: case ACCESS_LOGS: case PERMISSION_MODEL: case RESTORE_POINTS:
                result = VIRTUALISATION;
                break;
            case FIREWALL: case IDS_NETWORK: case ENCRYPTION: case PUBLICK_KEY: case CERTIFICATE: case WIRELESS_SECURITY:
                result = COMMUNICATIONS;
                break;
            case ENCRYPTED_STORAGE: case BACKUP:
                result = DATA;
                break;
            case AUDIT:
                result = SECURITY_AUDIT;
                break;
            case ANTI_TAMPERING:
                result = PHYSICAL;
                break;
            default:
                break;
        }
        return result;
    }

    public static ArrayList<String> getSecurityMeasuresByType(String type){
        List<String> result = null;
        switch (type) {
            case VIRTUALISATION:
                result = asList(AUTHENTICATION, IDS_HOST, ACCESS_LOGS,PERMISSION_MODEL, RESTORE_POINTS);
                break;
            case COMMUNICATIONS:
                result = asList(FIREWALL, IDS_NETWORK, ENCRYPTION, PUBLICK_KEY, CERTIFICATE, WIRELESS_SECURITY);
                break;
            case DATA:
                result = asList(ENCRYPTED_STORAGE, BACKUP);
                break;
            case SECURITY_AUDIT:
                result = asList(AUDIT);
                break;
            case PHYSICAL:
                result = asList(ANTI_TAMPERING);
                break;
            default:
                result = asList(AUTHENTICATION, IDS_HOST, ACCESS_LOGS,PERMISSION_MODEL,
                        RESTORE_POINTS,FIREWALL, IDS_NETWORK, ENCRYPTION, PUBLICK_KEY, CERTIFICATE, WIRELESS_SECURITY,
                        ENCRYPTED_STORAGE,
                        BACKUP, AUDIT, ANTI_TAMPERING);
                break;
        }
        return new ArrayList<>(result);
    }

}
