package com.example.ceedmyfinal;

public class GoalSetInfo
{
    private String id;
    private String examcategory;
    private String path;

    public GoalSetInfo(String id, String examcategory, String path)
    {
        this.id = id;
        this.examcategory = examcategory;
        this.path = path;
    }

    public String getId()
    {
        return id;
    }

    public String getExamcategory()
    {
        return examcategory;
    }

    public String getPath()
    {
        return path;
    }
}
