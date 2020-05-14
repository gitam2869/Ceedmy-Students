package com.example.ceedmyfinal;

public class GoalSetInfo
{
    private String id;
    private String name;
    private String path;
    private String videoLink;



    public GoalSetInfo(
            String id,
            String name,
            String path,
            String videoLink
    )
    {
        this.id = id;
        this.name = name;
        this.path = path;
        this.videoLink = videoLink;
    }

    public String getid()
    {
        return id;
    }

    public String getname()
    {
        return name;
    }

    public String getpath()
    {
        return path;
    }

    public String getVideoLink(){return videoLink;}

}
