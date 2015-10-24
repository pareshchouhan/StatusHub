package com.futuretraxex.statushub.Utility;

/**
 * Created by paresh on 10/24/2015.
 */
public class Utility {



    public static String getEthnicityFromId(int id) {
        switch(id)    {
            case Constants.ETHNICITY_ASIAN:
                return "Asian";
            case Constants.ETHNICITY_INDIAN:
                return "Indian";
            case Constants.ETHNICITY_AFRICAN_AMERICAN:
                return "African American";
            case Constants.ETHNICITY_ASIAN_AMERICAN:
                return "Asian American";
            case Constants.ETHNICITY_EUROPEAN:
                return "European";
            case Constants.ETHNICITY_BRITISH:
                return "British";
            case Constants.ETHNICITY_JEWISH:
                return "Jewish";
            case Constants.ETHNICITY_LATINO:
                return "Latino";
            case Constants.ETHNICITY_NATIVE_AMERICAN:
                return "Native Americans";
            case Constants.ETHNICITY_ARABIC:
                return "Arabic";
            default:
                return "N/A";
        }
    }

    public static int getIdFromEthnicity(String ethnicity)   {
        ethnicity = ethnicity.toLowerCase();



        switch(ethnicity)   {
            case "asian":
                return Constants.ETHNICITY_ASIAN;
            case "indian":
                return Constants.ETHNICITY_INDIAN;
            case "african american":
                return Constants.ETHNICITY_AFRICAN_AMERICAN;
            case "asian american":
                return Constants.ETHNICITY_ASIAN_AMERICAN;
            case "european":
                return Constants.ETHNICITY_EUROPEAN;
            case "british":
                return Constants.ETHNICITY_BRITISH;
            case "jewish":
                return Constants.ETHNICITY_JEWISH;
            case "latino":
                return Constants.ETHNICITY_LATINO;
            case "native americans":
                return Constants.ETHNICITY_NATIVE_AMERICAN;
            case "arabic":
                return Constants.ETHNICITY_ARABIC;
            //if no ethnicity consider him asian
            //as almost every 5th person is asian.
//            default:
//                return Constants.ETHNICITY_ASIAN;

        }

        //Check ut TestUtility.java for more test cases.
        //try regex matches in case our naive user enters
        // "singh is bling indian" -- match true.
        //
        if(ethnicity.matches(".*(asian|asia).*"))    {
            return Constants.ETHNICITY_ASIAN;
        }
        else if(ethnicity.matches(".*(indian|india|bharat).*")) {
            return Constants.ETHNICITY_INDIAN;
        }
        else if(ethnicity.matches(".*(african american|african).*")) {
            return Constants.ETHNICITY_AFRICAN_AMERICAN;
        }
        else if(ethnicity.matches(".*(asian american).*")) {
            return Constants.ETHNICITY_ASIAN_AMERICAN;
        }
        else if(ethnicity.matches(".*(european|euro|eu).*")) {
            return Constants.ETHNICITY_EUROPEAN;
        }
        else if(ethnicity.matches(".*(british|brits|uk).*")) {
            return Constants.ETHNICITY_BRITISH;
        }
        else if(ethnicity.matches(".*(jewish|jew).*")) {
            return Constants.ETHNICITY_JEWISH;
        }
        else if(ethnicity.matches(".*(latino|esp|espanol).*")) {
            return Constants.ETHNICITY_LATINO;
        }
        else if(ethnicity.matches(".*(native americans|native|hispanic).*")) {
            return Constants.ETHNICITY_NATIVE_AMERICAN;
        }
        else if(ethnicity.matches(".*(arab).*")) {
            return Constants.ETHNICITY_ARABIC;
        }
        else {
            //if no ethnicity consider him asian
            //as almost every 5th person is asian.
            return Constants.ETHNICITY_ASIAN;
        }

    }

    public static float convertWeightToKg(long grams)  {
        return ((float)grams/1000.0f);
    }

}
