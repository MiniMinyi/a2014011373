package com.ihandy.a2014011373.Database;

import com.activeandroid.annotation.Column;

/**
 * Created by liumy on 16/9/8.
 * Database Item
 */
public class NewsForSave {
    @Column(name = "Name")
    public String name;

    @Column(name = "Category")
    public Category category;

    public NewsForSave(){
        super();
    }

    public NewsForSave(String name, Category category){
        super();

    }
}
