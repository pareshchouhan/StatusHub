package com.futuretraxex.statushub;

import android.test.AndroidTestCase;

import com.futuretraxex.statushub.Utility.Constants;
import com.futuretraxex.statushub.Utility.Utility;
import com.orhanobut.logger.Logger;

/**
 * Created by hudelabs on 10/24/2015.
 */
public class TestUtility extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Logger.init();
    }

    public void testUtility()    {
        Logger.w("Running Utility Check Test");
        int ethnicityTest1 = Constants.ETHNICITY_ASIAN;
        assertEquals("Asian is asian fail", "Asian", Utility.getEthnicityFromId(ethnicityTest1));
        int ethnicityTest2 = Constants.ETHNICITY_JEWISH;
        assertEquals("Jewish is Jewish fail", "Jewish", Utility.getEthnicityFromId(ethnicityTest2));
        long weightTest1 = 1000;
        long weightTest2 = 1200;
        assertEquals("1000g is 1kg fail", 1.f, Utility.convertWeightToKg(weightTest1));
        assertEquals("1200g is 1.2kg fail", 1.2f, Utility.convertWeightToKg(weightTest2));

    }
}
