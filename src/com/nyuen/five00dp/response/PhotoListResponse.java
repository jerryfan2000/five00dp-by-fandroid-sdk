package com.nyuen.five00dp.response;

import java.util.ArrayList;

import com.nyuen.five00dp.structure.Photo;

public class PhotoListResponse {
    public String feature;
    public int current_page;
    public int total_pages;
    public int total_items;
    public ArrayList<Photo> photos;
}
