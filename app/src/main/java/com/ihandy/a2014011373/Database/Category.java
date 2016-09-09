package com.ihandy.a2014011373.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by liumy on 16/9/8.
 * Database model.
 */
@Table(name = "Categories")
public class Category extends Model{
    @Column(name = "Name")
    public String name;

    @Column(name = "Key")
    public String json_key;

    @Column(name = "isWatched")
    public Boolean isWatched;

    @Column(name = "showOrder")
    public Integer order;

    public Category(){
        super();
    }
}
