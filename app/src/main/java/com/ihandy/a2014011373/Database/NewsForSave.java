package com.ihandy.a2014011373.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by liumy on 16/9/8.
 * Database Item
 */
public class NewsForSave extends Model {
    @Column(name = "Title", index = true)
    public String title;

    @Column(name = "CategoryName")
    public String category_name;

    @Column(name = "news_id")
    public long news_id;

    @Column(name = "img_url")
    public String img_url;

    @Column(name = "source_url")
    public String source_url;

    @Column(name = "Category", index = true)
    public Category category;

    public NewsForSave(){
        super();
    }
}
