package com.nyuen.five00dp.response;

import java.util.ArrayList;

import com.nyuen.five00dp.structure.Comment;

public class CommentResponse {
    public String media_type;
    public int current_page;
    public int total_pages;
    public int total_items;
    public ArrayList<Comment> comments;
}
