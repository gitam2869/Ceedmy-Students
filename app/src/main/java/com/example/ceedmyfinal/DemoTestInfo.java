package com.example.ceedmyfinal;

public class DemoTestInfo
{
    private String id;
    private String examname;
    private String no;
    private String question;
    private String op1;
    private String op2;
    private String op3;
    private String op4;
    private String answer;

    public DemoTestInfo(
            String id,
            String examname,
            String no,
            String question,
            String op1,
            String op2,
            String op3,
            String op4,
            String answer
    )
    {
        this.id = id;
        this.examname = examname;
        this.no = no;
        this.question = question;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
        this.answer = answer;
    }

    public String getId()
    {
        return id;
    }

    public String getExamname()
    {
        return examname;
    }

    public String getNo()
    {
        return no;
    }

    public String getQuestion()
    {
        return question;
    }

    public String getOp1()
    {
        return op1;
    }

    public String getOp2()
    {
        return op2;
    }

    public String getOp3()
    {
        return op3;
    }

    public String getOp4()
    {
        return op4;
    }

    public String getAnswer()
    {
        return answer;
    }

}
