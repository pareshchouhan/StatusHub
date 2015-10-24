package com.futuretraxex.statushub;

import android.test.AndroidTestCase;

import com.futuretraxex.statushub.database.StatusHubContract;

/**
 * Created by hudelabs on 10/24/2015.
 */
public class TestUriGenerator extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testUriBuilders() {
        String contentPath = StatusHubContract.UsersSchema.CONTENT_URI.buildUpon().appendPath(StatusHubContract.PATH_USERS).build().toString();
        String contentPathTest1 = StatusHubContract.UsersSchema.CONTENT_URI.toString();
        assertEquals("User Uri With ID Fail", contentPathTest1 + "/3", StatusHubContract.UsersSchema.buildUsersUriWithId(3).toString());

        assertEquals("User Uri With Favourites Fail", contentPath + "/favourites", StatusHubContract.UsersSchema.buildUsersUriWithFavourites().toString());
        assertEquals("User Uri With WeightSortFilter Fail", contentPath + "/weight", StatusHubContract.UsersSchema.buildUsersUriWithWeightSortFilter().toString());
        assertEquals("User Uri With HeightSortFilter Fail", contentPath + "/height", StatusHubContract.UsersSchema.buildUsersUriWithHeightSortFilter().toString());


        assertEquals("User Uri With WeightFilter Fail", contentPath + "/weight/3000", StatusHubContract.UsersSchema.buildUsersUriWithWeightFilter(3000).toString());
        assertEquals("User Uri With WeightFilter Fail", contentPath + "/height/170", StatusHubContract.UsersSchema.buildUsersUriWithHeightFilter(170).toString());


        assertEquals("User Uri With EthnicityFilter Fail", contentPath + "/ethnicity/0", StatusHubContract.UsersSchema.buildUsersWithEthnicityFilter("asia").toString());
        assertEquals("User Uri With EthnicityFilter Fail", contentPath + "/ethnicity/0", StatusHubContract.UsersSchema.buildUsersWithEthnicityFilter("asian").toString());

        assertEquals("User Uri With EthnicityFilter Fail", contentPath + "/ethnicity/1", StatusHubContract.UsersSchema.buildUsersWithEthnicityFilter("india").toString());

        assertEquals("User Uri With EthnicityFilter Fail", contentPath + "/ethnicity/0", StatusHubContract.UsersSchema.buildUsersWithEthnicityFilter("Random stuff").toString());

    }
}
