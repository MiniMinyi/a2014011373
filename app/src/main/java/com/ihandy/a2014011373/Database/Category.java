package com.ihandy.a2014011373.Database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by liumy on 16/9/8.
 * Database model.
 */
@Table(name = "Categories")
public class Category extends Model{
    @Column(name = "Name")
    public String name;
}
