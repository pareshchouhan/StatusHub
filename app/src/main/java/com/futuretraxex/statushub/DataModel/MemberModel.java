package com.futuretraxex.statushub.DataModel;

import com.futuretraxex.statushub.Utility.Utility;

/**
 * Created by paresh on 10/24/2015.
 */
public class MemberModel {

    public int id;
    public String dob;
    public String status;
    public int ethnicity;
    public long weight;
    public int height;
    public int is_veg;
    public int drink;
    public String image;

    public MemberModel(int _id, String _dob, String _status, int _ethnicity, long _weight, int _height, int _is_veg, int _drink, String _image_url) {
        this.id = _id;
        this.dob = _dob;
        this.status = _status;
        this.ethnicity = _ethnicity;
        this.weight = _weight;
        this.height = _height;
        this.is_veg = _is_veg;
        this.drink = _drink;
        this.image = _image_url;
    }

}
