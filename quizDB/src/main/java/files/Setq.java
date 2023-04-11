package files;

import java.util.List;

public class Setq {
    private int examId;
    private List<String> question;
    private int counter;

    public Setq() {
    }

    public Setq(int examId, List<String> question, int counter) {
        this.examId = examId;
        this.question = question;
        this.counter = counter;
    }
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }
    public List<String> getQuestion() {
        return question;
    }
    public void setQuestion(List<String> question) {
        this.question = question;
    }
}


